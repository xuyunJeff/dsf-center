package com.xuyun.platform.dsfcenterservice.service;

import com.xuyun.platform.dsfcenterdao.config.SpringContextUtil;
import com.xuyun.platform.dsfcenterdao.service.DsfSiteService;
import com.xuyun.platform.dsfcenterdao.utlis.SiteUtil;
import com.xuyun.platform.dsfcenterdata.AbstractBalanceService;
import com.xuyun.platform.dsfcenterdata.AbstractGameService;
import com.xuyun.platform.dsfcenterdata.AbstractPlayerService;
import com.xuyun.platform.dsfcenterdata.entity.DsfGmApi;
import com.xuyun.platform.dsfcenterdata.entity.DsfMemberUser;
import com.xuyun.platform.dsfcenterdata.enums.GamePlatform;
import com.xuyun.platform.dsfcenterdata.response.RRException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName: BaseService
 * @Author: R.M.I
 * @CreateTime: 2019年02月23日 21:46:00
 * @Description: TODO
 * @Version 1.0.0
 */
@Slf4j
public class BaseService {

    @Autowired
    DsfSiteService dsfSiteService;

    @Autowired
    UserService userService;

    @Autowired
    public SiteUtil siteUtil;

    AbstractPlayerService playerService(GamePlatform gamePlatform){
        AbstractPlayerService playerService ;
        switch (gamePlatform){
            case Agin:
                playerService = SpringContextUtil.getBean("AgPlayerService");
                break;
            case Ds:
                playerService = SpringContextUtil.getBean("DsPlayerService");
                break;
            case Fg:
                playerService = SpringContextUtil.getBean("FgPlayerService");
                break;
            case Ky:
                playerService = SpringContextUtil.getBean("KyPlayerService");
                break;
            default: playerService =null;
        }
        return playerService;

    }

    AbstractGameService gameService(GamePlatform gamePlatform){
        AbstractGameService gameService ;
        switch (gamePlatform){
            case Agin:
                gameService = SpringContextUtil.getBean("AgGameService");
                break;
            case Ds:
                gameService = SpringContextUtil.getBean("DsGameService");
                break;
            case Fg:
                gameService = SpringContextUtil.getBean("FgGameService");
                break;
            case Ky:
                gameService = SpringContextUtil.getBean("KyGameService");
                break;
            default: gameService =null;
        }
        return gameService;
    }

    AbstractBalanceService balanceService(GamePlatform gamePlatform) {
        AbstractBalanceService balanceService ;
        switch (gamePlatform) {
            case Agin:
                balanceService = SpringContextUtil.getBean("AgBalanceService");
                break;
            case Ds:
                balanceService = SpringContextUtil.getBean("DsBalanceService");
                break;
            case Fg:
                balanceService = SpringContextUtil.getBean("FgBalanceService");
                break;
            case Ky:
                balanceService = SpringContextUtil.getBean("KyBalanceService");
                break;
            default:
                balanceService = null;
        }
        return balanceService;
    }

    /**
     * 获取当前用户的，确定了第三方游戏的，api线路名
     * @param gamePlatform
     * @param siteCode
     * @return
     */
    public DsfGmApi getSiteApi(GamePlatform gamePlatform,String siteCode){
         List<DsfGmApi> apis=dsfSiteService.queryApisBySiteCodePlatform(siteCode);
         apis = apis.stream().filter(it-> it.getPlatformCode().equals(gamePlatform.platformCode)).collect(Collectors.toList());
         if( 0 == apis.size()){
             log.error("该站点为配置平台 gamePlatform ={} ， siteCode ={}",gamePlatform,siteCode);
             throw new RRException("该站点未配置平台 gamePlatform ="+gamePlatform+" ， siteCode ="+siteCode);
         }
         return apis.get(0);
    }

    /**
     * 获取当前玩家信息
     * @param memberUser
     * @return
     */
    public List<DsfMemberUser> currentDsfMemberUser(String memberUser){
        return userService.userDetails(memberUser);
    }

    /**
     * 获取当前玩家信息
     * @param memberUser
     * @param gamePlatform
     * @return
     */
    public List<DsfMemberUser> currentDsfMemberUser(String memberUser ,GamePlatform gamePlatform){
        List<DsfMemberUser> dsfMemberUsers=currentDsfMemberUser(memberUser);
        return dsfMemberUsers.stream().filter(it -> it.getPlatformCode().equals(gamePlatform.platformCode)).collect(Collectors.toList());
    }

}
