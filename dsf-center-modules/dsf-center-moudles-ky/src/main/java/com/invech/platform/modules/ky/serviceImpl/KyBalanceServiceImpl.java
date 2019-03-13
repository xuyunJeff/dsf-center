package com.invech.platform.modules.ky.serviceImpl;

import com.alibaba.fastjson.JSONObject;
import com.invech.platform.dsfcenterdata.AbstractBalanceService;
import com.invech.platform.dsfcenterdata.entity.DsfGmApi;
import com.invech.platform.dsfcenterdata.entity.DsfMemberUser;
import com.invech.platform.dsfcenterdata.entity.DsfTransferLog;
import com.invech.platform.dsfcenterdata.enums.FailReason;
import com.invech.platform.dsfcenterdata.enums.TransferState;
import com.invech.platform.dsfcenterdata.enums.TransferType;
import com.invech.platform.dsfcenterdata.utils.DateUtil;
import com.invech.platform.dsfcenterdata.utils.GenerationSequenceUtil;
import com.invech.platform.modules.common.constants.KyConstans;
import com.invech.platform.modules.common.dto.ky.*;
import com.invech.platform.modules.common.enums.KyErrorCode;
import com.invech.platform.modules.ky.http.KyNetWork;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @ClassName: KyBalanceServiceImpl
 * @Author: R.M.I
 * @CreateTime: 2019年02月27日 14:49:00
 * @Description: TODO
 * @Version 1.0.0
 */
@Service("KyBalanceService")
@Slf4j
public class KyBalanceServiceImpl extends AbstractBalanceService {

    @Autowired
    KyNetWork netWork;

    @Override
    public DsfTransferLog performTransfer(DsfMemberUser dsfMemberUser, DsfTransferLog dsfTransferLog, DsfGmApi dsfGmApi) {
        String method = KyConstans.TOP_UP;
        if(dsfTransferLog.getType().equals(TransferType.Withdraw.getType())){
            method = KyConstans.WITHDRAW;
        }
        KyTransferRequest kyTransferRequest = new KyTransferRequest(method,dsfGmApi,dsfMemberUser.getDsfPlayerId(),dsfTransferLog.getAmount(),dsfTransferLog.getDsfTransactionId());
        KyReponse<KyReponseD> response = netWork.get(dsfGmApi,kyTransferRequest);
        if (response == null) {
            dsfTransferLog.setState(TransferState.UnKnow.code);
            return dsfTransferLog;
        }
        KyReponseD reponseD=JSONObject.parseObject(JSONObject.toJSONString(response.getD()),KyReponseD.class);
        if (reponseD.getCode().equals(KyErrorCode.Success.getCode())) {
            dsfTransferLog.setState(TransferState.Successful.code);
            return dsfTransferLog;
        }
        if (reponseD.getCode().equals(KyErrorCode.DataNotExist.getCode()) || reponseD.getCode().equals(KyErrorCode.BalanceNotAvailable.getCode()) || reponseD.getCode().equals(KyErrorCode.TranstaitonExist.getCode())) {
            dsfTransferLog.setState(TransferState.Failed.code);
            if(reponseD.getCode().equals(KyErrorCode.BalanceNotAvailable.getCode())){
                dsfTransferLog.setFailReason(FailReason.BalanceNotAlailable.message);
            }
            return dsfTransferLog;
        }
        dsfTransferLog.setState(TransferState.UnKnow.code);
        log.error("Ky 转账错误 ",KyErrorCode.kyError(reponseD.getCode()));
        return dsfTransferLog;
    }

    @Override
    public DsfTransferLog checkTransferStatus(DsfTransferLog dsfTransferLog, DsfGmApi dsfGmApi) {
        KyCheckStatusRequest request = new KyCheckStatusRequest(KyConstans.CHECK_STATUS,dsfGmApi,dsfTransferLog.getDsfTransactionId());
        KyReponse<KyCheckStatusReponse> response = netWork.get(dsfGmApi,request);

        if (response == null) {
            dsfTransferLog.setState(TransferState.UnKnow.code);
            return dsfTransferLog;
        }
        KyCheckStatusReponse kyCheckStatusReponse=JSONObject.parseObject(JSONObject.toJSONString(response.getD()),KyCheckStatusReponse.class);
        if (kyCheckStatusReponse.getCode().equals(KyErrorCode.Success.getCode())) {
            Integer status=kyCheckStatusReponse.getStatus();
            if(status == -1 || status == 2){
                dsfTransferLog.setState(TransferState.Failed.code);
                return dsfTransferLog;
            }else if(status == 0){
                dsfTransferLog.setState(TransferState.Successful.code);
                return dsfTransferLog;
            }
        }
        if (kyCheckStatusReponse.getCode().equals(KyErrorCode.DataNotExist.getCode())) {
            dsfTransferLog.setState(TransferState.Failed.code);
            return dsfTransferLog;
        }
        log.error("Ky 开始游戏请求异常" + KyErrorCode.kyError(kyCheckStatusReponse.getCode()).getMsg());
        return dsfTransferLog;
    }

    @Override
    public String gengerateDsfTransactionId(DsfMemberUser dsfMemberUser, String transactionId, DsfGmApi dsfGmApi) {
        Long timestamp = DateUtil.currentTimestamp();
        return dsfGmApi.getAgyAcc()+DateUtil.getSimpleDateFormat(DateUtil.FORMAT_17_DATE_TIME, timestamp)+dsfMemberUser.getDsfPlayerId() + timestamp;
    }
}
