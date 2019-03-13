package com.invech.platform.dsfcenterdao.service;

import com.invech.platform.dsfcenterdao.mapper.*;
import com.invech.platform.dsfcenterdao.utlis.SiteUtil;
import com.invech.platform.dsfcenterdata.betlog.DsfBetlogGameFish;
import com.invech.platform.dsfcenterdata.betlog.DsfBetlogGamePoker;
import com.invech.platform.dsfcenterdata.betlog.DsfBetlogGameComputer;
import com.invech.platform.dsfcenterdata.betlog.DsfBetlogGameVideo;
import com.invech.platform.dsfcenterdata.constants.ApiConstants;
import com.invech.platform.dsfcenterdata.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

/**
 * @ClassName: DsfBetlogService
 * @Author: R.M.I
 * @CreateTime: 2019年03月04日 20:17:00
 * @Description: TODO
 * @Version 1.0.0
 */
@Service
@Slf4j
public class DsfBetlogService {

    @Autowired
    DsfBetlogGameVideoMapper dsfBetlogGameVideoMapper;

    @Autowired
    InformationSchemaMapper informationSchemaMapper;

    @Autowired
    DsfBetlogGamePokerMapper dsfBetlogGamePokerMapper;

    @Autowired
    DsfBetlogGameFishMapper dsfBetlogGameFishMapper;

    @Autowired
    DsfBetlogGameComputerMapper dsfBetlogGameComputerMapper;

    @Autowired
    SiteUtil siteUtil;

    public List<DsfBetlogGameVideo> videoGameBetlogsBatch(Integer size, String lastUpdateId, String siteCode) {
        // 默认 10000000000000000 为第一次拉单
        long timestamp = getBetTimestamp(lastUpdateId, "dsf_betlog_game_video");
        List<String> months = DateUtil.getTargetMonth(timestamp);
        List<DsfBetlogGameVideo> dsfBetlogGameVideos = new LinkedList<>();
        months.forEach(it -> {
            siteUtil.setDataBase(siteCode);
            if (dsfBetlogGameVideos.size() > size) {
                return;
            }
            dsfBetlogGameVideos.addAll(dsfBetlogGameVideoMapper.videoGameBetlogsBatch(size - dsfBetlogGameVideos.size(), Long.parseLong(lastUpdateId), it));
            log.info(dsfBetlogGameVideos.toString());
        });
        return dsfBetlogGameVideos;
    }


    public List<DsfBetlogGamePoker> pokerBetlogsBatch(Integer size, String lastUpdateId, String siteCode) {
        // 默认 10000000000000000 为第一次拉单
        long timestamp = getBetTimestamp(lastUpdateId, "dsf_betlog_game_poker");
        List<String> months = DateUtil.getTargetMonth(timestamp);
        List<DsfBetlogGamePoker> dsfBetlogGamePokers = new LinkedList<>();
        months.forEach(it -> {
            siteUtil.setDataBase(siteCode);
            if (dsfBetlogGamePokers.size() > size) {
                return;
            }
            dsfBetlogGamePokers.addAll(dsfBetlogGamePokerMapper.pokerGameBetlogsBatch(size - dsfBetlogGamePokers.size(), Long.parseLong(lastUpdateId), it));
            log.info(dsfBetlogGamePokers.toString());
        });
        return dsfBetlogGamePokers;

    }


    public List<DsfBetlogGameFish> fishingBetlogsBatch(Integer size, String lastUpdateId, String siteCode) {
        // 默认 10000000000000000 为第一次拉单
        long timestamp = getBetTimestamp(lastUpdateId, "dsf_betlog_game_fish");
        List<String> months = DateUtil.getTargetMonth(timestamp);
        List<DsfBetlogGameFish> dsfFishingGameBetlogs = new LinkedList<>();
        months.forEach(it -> {
            siteUtil.setDataBase(siteCode);
            if (dsfFishingGameBetlogs.size() > size) {
                return;
            }
            dsfFishingGameBetlogs.addAll(dsfBetlogGameFishMapper.fishGameBetlogsBatch(size - dsfFishingGameBetlogs.size(), Long.parseLong(lastUpdateId), it));
            log.info(dsfFishingGameBetlogs.toString());
        });
        return dsfFishingGameBetlogs;

    }

    public List<DsfBetlogGameComputer> computerGameBetlogsBatch(Integer size, String lastUpdateId, String siteCode) {
        long timestamp = getBetTimestamp(lastUpdateId, "dsf_betlog_game_computer");
        List<String> months = DateUtil.getTargetMonth(timestamp);
        List<DsfBetlogGameComputer> dsfBetlogGameComputers = new LinkedList<>();
        months.forEach(it -> {
            siteUtil.setDataBase(siteCode);
            if (dsfBetlogGameComputers.size() > size) {
                return;
            }
            dsfBetlogGameComputers.addAll(dsfBetlogGameComputerMapper.computerGameBetlogsBatch(size - dsfBetlogGameComputers.size(), Long.parseLong(lastUpdateId), it));
            log.info(dsfBetlogGameComputers.toString());
        });
        return dsfBetlogGameComputers;

    }


    private Long getBetTimestamp(String lastUpdateId, String tablePrefix) {
        if (ApiConstants.BET_FIRST.equals(lastUpdateId)) {
            String yyyyMMdd = informationSchemaMapper.selectBetlogTable(siteUtil.getSchemaName(), tablePrefix).replace(tablePrefix + "_", "") + "01";
            return DateUtil.parse(yyyyMMdd, DateUtil.FORMAT_8_DATE).getTime();
        } else {
            return Long.parseLong(lastUpdateId.substring(0, 13));
        }
    }
}
