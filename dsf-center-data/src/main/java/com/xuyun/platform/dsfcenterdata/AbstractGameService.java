package com.xuyun.platform.dsfcenterdata;

import com.xuyun.platform.dsfcenterdata.entity.DsfGmApi;
import com.xuyun.platform.dsfcenterdata.entity.DsfGmGame;
import com.xuyun.platform.dsfcenterdata.entity.DsfMemberUser;
import com.xuyun.platform.dsfcenterdata.enums.GamePlatform;
import com.xuyun.platform.dsfcenterdata.response.RRException;

/**
 * @ClassName: AbstractGameService
 * @Author: R.M.I
 * @CreateTime: 2019年02月22日 22:01:00
 * @Description: TODO
 * @Version 1.0.0
 */
abstract public class AbstractGameService {

    /**
     * 启动游戏
     * @param dsfMemberUser 玩家信息
     * @return 启动的游戏地址
     */
    abstract public String  launchGame(DsfMemberUser dsfMemberUser, DsfGmGame dsfGmGame, DsfGmApi dsfGmApi, String homeUrl);


    public String launchMobileGame(DsfMemberUser dsfMemberUser, DsfGmGame dsfGmGame,DsfGmApi dsfGmApi, String homeUrl) throws RRException{
        throw new RRException("该游戏平台不支持");
    }

    /**
     * 启动试玩游戏
     */
    public String  launchTryingGame(DsfMemberUser dsfMemberUser, DsfGmGame dsfGmGame ,DsfGmApi dsfGmApi, String homeUrl){
        throw new RRException("该游戏平台不支持试玩");
    }

    public String launchMobileTryingGame(DsfMemberUser dsfMemberUser, DsfGmGame dsfGmGame ,DsfGmApi dsfGmApi, String homeUrl) {
        throw new RRException("该游戏平台不支持试玩");
    }

    /**
     * 获得游戏列表, 包括菜单
     * 当gameStatus 为null时 查询所有游戏
     */
    public DsfGmGame gameList(String siteCode , GamePlatform gamePlatform,DsfGmApi dsfGmApi) {
        throw new RRException("该游戏平台暂不支持获得游戏列表");
    }
}
