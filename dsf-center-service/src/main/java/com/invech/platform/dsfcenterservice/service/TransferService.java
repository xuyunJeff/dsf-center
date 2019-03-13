package com.invech.platform.dsfcenterservice.service;

import com.invech.platform.dsfcenterdao.mapper.DsfTransferLogMapper;
import com.invech.platform.dsfcenterdao.utlis.SiteUtil;
import com.invech.platform.dsfcenterdata.dto.MemberBalanceDto;
import com.invech.platform.dsfcenterdata.dto.WithdrawBalanceAll;
import com.invech.platform.dsfcenterdata.entity.DsfGmApi;
import com.invech.platform.dsfcenterdata.entity.DsfMemberUser;
import com.invech.platform.dsfcenterdata.entity.DsfTransferLog;
import com.invech.platform.dsfcenterdata.enums.ErrorCode;
import com.invech.platform.dsfcenterdata.enums.GamePlatform;
import com.invech.platform.dsfcenterdata.enums.TransferState;
import com.invech.platform.dsfcenterdata.enums.TransferType;
import com.invech.platform.dsfcenterdata.response.RRException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName: TransferService
 * @Author: R.M.I
 * @CreateTime: 2019年02月25日 20:32:00
 * @Description: TODO
 * @Version 1.0.0
 */
@Service
@Slf4j
public class TransferService extends BaseService {


    @Autowired
    DsfTransferLogMapper dsfTransferLogMapper;

    @Autowired
    GameService gameService;

    /**
     * 余额一键回归
     *
     * @param withdrawTransferList
     * @param memberUser
     * @param operater
     * @return
     */
    public List<DsfTransferLog> transferBalanceBackAll(List<WithdrawBalanceAll> withdrawTransferList, String memberUser, String operater) {
        List<DsfTransferLog> transferLogs = new ArrayList<>();
        for (WithdrawBalanceAll transferLog : withdrawTransferList) {
            GamePlatform gamePlatform = transferLog.getGamePlatform();
            //获取玩家余额
            MemberBalanceDto memberBalance = gameService.memberBalance(gamePlatform, memberUser);
            transferLogs.add(this.performTransfer(gamePlatform, TransferType.Withdraw, memberBalance.getBalance(), transferLog.getTransactionId(), operater, memberUser));
        }
        return transferLogs;
    }

    /**
     * 余额回归某个平台
     *
     * @param withdrawTransfer
     * @param memberUser
     * @param operater
     * @return
     */
    public DsfTransferLog transferBalanceBack(WithdrawBalanceAll withdrawTransfer, String memberUser, String operater) {
        GamePlatform gamePlatform = withdrawTransfer.getGamePlatform();
        //获取玩家余额
        MemberBalanceDto memberBalance = gameService.memberBalance(gamePlatform, memberUser);
        return this.performTransfer(gamePlatform, TransferType.Withdraw, memberBalance.getBalance(), withdrawTransfer.getTransactionId(), operater, memberUser);
    }

    /**
     * 玩家转账
     *
     * @param gamePlatform
     * @param transferType
     * @param amount
     * @param transactionId
     * @return
     */
    public DsfTransferLog performTransfer(GamePlatform gamePlatform, TransferType transferType, BigDecimal amount, String transactionId, String operator, String memberUser) {
        DsfTransferLog dsfTransferLog = new DsfTransferLog(transactionId);
        //判断平台订单号是否存在
        List<DsfTransferLog> transferLogs = dsfTransferLogMapper.select(dsfTransferLog);
        if (transferLogs.size() == 1) {
            TransferState transferState = transferLogs.get(0).getTransferState();
            dsfTransferLog =  new DsfTransferLog(transactionId = transactionId);
            dsfTransferLog.setState(TransferState.NotExist.code);
            return dsfTransferLog;
        }
        //第一步判断玩家是否存在第三方账号
        DsfMemberUser dsfMemberUser = userService.checkDsfMemberUserExists(memberUser, gamePlatform);
        if (transferLogs.size() == 0) {
            //数据库不存在订单号就直接去生成，标记为Init，此处直接insert
            dsfTransferLog = this.gengerateDsfTransactionLog(gamePlatform, dsfMemberUser, transferType, transactionId, amount, operator);
        }
        //去第三方转账
        dsfTransferLog = balanceService(gamePlatform).performTransfer(dsfMemberUser, dsfTransferLog, super.getSiteApi(GamePlatform.getGamePlatform(dsfTransferLog.getPlatformCode()), dsfTransferLog.getSiteCode()));
        if (dsfTransferLog.getTransferState().equals(TransferState.Successful) || dsfTransferLog.getTransferState().equals(TransferState.Failed)) {
            dsfTransferLogMapper.transferSuccessOrFailed(dsfTransferLog);
        }
        if (dsfTransferLog.getTransferState().equals(TransferState.UnKnow)) {
            //非成功非失败，标记为UnKnow ( 应该在底层直接返回值状态值) ,其他状态不管
            dsfTransferLogMapper.transferUnKnow(dsfTransferLog);
            try {
                Thread.sleep(500);
                this.checkTransferStatus(dsfTransferLog.getDsfTransactionId());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return dsfTransferLogMapper.selectOne(new DsfTransferLog(transactionId));
    }

    /**
     * 存在订单时的转账
     *
     * @param dsfTransferLog
     * @return
     */
    private DsfTransferLog performTransfer(DsfTransferLog dsfTransferLog) {
        List<DsfMemberUser> dsfMemberUsers = super.currentDsfMemberUser(dsfTransferLog.getMemberUser(), GamePlatform.getGamePlatform(dsfTransferLog.getPlatformCode()));
        dsfTransferLog = balanceService(GamePlatform.getGamePlatform(dsfTransferLog.getPlatformCode())).performTransfer(dsfMemberUsers.get(0), dsfTransferLog
                , super.getSiteApi(GamePlatform.getGamePlatform(dsfTransferLog.getPlatformCode()), dsfTransferLog.getSiteCode()));
        if (dsfTransferLog.getTransferState().equals(TransferState.UnKnow)) {
            dsfTransferLogMapper.transferUnKnow(dsfTransferLog);
        }
        if (dsfTransferLog.getTransferState().equals(TransferState.Successful) || dsfTransferLog.getTransferState().equals(TransferState.Failed)) {
            dsfTransferLogMapper.transferSuccessOrFailed(dsfTransferLog);
        }
        return dsfTransferLog;
    }

    /**
     * 检测转账状态
     *
     * @param transactionId
     * @return
     */
    public DsfTransferLog checkTransferStatus(String transactionId) {
        DsfTransferLog dsfTransferLog;
        List<DsfTransferLog> transferLogs = dsfTransferLogMapper.select(new DsfTransferLog(transactionId));
        try {
            dsfTransferLog = transferLogs.get(0);
        } catch (Exception e) {
            log.error("转账无该订单号 transactionId = {}", transactionId);
            dsfTransferLog = new DsfTransferLog(transactionId);
            dsfTransferLog.setState(TransferState.NotExist.code);
            return dsfTransferLog;
        }

        return this.switchTransferLog(dsfTransferLog);
    }

    private DsfTransferLog switchTransferLog(DsfTransferLog dsfTransferLog) {
        switch (dsfTransferLog.getTransferState()) {
            case Successful:
                return dsfTransferLog;
            case Failed:
                return dsfTransferLog;
            case UnKnow:
                //Unknow就去确认订单状态,具体在实现类中处理
                dsfTransferLog = balanceService(GamePlatform.getGamePlatform(dsfTransferLog.getPlatformCode())).checkTransferStatus(dsfTransferLog, super.getSiteApi(GamePlatform.getGamePlatform(dsfTransferLog.getPlatformCode()), dsfTransferLog.getSiteCode()));
                if (dsfTransferLog.getTransferState().equals(TransferState.Successful) || dsfTransferLog.getTransferState().equals(TransferState.Failed)) {
                    dsfTransferLogMapper.transferSuccessOrFailed(dsfTransferLog);
                }
                return dsfTransferLog;
            case Init:
                //初始化的订单就去转账
                return this.performTransfer(dsfTransferLog);
            default:
                throw new RRException("订单状态出错 TransferState =" + dsfTransferLog.getTransferState());
        }
    }

    /**
     * 生成第三方转账号和对象,直接生成为Init状态
     *
     * @param gamePlatform
     * @param dsfMemberUser
     * @param transferType
     * @param transactionId
     * @param amount
     * @return
     */
    private DsfTransferLog gengerateDsfTransactionLog(GamePlatform gamePlatform, DsfMemberUser dsfMemberUser, TransferType transferType, String transactionId, BigDecimal amount, String operator) {
        DsfGmApi api = super.getSiteApi(gamePlatform, dsfMemberUser.getSiteCode());
        String dsfTransactionId = balanceService(gamePlatform).gengerateDsfTransactionId(dsfMemberUser, transactionId, super.getSiteApi(gamePlatform, dsfMemberUser.getSiteCode()));
        DsfTransferLog dsfTransferLog = new DsfTransferLog(transactionId, dsfMemberUser.getMemberUser(),
                siteUtil.getSiteCode(), dsfMemberUser.getDsfPlayerId(), gamePlatform.platformCode,
                transferType.getType(), TransferState.Init.code, null,
                dsfTransactionId, amount, null, LocalDateTime.now(),
                null, 1, api.getApiName(), operator);
        int i = dsfTransferLogMapper.insert(dsfTransferLog);
        if (i != 1) {
            log.error(" 转账记录插入数据库失败 dsfTransferLog ={}", dsfTransferLog.toString());
            throw new RRException(" 转账记录插入数据库失败 dsfTransferLog =" + dsfTransferLog.toString());
        }
        return dsfTransferLog;
    }

}
