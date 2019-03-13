package com.xuyun.platform.dsfcenterbetlog.crons;

import com.xuyun.platform.dsfcenterdao.mapper.*;
import com.xuyun.platform.dsfcenterdao.mapper.DsfBetlogGameVideoMapper;
import com.xuyun.platform.dsfcenterdao.service.DsfSiteService;
import com.xuyun.platform.dsfcenterdao.utlis.SiteUtil;
import com.xuyun.platform.dsfcenterdata.entity.DsfGmApi;
import com.xuyun.platform.dsfcenterdao.mapper.DsfBetlogGameComputerMapper;
import com.xuyun.platform.dsfcenterdao.mapper.DsfBetlogGameFishMapper;
import com.xuyun.platform.dsfcenterdao.mapper.DsfBetlogGamePokerMapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

@Component
@Configuration
@EnableScheduling
public class CreateMonthTableTask {
    @Autowired
    DsfBetlogGameFishMapper dsfBetlogGameFishMapper;
    @Autowired
    DsfBetlogGameComputerMapper dsfBetlogGameComputerMapper;
    @Autowired
    DsfBetlogGamePokerMapper dsfBetlogGamePokerMapper;
    @Autowired
    DsfBetlogGameVideoMapper dsfBetlogGameVideoMapper;
    @Autowired
    SiteUtil siteUtil;
    @Autowired
    DsfSiteService dsfSiteService;


    /**
     * 定时任务，创建下个月份拉单表
     */
    @Scheduled(cron = "0 0 12 * * ?")
    public void execute() {
        //获取当前时间
        Calendar c = Calendar.getInstance();
        SimpleDateFormat simpleDateFormatMonth = new SimpleDateFormat("yyyyMM");
        String time = simpleDateFormatMonth.format(c.getTime());
        List<DsfGmApi> dsfSiteList = dsfSiteService.queryApis();
        this.executeCreateTable(dsfSiteList,time);
        c.add(Calendar.MONTH, +1);
        time = simpleDateFormatMonth.format(c.getTime());
        this.executeCreateTable(dsfSiteList,time);
    }

    private void executeCreateTable(List<DsfGmApi> dsfSiteList,String time){
        if (dsfSiteList.size() > 0) {
            for (DsfGmApi dsfGmApi : dsfSiteList)
                if (StringUtils.isNotEmpty(dsfGmApi.getSiteCode())) {
                    siteUtil.setDataBase(dsfGmApi.getSiteCode());
                    dsfBetlogGameFishMapper.createFishGameBetlog(time);
                    dsfBetlogGameComputerMapper.createComputerGameBetlog(time);
                    dsfBetlogGamePokerMapper.createPokerGameBetlog(time);
                    dsfBetlogGameVideoMapper.createvideoGameBetlog(time);
                    dsfBetlogGameVideoMapper.createvideoGameBetlogItem(time);
                }
        }
    }


}
