package com.xuyun.platform.moudles.fg.serviceImpl;

import com.alibaba.fastjson.JSONObject;
import com.xuyun.platform.dsfcenterdata.AbstractPlayerService;
import com.xuyun.platform.dsfcenterdata.entity.DsfGmApi;
import com.xuyun.platform.dsfcenterdata.entity.DsfMemberUser;
import com.xuyun.platform.dsfcenterdata.enums.ErrorCode;
import com.xuyun.platform.dsfcenterdata.response.RRException;
import com.xuyun.platform.dsfcenterdata.utils.GenerationSequenceUtil;
import com.xuyun.platform.modules.common.constants.FgConstants;
import com.xuyun.platform.modules.common.dto.fg.FgBalanceResponse;
import com.xuyun.platform.modules.common.dto.fg.FgRegisterPlayerDto;
import com.xuyun.platform.modules.common.dto.fg.FgResponse;
import com.xuyun.platform.modules.common.enums.FgErrorCode;
import com.xuyun.platform.moudles.fg.http.FgNetWork;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * @ClassName: FgPlayerServiceImpl
 * @Author: R.M.I
 * @CreateTime: 2019年02月27日 14:49:00
 * @Description: TODO
 * @Version 1.0.0
 */
@Service("FgPlayerService")
@Slf4j
public class FgPlayerServiceImpl extends AbstractPlayerService {

    @Autowired
    FgNetWork netWork;
    /**
     * Ds 平台无注册,此处为登录接口(新用户默认为注册)
     * @param dsfMemberUser
     * @param dsfGmApi
     * @return
     */
    @Override
    public DsfMemberUser registerPlayer(DsfMemberUser dsfMemberUser, DsfGmApi dsfGmApi) {
        FgRegisterPlayerDto player = new FgRegisterPlayerDto(dsfMemberUser.getDsfPlayerId(),dsfMemberUser.getPassword());
        FgResponse fgResponse =netWork.post(dsfGmApi,player, FgConstants.REGISTER);
        if (fgResponse == null ){
            throw new RRException("FG注册用户失败,第三方请求异常");
        }
        if(fgResponse.getCode() == FgConstants.FG_SUCCESS_CODE){
            return dsfMemberUser;
        }
        log.error("FG 创建玩家异常,error: {} , 结果 : {}", FgErrorCode.getMsgByCode(fgResponse.getCode()),fgResponse.toString());
        throw new RRException(ErrorCode.DSF_GAME_PLATFORM_REQUEST_ERROR_FG);
    }



    public String generatePassword(){
        return GenerationSequenceUtil.generateRandomNum(8);
    }
    @Override
    public BigDecimal playerBalance(DsfMemberUser dsfMemberUser, DsfGmApi dsfGmApi) {
        FgResponse fgResponse =netWork.post(dsfGmApi,null,FgConstants.BALANCE +dsfMemberUser.getDsfPlayerId());
        if (fgResponse == null ){
            throw new RRException("FG余额获取失败,第三方请求异常");
        }
        if(fgResponse.getCode().equals(FgConstants.FG_SUCCESS_CODE)){
            return JSONObject.parseObject(JSONObject.toJSONString(fgResponse.getData()),FgBalanceResponse.class).getBalance().divide(BigDecimal.valueOf(100));
        }
        log.error("FG 创建玩家异常,error: {} , 结果 : {}", FgErrorCode.getMsgByCode(fgResponse.getCode()),fgResponse.toString());
        throw new RRException(ErrorCode.DSF_GAME_PLATFORM_REQUEST_ERROR_FG);
    }

    @Override
    public boolean freezePlayer(DsfMemberUser dsfMemberUser, DsfGmApi dsfGmApi) {
        return false;
    }

    @Override
    public String generateLoginName(DsfGmApi dsfGmApi){
        return super.generateLoginName(dsfGmApi);
    }

}
