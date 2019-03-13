package com.invech.platform.dsfcenterservice.controller;

import com.invech.platform.dsfcenterdao.utlis.SiteUtil;
import com.invech.platform.dsfcenterdata.constants.ApiConstants;
import com.invech.platform.dsfcenterdata.entity.DsfGmApi;
import com.invech.platform.dsfcenterdata.entity.DsfMemberUser;
import com.invech.platform.dsfcenterdata.enums.Available;
import com.invech.platform.dsfcenterdata.enums.ErrorCode;
import com.invech.platform.dsfcenterdata.enums.GamePlatform;
import com.invech.platform.dsfcenterdata.enums.PlatformType;
import com.invech.platform.dsfcenterdata.response.RRException;
import com.invech.platform.dsfcenterdata.utils.DateUtil;
import com.invech.platform.dsfcenterdata.utils.ObjectUtils;
import com.invech.platform.dsfcenterdata.utils.StringUtils;
import com.invech.platform.dsfcenterservice.service.BaseService;
import com.invech.platform.dsfcenterservice.service.UserService;

import javax.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Controller
@Slf4j
public abstract class BaseController {

    @Autowired
    UserService userService;

    @Autowired
    public SiteUtil siteUtil;

    @Autowired
    public RedisTemplate redisTemplate;


    public String getMemberUser() throws RRException {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes()).getRequest();
        String memberUser = request.getHeader(ApiConstants.PLAYER_LOGGIN_KEY);
        if(StringUtils.isEmpty(memberUser)){
            throw new RRException("未获取玩家信息");
        }
        log.info("玩家平台 ID = {}", memberUser);
        return memberUser;
    }

    public boolean checkMemberAvailable(GamePlatform gamePlatform) {
        List<DsfMemberUser> dsfMemberUsers = userService.currentDsfMemberUser(getMemberUser(), gamePlatform);
        if (dsfMemberUsers.size() == 0) {
            return true;
        }
        return dsfMemberUsers.get(0).getAvailable().equals(Available.Available.code);
    }

    public boolean checkGamePlatformApiAvailable(GamePlatform gamePlatform) {
        DsfGmApi dsfGmApi = userService.getSiteApi(gamePlatform, siteUtil.getSiteCode());
        return dsfGmApi.getAvailable() == 1 && dsfGmApi.getSiteApiAvailable().equals(Available.Available.code);
    }

    public boolean checkRequestLimit(GamePlatform gamePlatform,String type) {
        String key = ApiConstants.GAME_REQUEST_LIMIT+":"+gamePlatform.platformCode+":"+siteUtil.getSiteCode()+":"+type+":"+getMemberUser();
        Object value =redisTemplate.opsForValue().get(key);
        if(ObjectUtils.isEmpty(value)){
            redisTemplate.opsForValue().set(key, DateUtil.currentTimestamp(),3, TimeUnit.SECONDS );
            return true;
        }
        Long timestamp = Long.valueOf(value.toString())+3000;
        try {
            Thread.sleep(timestamp-DateUtil.currentTimestamp());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean checkBetlogLimit(PlatformType platformType, String site) {
        String key = ApiConstants.GAME_REQUEST_LIMIT+":"+platformType+":"+site;
        Object value =redisTemplate.opsForValue().get(key);
        if(ObjectUtils.isEmpty(value)){
            redisTemplate.opsForValue().set(key, DateUtil.currentTimestamp(),10, TimeUnit.SECONDS );
            return true;
        }
        throw new RRException(ErrorCode.DSF_BET_LOG_REQUEST_LIMIT);
    }

}
