package com.invech.platform.modules.ky.serviceImpl;


import com.alibaba.fastjson.JSONObject;
import com.invech.platform.dsfcenterdao.service.DsfSiteService;
import com.invech.platform.dsfcenterdao.utlis.IpUtils;
import com.invech.platform.dsfcenterdao.utlis.SiteUtil;
import com.invech.platform.dsfcenterdata.AbstractGameService;
import com.invech.platform.dsfcenterdata.entity.DsfGmApi;
import com.invech.platform.dsfcenterdata.entity.DsfGmGame;
import com.invech.platform.dsfcenterdata.entity.DsfMemberUser;
import com.invech.platform.dsfcenterdata.response.RRException;
import com.invech.platform.modules.common.constants.KyConstans;
import com.invech.platform.modules.common.dto.ky.KyLauchGameReponse;
import com.invech.platform.modules.common.dto.ky.KyLauchGameRequest;
import com.invech.platform.modules.common.dto.ky.KyReponse;
import com.invech.platform.modules.common.enums.KyErrorCode;
import com.invech.platform.modules.ky.http.KyNetWork;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName: KyGameServiceImpl
 * @Author: R.M.I
 * @CreateTime: 2019年02月27日 14:49:00
 * @Description: TODO
 * @Version 1.0.0
 */
@Service("KyGameService")
@Slf4j
public class KyGameServiceImpl extends AbstractGameService {

    @Autowired
    KyNetWork kyNetWork;

    @Autowired
    DsfSiteService dsfSiteService ;

    @Autowired
    SiteUtil siteUtil;

    @Override
    public String launchGame(DsfMemberUser dsfMemberUser, DsfGmGame dsfGmGame, DsfGmApi dsfGmApi, String homeUrl) {
        try {
            KyLauchGameRequest kyLauchGameRequest = new KyLauchGameRequest(KyConstans.LAUCH_GAME,dsfGmApi,dsfMemberUser.getDsfPlayerId(),"127.0.0.1",siteUtil.getSiteCode(),dsfGmGame.getGameCode());
            KyReponse<KyLauchGameReponse> reponse =kyNetWork.get(dsfGmApi,kyLauchGameRequest);
            KyLauchGameReponse lauchGameReponse =JSONObject.parseObject(JSONObject.toJSONString(reponse.getD()),KyLauchGameReponse.class);
            if(lauchGameReponse.getCode().equals(KyErrorCode.Success.getCode())){
                return  lauchGameReponse.getUrl();
            }
            log.error( "Ky 开始游戏请求异常" +KyErrorCode.kyError(lauchGameReponse.getCode()).getMsg());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String launchMobileGame(DsfMemberUser dsfMemberUser, DsfGmGame dsfGmGame,DsfGmApi dsfGmApi, String homeUrl) throws RRException {
        return this.launchGame(dsfMemberUser,dsfGmGame,dsfGmApi,homeUrl);
    }
}
