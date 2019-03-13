package com.xuyun.platform.moudles.fg.serviceImpl;


import com.alibaba.fastjson.JSONObject;
import com.xuyun.platform.dsfcenterdao.service.DsfSiteService;
import com.xuyun.platform.dsfcenterdao.utlis.IpUtils;
import com.xuyun.platform.dsfcenterdao.utlis.SiteUtil;
import com.xuyun.platform.dsfcenterdata.AbstractGameService;
import com.xuyun.platform.dsfcenterdata.entity.DsfGmApi;
import com.xuyun.platform.dsfcenterdata.entity.DsfGmGame;
import com.xuyun.platform.dsfcenterdata.entity.DsfMemberUser;
import com.xuyun.platform.dsfcenterdata.enums.ErrorCode;
import com.xuyun.platform.dsfcenterdata.response.RRException;
import com.xuyun.platform.modules.common.constants.FgConstants;
import com.xuyun.platform.modules.common.dto.fg.FgLaunchGameDto;
import com.xuyun.platform.modules.common.dto.fg.FgLaunchGameReponse;
import com.xuyun.platform.modules.common.dto.fg.FgResponse;
import com.xuyun.platform.modules.common.enums.FgErrorCode;
import com.xuyun.platform.moudles.fg.http.FgNetWork;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName: FgGameServiceImpl
 * @Author: R.M.I
 * @CreateTime: 2019年02月27日 14:49:00
 * @Description: TODO
 * @Version 1.0.0
 */
@Service("FgGameService")
@Slf4j
public class FgGameServiceImpl extends AbstractGameService {

    @Autowired
    FgNetWork fgNetWork;
    @Autowired
    DsfSiteService dsfSiteService ;
    @Autowired
    SiteUtil siteUtil;

    @Override
    public String launchGame(DsfMemberUser dsfMemberUser, DsfGmGame dsfGmGame, DsfGmApi dsfGmApi, String homeUrl) {
        return this.fglaunch(dsfMemberUser,dsfGmGame,dsfGmApi,homeUrl,"h5");
    }

    @Override
    public String launchMobileGame(DsfMemberUser dsfMemberUser, DsfGmGame dsfGmGame,DsfGmApi dsfGmApi, String homeUrl) throws RRException {
        return this.fglaunch(dsfMemberUser,dsfGmGame,dsfGmApi,homeUrl,"app");
    }

    private String fglaunch(DsfMemberUser dsfMemberUser, DsfGmGame dsfGmGame,DsfGmApi dsfGmApi, String homeUrl,String fgGameType){
        Integer siteId =dsfSiteService.getSchemaName(siteUtil.getSiteCode()).getId();
        FgLaunchGameDto launchGameDto = new FgLaunchGameDto(dsfMemberUser.getDsfPlayerId(),dsfGmGame.getGameCode(),fgGameType,
                FgConstants.ZH, IpUtils.getIp(),homeUrl,String.valueOf(siteId));
        FgResponse fgResponse =fgNetWork.post(dsfGmApi,launchGameDto,FgConstants.LAUNCH_GAME);
        if (fgResponse == null ){
            throw new RRException("FG游戏登录异常,第三方请求异常");
        }
        if(fgResponse.getCode() == FgConstants.FG_SUCCESS_CODE){
            FgLaunchGameReponse fgResponseDate= JSONObject.parseObject(JSONObject.toJSONString(fgResponse.getData()),FgLaunchGameReponse.class);
            return fgResponseDate.getGame_url()+"&token="+fgResponseDate.getToken();
        }
        log.error("FG 游戏登录异常,error: {} , 结果 : {}", FgErrorCode.getMsgByCode(fgResponse.getCode()),fgResponse.toString());
        throw new RRException(ErrorCode.DSF_GAME_PLATFORM_REQUEST_ERROR_FG);
    }
}
