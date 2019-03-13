package com.xuyun.platform.modules.ag.serviceImpl;

import com.xuyun.platform.dsfcenterdata.AbstractPlayerService;
import com.xuyun.platform.dsfcenterdata.entity.DsfGmApi;
import com.xuyun.platform.dsfcenterdata.entity.DsfMemberUser;
import com.xuyun.platform.dsfcenterdata.response.RRException;
import com.xuyun.platform.dsfcenterdata.utils.GenerationSequenceUtil;
import com.xuyun.platform.modules.ag.http.AgNetWork;
import com.xuyun.platform.modules.common.constants.AgConstants;
import com.xuyun.platform.modules.common.dto.ag.AgDataDto;
import com.xuyun.platform.modules.common.dto.ag.AgResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * @ClassName: PlayerServiceImpl
 * @Author: R.M.I
 * @CreateTime: 2019年02月23日 10:48:00
 * @Description: TODO
 * @Version 1.0.0
 */
@Service("AgPlayerService")
@Slf4j
public class AgPlayerServiceImpl extends AbstractPlayerService {

    @Autowired
    private AgNetWork agNetWork;
    /**
     * 游戏账号的登錄名, 必須少于 20 個字元
     * 不可以带特殊字符，只可以数字，字母，下划线
     * @param dsfMemberUser
     * @param dsfGmApi
     * @return
     */
    @Override
    public DsfMemberUser registerPlayer(DsfMemberUser dsfMemberUser, DsfGmApi dsfGmApi) {
        AgDataDto agDataDto = new AgDataDto().getAgDataDto(dsfMemberUser,dsfGmApi);
        agDataDto.setMethod(AgConstants.AGIN_FUN_CHECKORCREATEGAMEACCOUT);
        AgResponseDto responseDto =agNetWork.post(dsfGmApi,agDataDto);
        if(AgConstants.AGIN_FUN_SUCC_KEY.equals(responseDto.getInfo())){
            log.info("Ag 玩家创建成功 返回值 = {}",responseDto);
        }else {
            log.error("Ag 玩家创建异常 返回值 = {}",responseDto);
        }
        return dsfMemberUser;
    }

    /**
     * 所有生成的第三方账号名必须以 siteCode + apiPrefix为前缀,
     * 涉及拉单,不可错
     * @param dsfGmApi
     * @return
     */
    @Override
    public String generateLoginName(DsfGmApi dsfGmApi){
        String siteCode = dsfGmApi.getSiteCode();
        String apiPrefix = dsfGmApi.getPrefix();
        int dsfPlayerIdLength = 18 ;
        String generateRandomNum = GenerationSequenceUtil.generateRandomNum(dsfPlayerIdLength - apiPrefix.length()-siteCode.length());
        return siteCode+apiPrefix+generateRandomNum;
    }

    @Override
    public String generatePassword(){
        return  GenerationSequenceUtil.generateRandomNum(18);
    }

    @Override
    public BigDecimal playerBalance(DsfMemberUser dsfMemberUser,DsfGmApi dsfGmApi) {
        AgDataDto agDataDto = new AgDataDto().getAgDataDto(dsfMemberUser,dsfGmApi);
        agDataDto.setMethod(AgConstants.AGIN_FUN_GETBALANCE);
        AgResponseDto responseDto =agNetWork.post(dsfGmApi,agDataDto);
        try {
            return BigDecimal.valueOf(Double.valueOf(responseDto.getInfo()));
        }catch (Exception e){
            log.error("Ag 余额查询异常 responseDto ={}",responseDto);
        }
        throw new RRException("AGIN余额获取失败,其它失败! ,请联系技术，查询原因" + responseDto.getMessage());
    }

    @Override
    public boolean freezePlayer(DsfMemberUser dsfMemberUser,DsfGmApi dsfGmApi) {
        return false;
    }
}
