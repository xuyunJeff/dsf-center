package com.xuyun.platform.dsfcenterbetlog.crons.ky;

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
@JobHandler("KyPokerCronTask")
public class KyPokerCronTaskService extends BaseCronTaskService {
    public KyPokerCronTaskService() {
        super.gamePlatform = GamePlatform.Ky;
        super.platformTypes = new PlatformType[] {PlatformType.Poker};
        super.dsfPlatformType =PlatformType.Poker.code;
    }

    @Override
    public DsfBetlogRedisDto executeBetGamePlatform(BetTaskRequestParams taskRequestParams, List<String> siteCode, String returnMsg) {
        AbstractBetlogService betlogService = getBetDaoService();
        return betlogService.pokerGame(taskRequestParams,siteCode,returnMsg);
    }
}
