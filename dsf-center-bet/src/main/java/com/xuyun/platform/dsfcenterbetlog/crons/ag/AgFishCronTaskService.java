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
 *  ag 通过ftp服务区读取文件xml
 *  按照platformType 区分ag游戏种类
 *  GR : 真人
 *  EBR : 电子
 *  HPR : 养鱼
 *  HSR : 捕鱼下注
 *  一个文件中包含所有的种类,所有一次会读取ag所有游戏类别
 * @ClassName: AgGRCronTaskService
 * @Author: R.M.I
 * @CreateTime: 2019年03月08日 21:21:00
 * @Description: TODO
 * @Version 1.0.0
 */
@Service
@JobHandler("AgFishCronTask")
public class AgFishCronTaskService extends BaseCronTaskService {

    public AgFishCronTaskService() {
        super.gamePlatform = GamePlatform.Agin;
        platformTypes = new PlatformType[]{PlatformType.Fish};
        super.dsfPlatformType = PlatformType.Fish.code;
    }

    @Override
    public DsfBetlogRedisDto executeBetGamePlatform(BetTaskRequestParams taskRequestParams, List<String> siteCode, String returnMsg) {
        AbstractBetlogService betlogService = getBetDaoService();
        return  betlogService.fishGame(taskRequestParams,siteCode,returnMsg);
    }
}
