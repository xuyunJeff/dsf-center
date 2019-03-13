package com.invech.platform.modudles.ds.serviceImpl;

import com.alibaba.fastjson.JSONObject;
import com.invech.platform.dsfcenterdata.AbstractPlayerService;
import com.invech.platform.dsfcenterdata.entity.DsfGmApi;
import com.invech.platform.dsfcenterdata.entity.DsfMemberUser;
import com.invech.platform.dsfcenterdata.response.RRException;
import com.invech.platform.dsfcenterdata.utils.GenerationSequenceUtil;
import com.invech.platform.dsfcenterdata.utils.MD5;
import com.invech.platform.modudles.ds.http.DsNetWork;
import com.invech.platform.modules.common.constants.DsConstants;
import com.invech.platform.modules.common.dto.ds.request.DsBalanceRequest;
import com.invech.platform.modules.common.dto.ds.request.DsLoginDto;
import com.invech.platform.modules.common.dto.ds.request.DsRequest;
import com.invech.platform.modules.common.dto.ds.response.DsBalanceResponse;
import com.invech.platform.modules.common.dto.ds.response.DsResponse;
import com.invech.platform.modules.common.enums.DsErroeCodes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * @ClassName: DsPlayerServiceImpl
 * @Author: R.M.I
 * @CreateTime: 2019年02月27日 14:49:00
 * @Description: TODO
 * @Version 1.0.0
 */
@Service("DsPlayerService")
@Slf4j
public class DsPlayerServiceImpl extends AbstractPlayerService {

    @Autowired
    DsNetWork dsNetWork;
    /**
     * Ds 平台无注册,此处为登录接口(新用户默认为注册)
     * @param dsfMemberUser
     * @param dsfGmApi
     * @return
     */
    @Override
    public DsfMemberUser registerPlayer(DsfMemberUser dsfMemberUser, DsfGmApi dsfGmApi) {
        DsLoginDto dsLoginDto = new DsLoginDto();
        //此处Ds要求密码为32位md5
        dsLoginDto.setPassword(MD5.getMD5(dsfMemberUser.getPassword()));
        dsLoginDto.setUsername(dsfMemberUser.getDsfPlayerId());
        dsLoginDto.setLine(DsLoginDto.generateDsLoginLine());
        dsLoginDto.setHomeUrl("www.qidian.com");
        dsLoginDto.setCurrency(dsfGmApi.getMemo());
        dsLoginDto.setNickname(dsfMemberUser.getDsfPlayerId());
        DsRequest dsRequest = new DsRequest<>(dsLoginDto, DsConstants.COMMAND_LOGIN);
        dsRequest.setHashCode(DsRequest.getHashCode(dsfGmApi));
        DsResponse dsResponse =dsNetWork.post(dsfGmApi,dsRequest);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if(dsResponse.getErrorCode() == DsErroeCodes.Success){
            log.info(dsResponse.toString());
            return dsfMemberUser;
        }
        throw new RRException("DS游戏登录错误(创建玩家) dsResponse ="+dsResponse.toString());
    }



    public String generatePassword(){
        return GenerationSequenceUtil.generateRandomNum(8);
    }
    @Override
    public BigDecimal playerBalance(DsfMemberUser dsfMemberUser, DsfGmApi dsfGmApi) {
        DsBalanceRequest dsBalanceRequest =new DsBalanceRequest(dsfMemberUser.getDsfPlayerId(),MD5.getMD5(dsfMemberUser.getPassword()));
        DsRequest dsRequest = new DsRequest<>(dsBalanceRequest, DsConstants.COMMAND_GET_BALANCE);
        dsRequest.setHashCode(DsRequest.getHashCode(dsfGmApi));
        DsResponse dsResponse =dsNetWork.post(dsfGmApi,dsRequest);
        if(dsResponse.getErrorCode() == DsErroeCodes.Success){
            log.info(dsResponse.toString());
            DsBalanceResponse params =JSONObject.parseObject(JSONObject.toJSONString(dsResponse.getParams()),DsBalanceResponse.class);
            return params.getBalance();
        }
        throw new RRException("Ds余额获取失败",2000);
    }

    @Override
    public boolean freezePlayer(DsfMemberUser dsfMemberUser, DsfGmApi dsfGmApi) {
        return false;
    }

    /**
     * 所有生成的第三方账号名必须以 siteCode + apiPrefix为前缀,
     * 涉及拉单,不可错
     * @param dsfGmApi
     * @return
     */
    @Override
    public String generateLoginName(DsfGmApi dsfGmApi) {
        int dsfPlayerIdLength = 20 ;
        String siteCode = dsfGmApi.getSiteCode();
        String apiPrefix = dsfGmApi.getPrefix();
        String generateRandomNum = GenerationSequenceUtil.generateRandomNum(dsfPlayerIdLength - apiPrefix.length()-siteCode.length());
        return siteCode+apiPrefix+generateRandomNum;
    }
}
