package com.xuyun.platform.modules.ag.serviceImpl;


import com.xuyun.platform.dsfcenterdao.mapper.DsfTransferLogMapper;
import com.xuyun.platform.dsfcenterdata.AbstractBalanceService;
import com.xuyun.platform.dsfcenterdata.entity.DsfGmApi;
import com.xuyun.platform.dsfcenterdata.entity.DsfMemberUser;
import com.xuyun.platform.dsfcenterdata.entity.DsfTransferLog;
import com.xuyun.platform.dsfcenterdata.enums.FailReason;
import com.xuyun.platform.dsfcenterdata.enums.TransferState;
import com.xuyun.platform.dsfcenterdata.enums.TransferType;
import com.xuyun.platform.dsfcenterdata.utils.DateUtil;
import com.xuyun.platform.dsfcenterdata.utils.GenerationSequenceUtil;
import com.xuyun.platform.modules.ag.http.AgNetWork;
import com.xuyun.platform.modules.common.constants.AgConstants;
import com.xuyun.platform.modules.common.dto.ag.AgDataDto;
import com.xuyun.platform.modules.common.dto.ag.AgResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName: AgBalanceServiceImpl
 * @Author: R.M.I
 * @CreateTime: 2019年02月23日 10:47:00
 * @Description: TODO
 * @Version 1.0.0
 */
@Service("AgBalanceService")
@Slf4j
public class AgBalanceServiceImpl extends AbstractBalanceService {

    @Autowired
    DsfTransferLogMapper dsfTransferLogMapper;

    @Autowired
    private AgNetWork agNetWork;

    @Override
    public DsfTransferLog performTransfer(DsfMemberUser dsfMemberUser, DsfTransferLog dsfTransferLog, DsfGmApi dsfGmApi) {
        return prepareTransferCredit(dsfMemberUser,dsfGmApi,dsfTransferLog);
    }

    @Override
    public DsfTransferLog checkTransferStatus(DsfTransferLog dsfTransferLog,DsfGmApi dsfGmApi) {
        AgDataDto agDataDto = new AgDataDto();
        agDataDto.setActype(AgConstants.Actype.trueAccount);
        agDataDto.setCagent(dsfGmApi.getAgyAcc());
        agDataDto.setBillno(dsfTransferLog.getDsfTransactionId());
        agDataDto.setMethod(AgConstants.AGIN_FUN_QUERYORDERSTATUS);
        AgResponseDto agResponseDto=agNetWork.post(dsfGmApi,agDataDto);
        // 0 成功
        // 1 失败, 订单未处理状态
        if(AgConstants.AGIN_FUN_SUCC_KEY.equals(agResponseDto.getInfo())){
            dsfTransferLog.setState(TransferState.Successful.code);
        }else if(AgConstants.AGIN_FUN_FAILED_KEY.equals(agResponseDto.getInfo())){
            log.info(agResponseDto.toString());
            dsfTransferLog.setState(TransferState.Failed.code);
            dsfTransferLog.setFailReason(FailReason.DsfBalanceUpdateFailed.message);
        }
        return dsfTransferLog;
    }

    /**
     * billno = (cagent+序列), 序列是唯一的 13~16 位数, 例如:
     * cagent = ‘XXXXX’ 及 序列 = 1234567890987, 那么
     * billno = XXXXX1234567890987,
     * @param dsfMemberUser
     * @param transactionId
     * @param dsfGmApi
     * @return
     */
    @Override
    public String gengerateDsfTransactionId( DsfMemberUser dsfMemberUser,String transactionId,DsfGmApi dsfGmApi) {
        Long timestamp=DateUtil.currentTimestamp();
        AgDataDto agDataDto = new AgDataDto().getAgDataDto(dsfMemberUser,dsfGmApi);
        return agDataDto.getCagent()+ GenerationSequenceUtil.generateRandomNum(3) +timestamp;
    }


    private DsfTransferLog prepareTransferCredit(DsfMemberUser dsfMemberUser, DsfGmApi gmApi, DsfTransferLog dsfTransferLog) {
        AgDataDto agDataDto = new AgDataDto().getAgDataDto(dsfMemberUser,gmApi);
        agDataDto.setMethod(AgConstants.AGIN_FUN_PREPARETRANSFERCREDIT);
        agDataDto.setBillno(dsfTransferLog.getDsfTransactionId());
        String type = dsfTransferLog.getType().equals(TransferType.Withdraw.getType())?"OUT":"IN";
        agDataDto.setType(type);
        agDataDto.setCredit(dsfTransferLog.getAmount());
        AgResponseDto agResponseDto=agNetWork.post(gmApi,agDataDto);
        if(AgConstants.AGIN_FUN_SUCC_KEY.equals(agResponseDto.getInfo())){
            //此处标记为注单Unknow,去查询订单状态
            dsfTransferLog.setState(TransferState.UnKnow.code);
            //下一个流程，转账流程
            return transferCreditConfirm(gmApi,agDataDto,dsfTransferLog);
        }else {
            log.info(agResponseDto.toString());
            dsfTransferLog.setState(TransferState.Failed.code);
            if(agResponseDto.getMessage().contains("not enough credit")) {
                dsfTransferLog.setFailReason(FailReason.DsfBalanceUpdateFailed.message);
            }
        }
        return dsfTransferLog;
    }

    private DsfTransferLog transferCreditConfirm(DsfGmApi gmApi, AgDataDto agDataDto, DsfTransferLog dsfTransferLog) {
        agDataDto.setFlag(AgConstants.PPreTrf.suc);
        agDataDto.setMethod(AgConstants.AGIN_FUN_TRANSFERCREDITCONFIRM);
        AgResponseDto agResponseDto=agNetWork.post(gmApi,agDataDto);
        if(AgConstants.AGIN_FUN_SUCC_KEY.equals(agResponseDto.getInfo())){
            dsfTransferLog.setState(TransferState.Successful.code);
        }
        //此处订单为UnKnow 状态
        return dsfTransferLog;
    }
}
