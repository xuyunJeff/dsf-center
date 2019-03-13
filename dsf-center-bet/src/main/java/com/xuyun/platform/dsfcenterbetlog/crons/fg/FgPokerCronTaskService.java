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
