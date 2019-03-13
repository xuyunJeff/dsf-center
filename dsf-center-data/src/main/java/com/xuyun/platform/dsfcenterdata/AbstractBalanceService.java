package com.xuyun.platform.dsfcenterdata;

import com.xuyun.platform.dsfcenterdata.entity.DsfGmApi;
import com.xuyun.platform.dsfcenterdata.entity.DsfMemberUser;
import com.xuyun.platform.dsfcenterdata.entity.DsfTransferLog;

/**
 * @ClassName: AbstractBalanceService
 * @Author: R.M.I
 * @CreateTime: 2019年02月22日 21:52:00
 * @Description: TODO
 * @Version 1.0.0
 */
abstract public class AbstractBalanceService {

    /**
     * 资金转账(转入或转出)，不成功全部标记为UnKnow
     */
    abstract public DsfTransferLog performTransfer(DsfMemberUser dsfMemberUser, DsfTransferLog dsfTransferLog, DsfGmApi dsfGmApi);

    /**
     * 查询交易状态
     * @param dsfTransferLog 订单
     * @return DsfTransferLog 订单
     */
    abstract public DsfTransferLog checkTransferStatus(DsfTransferLog dsfTransferLog,DsfGmApi dsfGmApi);

    /**
     * 生成第三方转账id
     * @param dsfMemberUser
     * @return
     */
    abstract public String gengerateDsfTransactionId( DsfMemberUser dsfMemberUser,String transactionId,DsfGmApi dsfGmApi);

}
