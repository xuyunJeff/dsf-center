package com.invech.platform.dsfcenterbetlog.crons.fg;

import com.invech.platform.dsfcenterbetlog.crons.BaseCronTaskService;
import com.invech.platform.dsfcenterdata.AbstractBetlogService;
import com.invech.platform.dsfcenterdata.betlog.BetTaskRequestParams;
import com.invech.platform.dsfcenterdata.betlog.DsfBetlogRedisDto;
import com.invech.platform.dsfcenterdata.enums.GamePlatform;
import com.invech.platform.dsfcenterdata.enums.PlatformType;
import com.xxl.job.core.handler.annotation.JobHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName: FgPokerCronTaskService
 * @Author: R.M.I
 * @CreateTime: 2019年03月06日 20:42:00
 * @Description: TODO
 * @Version 1.0.0
 */
@Service
@Slf4j
@JobHandler("FgPokerCronTask")
public class FgPokerCronTaskService extends BaseCronTaskService {

    public FgPokerCronTaskService() {
        super.gamePlatform = GamePlatform.Fg;
        super.platformTypes= new PlatformType[] {PlatformType.Poker};
        super.dsfPlatformType =PlatformType.Poker.code;
    }

    @Override
    public DsfBetlogRedisDto executeBetGamePlatform(BetTaskRequestParams taskRequestParams, List<String> siteCode, String returnMsg) {
        AbstractBetlogService betlogService = getBetDaoService();
        return betlogService.pokerGame(taskRequestParams,siteCode,returnMsg);
    }
}
