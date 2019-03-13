package com.invech.platform.dsfcenterbetlog.crons.ds;

import com.invech.platform.dsfcenterbetlog.crons.BaseCronTaskService;
import com.invech.platform.dsfcenterdao.service.DsfSiteService;
import com.invech.platform.dsfcenterdata.AbstractBetlogService;
import com.invech.platform.dsfcenterdata.betlog.BetTaskDto;
import com.invech.platform.dsfcenterdata.betlog.BetTaskRequestParams;
import com.invech.platform.dsfcenterdata.betlog.DsfBetlogRedisDto;
import com.invech.platform.dsfcenterdata.entity.DsfGmApi;
import com.invech.platform.dsfcenterdata.enums.GamePlatform;
import com.invech.platform.dsfcenterdata.enums.PlatformType;
import com.invech.platform.modules.common.constants.DsConstants;
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
