package com.xuyun.platform.dsfcenterbetlog.crons.ds;

import com.xuyun.platform.dsfcenterbetlog.crons.BaseCronTaskService;
import com.xuyun.platform.dsfcenterdao.service.DsfSiteService;
import com.xuyun.platform.dsfcenterdata.AbstractBetlogService;
import com.xuyun.platform.dsfcenterdata.betlog.BetTaskRequestParams;
import com.xuyun.platform.dsfcenterdata.betlog.DsfBetlogRedisDto;
import com.xuyun.platform.dsfcenterdata.enums.GamePlatform;
import com.xuyun.platform.dsfcenterdata.enums.PlatformType;
import com.xxl.job.core.handler.annotation.JobHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName: DsCronTaskService
 * @Author: R.M.I
 * @CreateTime: 2019年03月01日 11:10:00
 * @Description: TODO
 * @Version 1.0.0
 */
@JobHandler("DsCronTask")
@Service
@Slf4j
public class DsCronTaskService extends BaseCronTaskService {

    @Autowired
    DsfSiteService dsfSiteService;



    public DsCronTaskService() {
        super.gamePlatform = GamePlatform.Ds;
        super.platformTypes = new PlatformType[] {PlatformType.VideoGame};
    }

    /**
     *
     * @param taskRequestParams
     * @param siteCode
     * @param returnMsg
     * @return
     */
    @Override
    public DsfBetlogRedisDto executeBetGamePlatform(BetTaskRequestParams taskRequestParams, List<String> siteCode, String returnMsg) {
        AbstractBetlogService betlogService = getBetDaoService();
        return  betlogService.videoGame(taskRequestParams,siteCode,returnMsg);
    }
}
