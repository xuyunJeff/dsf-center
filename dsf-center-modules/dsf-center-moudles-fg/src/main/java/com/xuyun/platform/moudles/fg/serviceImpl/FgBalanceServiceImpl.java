package com.xuyun.platform.moudles.fg.serviceImpl;

import com.xuyun.platform.dsfcenterdata.AbstractBalanceService;
import com.xuyun.platform.dsfcenterdata.entity.DsfGmApi;
import com.xuyun.platform.dsfcenterdata.entity.DsfMemberUser;
import com.xuyun.platform.dsfcenterdata.entity.DsfTransferLog;
import com.xuyun.platform.dsfcenterdata.enums.FailReason;
import com.xuyun.platform.dsfcenterdata.enums.TransferState;
import com.xuyun.platform.dsfcenterdata.enums.TransferType;
import com.xuyun.platform.modules.common.constants.FgConstants;
import com.xuyun.platform.modules.common.dto.fg.FgResponse;
import com.xuyun.platform.modules.common.dto.fg.FgTransferRequest;
import com.xuyun.platform.modules.common.enums.FgErrorCode;
import com.xuyun.platform.moudles.fg.http.FgNetWork;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * @ClassName: FgBalanceServiceImpl
 * @Author: R.M.I
 * @CreateTime: 2019年02月27日 14:49:00
 * @Description: TODO
 * @Version 1.0.0
 */
@Service("FgBalanceService")
@Slf4j
public class FgBalanceServiceImpl extends AbstractBalanceService {

    @Autowired
    FgNetWork netWork;

    @Override
    public DsfTransferLog performTransfer(DsfMemberUser dsfMemberUser, DsfTransferLog dsfTransferLog, DsfGmApi dsfGmApi) {
        int amount = Integer.parseInt(dsfTransferLog.getAmount().multiply(BigDecimal.valueOf(100)).toString().split("\\.")[0]);
        if (dsfTransferLog.getType().equals(TransferType.Withdraw.getType())) {
            amount = amount * -1;
        }
        FgTransferRequest fgTransferRequest = new FgTransferRequest(amount, dsfTransferLog.getDsfTransactionId());
        FgResponse response = netWork.post(dsfGmApi, fgTransferRequest, FgConstants.PLAYER_UCHIPS+dsfMemberUser.getDsfPlayerId());
        if (response == null) {
            dsfTransferLog.setState(TransferState.UnKnow.code);
            return dsfTransferLog;
        }
        if (response.getCode().equals(FgConstants.FG_SUCCESS_CODE)) {
            dsfTransferLog.setState(TransferState.Successful.code);
            return dsfTransferLog;
        } else if (response.getCode().equals(FgErrorCode.NotEexist.getCode()) ||
                response.getCode().equals(FgErrorCode.PlayerBalance.getCode())||
                response.getCode().equals(FgErrorCode.ApiServerMaintaining.getCode()) ||
                response.getCode().equals(FgErrorCode.ChipsUpdateFail.getCode())||
                response.getCode().equals(FgErrorCode.PlayerDoesNotExist.getCode())||
                response.getCode().equals(FgErrorCode.TransactionidExisted.getCode())
        ) {
            dsfTransferLog.setState(TransferState.Failed.code);
            if(response.getCode().equals(FgErrorCode.PlayerBalance.getCode())){
                dsfTransferLog.setFailReason(FailReason.BalanceNotAlailable.message);
            }
            return dsfTransferLog;
        }
        log.error("Fg转账错误 response : {}",response);
        dsfTransferLog.setState(TransferState.UnKnow.code);
        return dsfTransferLog;

    }

    @Override
    public DsfTransferLog checkTransferStatus(DsfTransferLog dsfTransferLog, DsfGmApi dsfGmApi) {
        FgResponse response = netWork.post(dsfGmApi,null,FgConstants.UCHIPS_CHECK+dsfTransferLog.getDsfTransactionId());
        if (response == null) {
            dsfTransferLog.setState(TransferState.UnKnow.code);
            return dsfTransferLog;
        }
        if (response.getCode().equals(FgConstants.FG_SUCCESS_CODE)) {
            dsfTransferLog.setState(TransferState.Successful.code);
            return dsfTransferLog;
        }else if (response.getCode().equals(FgErrorCode.NotEexist.getCode()) || response.getCode().equals(FgErrorCode.TransferFailed.getCode())){
            dsfTransferLog.setState(TransferState.Failed.code);
            if(response.getCode().equals(FgErrorCode.NotEexist.getCode())){
                dsfTransferLog.setFailReason(FailReason.TranstaitonNotExist.message);
            }
            if(response.getCode().equals(FgErrorCode.TransferFailed.getCode())){
                dsfTransferLog.setFailReason(FailReason.ApiServerMaintaining.message);
            }
            return dsfTransferLog;
        }
        return dsfTransferLog;
    }

    @Override
    public String gengerateDsfTransactionId(DsfMemberUser dsfMemberUser, String transactionId, DsfGmApi dsfGmApi) {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
