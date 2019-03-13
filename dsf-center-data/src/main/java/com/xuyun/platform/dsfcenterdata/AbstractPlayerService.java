package com.xuyun.platform.dsfcenterdata;

import com.xuyun.platform.dsfcenterdata.entity.DsfGmApi;
import com.xuyun.platform.dsfcenterdata.entity.DsfMemberUser;
import com.xuyun.platform.dsfcenterdata.utils.GenerationSequenceUtil;

import java.math.BigDecimal;

/**
 * @ClassName: AbstractPlayerService
 * @Author: R.M.I
 * @CreateTime: 2019年02月22日 21:40:00
 * @Description: TODO
 * @Version 1.0.0
 */
public abstract class AbstractPlayerService {


    /**
     * 创建帐号
     */
    public abstract DsfMemberUser registerPlayer(DsfMemberUser dsfMemberUser, DsfGmApi dsfGmApi);

    /**
     * 查询玩家余额
     *
     * @param dsfMemberUser 玩家信息
     * @return 余额
     */
    public abstract BigDecimal playerBalance(DsfMemberUser dsfMemberUser, DsfGmApi dsfGmApi);

    /**
     * 冻结玩家 true -> 冻结 false -> 解冻
     * 冻结失败是返回FreezePlayerExcetion 异常
     *
     * @param dsfMemberUser
     */
    public abstract boolean freezePlayer(DsfMemberUser dsfMemberUser, DsfGmApi dsfGmApi);

    /**
     * 所有生成的第三方账号名必须以 siteCode + apiPrefix为前缀,
     * 涉及拉单,不可错
     *
     * @param dsfGmApi
     * @return
     */
    public String generateLoginName(DsfGmApi dsfGmApi) {
        int dsfPlayerIdLength = 20;
        String siteCode = dsfGmApi.getSiteCode();
        String apiPrefix = dsfGmApi.getPrefix();
        String generateRandomNum = GenerationSequenceUtil.generateRandomNum(dsfPlayerIdLength - apiPrefix.length() - siteCode.length());
        return siteCode + apiPrefix + generateRandomNum;

    }

    public String generatePassword() {
        return "";
    }

}
