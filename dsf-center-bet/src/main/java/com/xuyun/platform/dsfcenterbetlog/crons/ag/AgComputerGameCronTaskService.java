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
 * @ClassName: AgSlotCronTaskService
 * @Author: R.M.I
 * @CreateTime: 2019年03月11日 10:36:00
 * @Description: TODO
 * @Version 1.0.0
 */
@JobHandler("AgComputerGameCronTask")
@Service
public class AgComputerGameCronTaskService extends BaseCronTaskService {

    public AgComputerGameCronTaskService() {
        super.gamePlatform = GamePlatform.Agin;
        platformTypes = new PlatformType[]{PlatformType.ComputerGame};
        super.dsfPlatformType = PlatformType.ComputerGame.code;
    }
    @Override
    public DsfBetlogRedisDto executeBetGamePlatform(BetTaskRequestParams taskRequestParams, List<String> siteCode, String returnMsg) {
        AbstractBetlogService betlogService = getBetDaoService();
        return  betlogService.computerGame(taskRequestParams,siteCode,returnMsg);
    }
}
