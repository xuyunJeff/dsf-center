package com.invech.platform.modules.ky.serviceImpl;

import com.alibaba.fastjson.JSONObject;
import com.invech.platform.dsfcenterdao.utlis.IpUtils;
import com.invech.platform.dsfcenterdao.utlis.SiteUtil;
import com.invech.platform.dsfcenterdata.AbstractPlayerService;
import com.invech.platform.dsfcenterdata.entity.DsfGmApi;
import com.invech.platform.dsfcenterdata.entity.DsfMemberUser;
import com.invech.platform.dsfcenterdata.response.RRException;
import com.invech.platform.dsfcenterdata.utils.GenerationSequenceUtil;
import com.invech.platform.modules.common.constants.KyConstans;
import com.invech.platform.modules.common.dto.ky.*;
import com.invech.platform.modules.common.enums.KyErrorCode;
import com.invech.platform.modules.ky.http.KyNetWork;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * @ClassName: KyPlayerServiceImpl
 * @Author: R.M.I
 * @CreateTime: 2019年02月27日 14:49:00
 * @Description: TODO
 * @Version 1.0.0
 */
@Service("KyPlayerService")
@Slf4j
public class KyPlayerServiceImpl extends AbstractPlayerService {

    @Autowired
    KyNetWork netWork;

    @Autowired
    SiteUtil siteUtil;

    /**
     * Ds 平台无注册,此处为登录接口(新用户默认为注册)
     *
     * @param dsfMemberUser
     * @param dsfGmApi
     * @return
     */
    @Override
    public DsfMemberUser registerPlayer(DsfMemberUser dsfMemberUser, DsfGmApi dsfGmApi) {

        KyLauchGameRequest kyLauchGameRequest = new KyLauchGameRequest(KyConstans.LAUCH_GAME, dsfGmApi, dsfMemberUser.getDsfPlayerId(), "127.0.0.1", siteUtil.getSiteCode(), "0");
        KyReponse<KyLauchGameReponse> reponse = netWork.get(dsfGmApi, kyLauchGameRequest);
        if(reponse == null ){
            throw new RRException("Ky棋牌接口调用异常 -- 开始游戏");
        }
        KyLauchGameReponse kyLauchGameReponse = JSONObject.parseObject(JSONObject.toJSONString(reponse.getD()),KyLauchGameReponse.class);
        if (kyLauchGameReponse.getCode().equals(KyErrorCode.Success.getCode())) {
            return dsfMemberUser;
        }
        log.error("Ky 开始游戏请求异常" + KyErrorCode.kyError(kyLauchGameReponse.getCode()).getMsg());
        throw new RRException(KyErrorCode.kyError(kyLauchGameReponse.getCode()).getMsg());
    }


    public String generatePassword() {
        return GenerationSequenceUtil.generateRandomNum(8);
    }

    @Override
    public BigDecimal playerBalance(DsfMemberUser dsfMemberUser, DsfGmApi dsfGmApi) {
        KyBalanceRequest request=new KyBalanceRequest(KyConstans.CHECK_BALANCE,dsfGmApi,dsfMemberUser.getDsfPlayerId());
        KyReponse<KyBalanceReponse> reponse = netWork.get(dsfGmApi, request);
        if(reponse == null ){
            throw new RRException("Ky余额获取失败-- checkBalance");
        }
        KyBalanceReponse balanceReponse = JSONObject.parseObject(JSONObject.toJSONString(reponse.getD()),KyBalanceReponse.class);

        if (balanceReponse.getCode().equals(KyErrorCode.Success.getCode())) {
            return balanceReponse.getTotalMoney();
        }
        log.error( "Ky 请求余额 请求异常 " +KyErrorCode.kyError(balanceReponse.getCode()).getMsg());
        throw new RRException("Ky 请求余额 请求异常 " +KyErrorCode.kyError(balanceReponse.getCode()).getMsg());
    }

    @Override
    public boolean freezePlayer(DsfMemberUser dsfMemberUser, DsfGmApi dsfGmApi) {
        return false;
    }

    @Override
    public String generateLoginName(DsfGmApi dsfGmApi) {
        return super.generateLoginName(dsfGmApi);
    }

}
