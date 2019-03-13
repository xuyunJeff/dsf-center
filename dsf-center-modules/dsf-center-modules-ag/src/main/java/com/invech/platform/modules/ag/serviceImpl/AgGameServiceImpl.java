package com.invech.platform.modules.ag.serviceImpl;

import com.invech.platform.dsfcenterdao.utlis.SiteUtil;
import com.invech.platform.dsfcenterdata.AbstractGameService;
import com.invech.platform.dsfcenterdata.entity.DsfGmApi;
import com.invech.platform.dsfcenterdata.entity.DsfGmGame;
import com.invech.platform.dsfcenterdata.entity.DsfMemberUser;
import com.invech.platform.dsfcenterdata.response.RRException;
import com.invech.platform.modules.ag.http.AgNetWork;
import com.invech.platform.modules.common.constants.AgConstants;
import com.invech.platform.modules.common.dto.ag.AgDataDto;
import org.springframework.stereotype.Service;

/**
 * Ag开始游戏使用gci开头的域名
 *
 * @ClassName: AgGameServiceImpl
 * @Author: R.M.I
 * @CreateTime: 2019年02月23日 10:48:00
 * @Description: TODO
 * @Version 1.0.0
 */
@Service("AgGameService")
public class AgGameServiceImpl extends AbstractGameService {
    @Override
    public String launchGame(DsfMemberUser dsfMemberUser, DsfGmGame dsfGmGame, DsfGmApi dsfGmApi, String homeUrl) {
        return gengerteUrl(dsfMemberUser,dsfGmGame,dsfGmApi,false,homeUrl);
    }

    @Override
    public String launchMobileGame(DsfMemberUser dsfMemberUser, DsfGmGame dsfGmGame, DsfGmApi dsfGmApi, String homeUrl) throws RRException {
        return gengerteUrl(dsfMemberUser,dsfGmGame,dsfGmApi,true,homeUrl);
    }

    @Override
    public String launchTryingGame(DsfMemberUser dsfMemberUser, DsfGmGame dsfGmGame, DsfGmApi dsfGmApi, String homeUrl) {
        return gengerteUrlTrying(dsfMemberUser,dsfGmGame,dsfGmApi,false,homeUrl);
    }

    @Override
    public String launchMobileTryingGame(DsfMemberUser dsfMemberUser, DsfGmGame dsfGmGame, DsfGmApi dsfGmApi, String homeUrl) {
        return gengerteUrlTrying(dsfMemberUser,dsfGmGame,dsfGmApi,true,homeUrl);
    }

    private String gengerteUrlTrying(DsfMemberUser dsfMemberUser, DsfGmGame dsfGmGame, DsfGmApi dsfGmApi, boolean isH5 , String homeUrl){
        AgDataDto agDataDto =  new AgDataDto();
        agDataDto.setLoginname(dsfMemberUser.getDsfPlayerId());
        agDataDto.setPassword(dsfMemberUser.getPassword());
        agDataDto.setCur(AgConstants.CurTpye.curCny);
        agDataDto.setActype(AgConstants.Actype.testAccount);
        agDataDto.setOddtype(AgConstants.OddTpye.OddA);
        agDataDto.setCagent(dsfGmApi.getAgyAcc());
        agDataDto.setSid(SiteUtil.genRandomNum(13, 16));
        agDataDto.setLang(AgConstants.LangCode.cn_code);
        agDataDto.setGameType(dsfGmGame.getPcGameCode());
        agDataDto.setDm(homeUrl);
        if(isH5){
            agDataDto.setMh5(AgDataDto.Mh5.isMobile);
        }
        StringBuffer buffer = new StringBuffer();
        buffer.append(dsfGmApi.getPcUrl()).append(AgConstants.AGIN_M_FORWARDGAME);
        buffer.append(AgNetWork.getParams(DsfGmApi.getSecureCodeMap(dsfGmApi.getSecureCode()).get(AgConstants.AGIN_DESCODE_KEY).toString(), dsfGmApi.getMd5Key(), agDataDto));
        return buffer.toString();
    }

    private String gengerteUrl(DsfMemberUser dsfMemberUser, DsfGmGame dsfGmGame, DsfGmApi dsfGmApi, boolean isH5 , String homeUrl){
        AgDataDto agDataDto = new AgDataDto().getAgDataDto(dsfMemberUser,dsfGmApi);
        agDataDto.setSid(SiteUtil.genRandomNum(13, 16));
        agDataDto.setLang(AgConstants.LangCode.cn_code);
        agDataDto.setGameType(dsfGmGame.getPcGameCode());
        agDataDto.setDm(homeUrl);
        if(isH5){
            agDataDto.setMh5(AgDataDto.Mh5.isMobile);
        }
        StringBuffer buffer = new StringBuffer();
        buffer.append(dsfGmApi.getPcUrl()).append(AgConstants.AGIN_M_FORWARDGAME);
        buffer.append(AgNetWork.getParams(DsfGmApi.getSecureCodeMap(dsfGmApi.getSecureCode()).get(AgConstants.AGIN_DESCODE_KEY).toString(), dsfGmApi.getMd5Key(), agDataDto));
        return buffer.toString();
    }


}
