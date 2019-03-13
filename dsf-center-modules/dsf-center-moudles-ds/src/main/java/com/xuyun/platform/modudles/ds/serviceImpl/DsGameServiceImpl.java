package com.xuyun.platform.modudles.ds.serviceImpl;

import com.alibaba.fastjson.JSONObject;
import com.xuyun.platform.dsfcenterdata.AbstractGameService;
import com.xuyun.platform.dsfcenterdata.entity.DsfGmApi;
import com.xuyun.platform.dsfcenterdata.entity.DsfGmGame;
import com.xuyun.platform.dsfcenterdata.entity.DsfMemberUser;
import com.xuyun.platform.dsfcenterdata.response.RRException;
import com.xuyun.platform.dsfcenterdata.utils.MD5;
import com.xuyun.platform.modudles.ds.http.DsNetWork;
import com.xuyun.platform.modules.common.constants.DsConstants;
import com.xuyun.platform.modules.common.dto.ds.request.DsLoginDto;
import com.xuyun.platform.modules.common.dto.ds.request.DsRequest;
import com.xuyun.platform.modules.common.dto.ds.response.DsLoginResponse;
import com.xuyun.platform.modules.common.dto.ds.response.DsResponse;
import com.xuyun.platform.modules.common.enums.DsErroeCodes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName: DsGameServiceImpl
 * @Author: R.M.I
 * @CreateTime: 2019年02月27日 14:49:00
 * @Description: TODO
 * @Version 1.0.0
 */
@Service("DsGameService")
@Slf4j
public class DsGameServiceImpl extends AbstractGameService {

    @Autowired
    DsNetWork dsNetWork;

    @Override
    public String launchGame(DsfMemberUser dsfMemberUser, DsfGmGame dsfGmGame, DsfGmApi dsfGmApi, String homeUrl) {
        DsLoginDto dsLoginDto = new DsLoginDto();
        //此处Ds要求密码为32位md5
        dsLoginDto.setPassword(MD5.getMD5(dsfMemberUser.getPassword()));
        dsLoginDto.setUsername(dsfMemberUser.getDsfPlayerId());
        dsLoginDto.setLine(DsLoginDto.generateDsLoginLine());
        dsLoginDto.setHomeUrl(homeUrl);
        dsLoginDto.setCurrency(dsfGmApi.getMemo());
        dsLoginDto.setNickname(dsfMemberUser.getDsfPlayerId());
        DsRequest dsRequest = new DsRequest<>(dsLoginDto, DsConstants.COMMAND_LOGIN);
        dsRequest.setHashCode(DsRequest.getHashCode(dsfGmApi));
        DsResponse dsResponse =dsNetWork.post(dsfGmApi,dsRequest);
        if(dsResponse.getErrorCode() == DsErroeCodes.Success){
            DsLoginResponse link = JSONObject.parseObject(JSONObject.toJSONString(dsResponse.getParams()), DsLoginResponse.class);
            return link.getLink();
        }
        if(dsResponse.getErrorCode() == DsErroeCodes.RequestError){
            try {
                Thread.sleep(2000);
              return   this.launchGame(dsfMemberUser,dsfGmGame,dsfGmApi,homeUrl);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        throw new RRException("DS游戏登录错误 dsResponse ="+dsResponse.toString());
    }

    @Override
    public String launchMobileGame(DsfMemberUser dsfMemberUser, DsfGmGame dsfGmGame,DsfGmApi dsfGmApi, String homeUrl) throws RRException{
        return this.launchGame(dsfMemberUser,dsfGmGame,dsfGmApi,homeUrl);
    }
}
