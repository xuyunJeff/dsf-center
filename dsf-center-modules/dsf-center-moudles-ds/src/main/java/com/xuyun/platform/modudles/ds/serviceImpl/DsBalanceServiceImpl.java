package com.xuyun.platform.modudles.ds.serviceImpl;

import com.xuyun.platform.dsfcenterdata.AbstractBalanceService;
import com.xuyun.platform.dsfcenterdata.entity.DsfGmApi;
import com.xuyun.platform.dsfcenterdata.entity.DsfMemberUser;
import com.xuyun.platform.dsfcenterdata.entity.DsfTransferLog;
import com.xuyun.platform.dsfcenterdata.enums.FailReason;
import com.xuyun.platform.dsfcenterdata.enums.TransferState;
import com.xuyun.platform.dsfcenterdata.enums.TransferType;
import com.xuyun.platform.dsfcenterdata.utils.MD5;
import com.xuyun.platform.modudles.ds.http.DsNetWork;
import com.xuyun.platform.modules.common.constants.DsConstants;
import com.xuyun.platform.modules.common.dto.ds.request.DsCheckStatus;
import com.xuyun.platform.modules.common.dto.ds.request.DsDeposit;
import com.xuyun.platform.modules.common.dto.ds.request.DsRequest;
import com.xuyun.platform.modules.common.dto.ds.response.DsResponse;
import com.xuyun.platform.modules.common.enums.DsErroeCodes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @ClassName: DsBalanceServiceImpl
 * @Author: R.M.I
 * @CreateTime: 2019年02月27日 14:49:00
 * @Description: TODO
 * @Version 1.0.0
 */
@Service("DsBalanceService")
@Slf4j
public class DsBalanceServiceImpl extends AbstractBalanceService {

    @Autowired
    DsNetWork dsNetWork;

    @Override
    public DsfTransferLog performTransfer(DsfMemberUser dsfMemberUser, DsfTransferLog dsfTransferLog, DsfGmApi dsfGmApi) {
        DsDeposit dsDeposit =new DsDeposit(dsfMemberUser.getDsfPlayerId(), MD5.getMD5(dsfMemberUser.getPassword()),dsfTransferLog.getDsfTransactionId(),"operator"+dsfTransferLog.getOperator(),String.valueOf(dsfTransferLog.getAmount()));
        DsRequest dsRequest = new DsRequest<>(dsDeposit);
        if(dsfTransferLog.getType().equals(TransferType.TopUp.getType())) {
             dsRequest.setCommand(DsConstants.COMMAND_DEPOSIT);
        }else {
            dsRequest.setCommand(DsConstants.COMMAND_WITHDRAW);
        }
        dsRequest.setHashCode(DsRequest.getHashCode(dsfGmApi));
        DsResponse dsResponse=dsNetWork.post(dsfGmApi,dsRequest);
        if(dsResponse == null ){
            dsfTransferLog.setState(TransferState.UnKnow.code);
            return dsfTransferLog;
        }
        if(dsResponse.getErrorCode() == DsErroeCodes.Success){
            log.info(dsResponse.toString());
            dsfTransferLog.setState(TransferState.Successful.code);
        }else if(dsResponse.getErrorCode() == DsErroeCodes.RequestError){
            try {
                Thread.sleep(2000);
                return   this.performTransfer(dsfMemberUser,dsfTransferLog,dsfGmApi);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else{
            log.info("Ds转账失败"+dsResponse.toString());
            dsfTransferLog.setState(TransferState.Failed.code);
            if(dsResponse.getErrorCode() == DsErroeCodes.LuckMoney){
                dsfTransferLog.setFailReason(FailReason.BalanceNotAlailable.message);
            }else if(dsResponse.getErrorCode() == DsErroeCodes.LoginError || dsResponse.getErrorCode() == DsErroeCodes.AccountLock ){
                dsfTransferLog.setFailReason(FailReason.PlayerAbnormable.message);
            }else {
                dsfTransferLog.setFailReason(FailReason.DsfBalanceUpdateFailed.message);
            }
        }


        return dsfTransferLog;
    }

    @Override
    public DsfTransferLog checkTransferStatus(DsfTransferLog dsfTransferLog, DsfGmApi dsfGmApi) {
        DsCheckStatus dsCheckStatus = new DsCheckStatus(dsfTransferLog.getDsfTransactionId());
        DsRequest dsRequest = new DsRequest<>(dsCheckStatus,DsConstants.COMMAND_CHECK_REF);
        dsRequest.setHashCode(DsRequest.getHashCode(dsfGmApi));
        DsResponse dsResponse =dsNetWork.post(dsfGmApi,dsRequest);
        if(dsResponse.getErrorCode() == DsErroeCodes.Success){
            //此处 error = 0 为 交易号不存在
            dsfTransferLog.setState(TransferState.Failed.code);
            dsfTransferLog.setFailReason(FailReason.TranstaitonNotExist.message);
        }
        if(dsResponse.getErrorCode() == DsErroeCodes.EefSuccess){
            //此处 error = 6601 为 成功
            dsfTransferLog.setState(TransferState.Successful.code);
        }
        return dsfTransferLog;
    }

    @Override
    public String gengerateDsfTransactionId(DsfMemberUser dsfMemberUser, String transactionId, DsfGmApi dsfGmApi) {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
