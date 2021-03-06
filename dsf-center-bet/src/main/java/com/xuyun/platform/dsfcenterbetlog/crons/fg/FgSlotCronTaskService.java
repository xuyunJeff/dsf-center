package com.xuyun.platform.dsfcenterbetlog.crons.fg;

import com.xuyun.platform.dsfcenterbetlog.crons.BaseCronTaskService;
import com.xuyun.platform.dsfcenterdata.AbstractBetlogService;
import com.xuyun.platform.dsfcenterdata.betlog.BetTaskRequestParams;
import com.xuyun.platform.dsfcenterdata.betlog.DsfBetlogRedisDto;
import com.xuyun.platform.dsfcenterdata.enums.GamePlatform;
import com.xuyun.platform.dsfcenterdata.enums.PlatformType;
import com.xxl.job.core.handler.annotation.JobHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@JobHandler("FgSlotCronTask")
public class FgSlotCronTaskService extends BaseCronTaskService {
    public FgSlotCronTaskService() {
        super.gamePlatform = GamePlatform.Fg;
        super.platformTypes= new PlatformType[] {PlatformType.ComputerGame};
        super.dsfPlatformType =PlatformType.ComputerGame.code;
    }

    @Override
    public DsfBetlogRedisDto executeBetGamePlatform(BetTaskRequestParams taskRequestParams, List<String> siteCode, String returnMsg) {
        AbstractBetlogService betlogService = getBetDaoService();
        return betlogService.computerGame(taskRequestParams,siteCode,returnMsg);
    }
}
