package com.invech.platform.dsfcenterbetlog.crons.ag;

import com.invech.platform.dsfcenterbetlog.crons.BaseCronTaskService;
import com.invech.platform.dsfcenterdata.AbstractBetlogService;
import com.invech.platform.dsfcenterdata.betlog.BetTaskRequestParams;
import com.invech.platform.dsfcenterdata.betlog.DsfBetlogRedisDto;
import com.invech.platform.dsfcenterdata.enums.GamePlatform;
import com.invech.platform.dsfcenterdata.enums.PlatformType;
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
