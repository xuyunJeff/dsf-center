package com.xuyun.platform.dsfcenterbetlog.crons.ag;

import com.xuyun.platform.dsfcenterbetlog.crons.BaseCronTaskService;
import com.xuyun.platform.dsfcenterdata.AbstractBetlogService;
import com.xuyun.platform.dsfcenterdata.betlog.BetTaskRequestParams;
import com.xuyun.platform.dsfcenterdata.betlog.DsfBetlogRedisDto;
import com.xuyun.platform.dsfcenterdata.enums.GamePlatform;
import com.xuyun.platform.dsfcenterdata.enums.PlatformType;
import com.xxl.job.core.handler.annotation.JobHandler;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName: AgVideoCronTaskService
 * @Author: R.M.I
 * @CreateTime: 2019年03月11日 19:14:00
 * @Description: TODO
 * @Version 1.0.0
 */
@Service
@JobHandler("AgVideoCronTask")
public class AgVideoCronTaskService extends BaseCronTaskService {

    public AgVideoCronTaskService(){
        super.gamePlatform = GamePlatform.Agin;
        platformTypes = new PlatformType[]{PlatformType.VideoGame};
        super.dsfPlatformType = PlatformType.VideoGame.code;
    }
    @Override
    public DsfBetlogRedisDto executeBetGamePlatform(BetTaskRequestParams taskRequestParams, List<String> siteCode, String returnMsg) {
        AbstractBetlogService betlogService = getBetDaoService();
        return betlogService.videoGame(taskRequestParams,siteCode,returnMsg);
    }
}
