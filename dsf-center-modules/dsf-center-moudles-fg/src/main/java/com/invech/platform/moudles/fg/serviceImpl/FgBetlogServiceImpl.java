package com.invech.platform.moudles.fg.serviceImpl;

import com.alibaba.fastjson.JSONObject;
import com.invech.platform.dsfcenterdao.mapper.DsfGmGameMapper;
import com.invech.platform.dsfcenterdao.service.BetDaoService;
import com.invech.platform.dsfcenterdata.AbstractBetlogService;
import com.invech.platform.dsfcenterdata.betlog.*;
import com.invech.platform.dsfcenterdata.entity.DsfGmApi;
import com.invech.platform.dsfcenterdata.entity.DsfGmGame;
import com.invech.platform.dsfcenterdata.enums.GamePlatform;
import com.invech.platform.dsfcenterdata.enums.PlatformType;
import com.invech.platform.dsfcenterdata.utils.DateUtil;
import com.invech.platform.dsfcenterdata.utils.GenerationSequenceUtil;
import com.invech.platform.dsfcenterdata.utils.StringUtils;
import com.invech.platform.modules.common.constants.FgConstants;
import com.invech.platform.modules.common.dto.fg.FgBetResponse;
import com.invech.platform.modules.common.dto.fg.FgBetlogResponse;
import com.invech.platform.modules.common.dto.fg.FgResponse;
import com.invech.platform.modules.common.enums.*;
import com.invech.platform.moudles.fg.http.FgNetWork;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @ClassName: DsBetService
 * @Author: R.M.I
 * @CreateTime: 2019年03月01日 14:12:00
 * @Description: TODO
 * @Version 1.0.0
 */
@Slf4j
@Service("FgBetlogService")
public class FgBetlogServiceImpl extends AbstractBetlogService {

    @Autowired
    FgNetWork fgNetWork;
    @Autowired
    BetDaoService betDaoService;
    @Autowired
    DsfGmGameMapper dsfGmGameMapper;

    /**
     * 每次的拉单beginId会存在redis,永不过期
     * 同时 xxl-job 数据库中会把任务记录会保存每次的beginId,丢失的情况下直接xxl日志查就行
     * 执行在任务中加入执行参数
     *
     * @param betTaskRequestParams
     * @return
     */
    @Override
    public DsfBetlogRedisDto pokerGame(BetTaskRequestParams betTaskRequestParams, List<String> siteCode, String returnMsg) {
        log.info("这里是Fg的拉单流程");
        String extraKey = getRedisData(betTaskRequestParams, siteCode, returnMsg,"poker");
        String method = FgConstants.BETLOG_POKER;
        method = getMethod(betTaskRequestParams, method);
        DsfGmApi dsfGmApi = betTaskRequestParams.getDsfGmApi();
        String apiPrefix = dsfGmApi.getPrefix();
        FgResponse fgResponse = fgNetWork.post(dsfGmApi, null, method);
        if (fgResponse.getCode().equals(FgErrorCode.Success.getCode())) {
            FgBetResponse fgBetResponse = JSONObject.parseObject(JSONObject.toJSONString(fgResponse.getData()), FgBetResponse.class);
            returnMsg += "Fg注单获取成功 size = " + fgBetResponse.getData().size();
            List<FgBetlogResponse> recordList = fgBetResponse.getData();
            List<AbstractDsfBetlog> dsfBetlogs = new LinkedList<>();
            if (0 < recordList.size()) {
                if (betTaskRequestParams == null) {
                    betTaskRequestParams = new BetTaskRequestParams(fgBetResponse.getPage_key());
                } else {
                    betTaskRequestParams.setBetExtraKey(fgBetResponse.getPage_key());
                }
                dsfBetlogs = handleDsBetlogs(recordList, returnMsg, apiPrefix, siteCode);
            }
            return new DsfBetlogRedisDto(dsfBetlogs, betTaskRequestParams, returnMsg);
        }
        log.info("Fg注单异常");
        return new DsfBetlogRedisDto(Collections.emptyList(), betTaskRequestParams, returnMsg);
    }

    private String getMethod(BetTaskRequestParams betTaskRequestParams, String method) {
        if (betTaskRequestParams != null) {
            if (!StringUtils.isEmpty(betTaskRequestParams.getBetExtraKey())) {
                method = method + "page_key/" + betTaskRequestParams.getBetExtraKey();
            }
            if (!StringUtils.isEmpty(betTaskRequestParams.getId())) {
                method = method + "id/" + betTaskRequestParams.getId();
            }
        }
        return method;
    }

    private String getRedisData(BetTaskRequestParams betTaskRequestParams, List<String> siteCode, String returnMsg,String key) {

        DsfGmApi dsfGmApi = betTaskRequestParams.getDsfGmApi();
        String extraKey = betTaskRequestParams.getBetExtraKey();
        if (StringUtils.isEmpty(extraKey)) {
            betTaskRequestParams = betDaoService.getBetRedisKey(dsfGmApi.getApiName()+":"+key);
            if (betTaskRequestParams != null) {
                extraKey = betTaskRequestParams.getBetExtraKey();
            }
        }
        return extraKey;
    }


    /**
     * Ds 注单翻译
     *
     * @param recordList
     * @param returnMsg
     * @param apiPrefix
     * @param siteCodes
     * @return
     */
    private List<AbstractDsfBetlog> handleDsBetlogs(List<FgBetlogResponse> recordList, String returnMsg, String apiPrefix, List<String> siteCodes) {
        List<AbstractDsfBetlog> videoGameBetlogs = new LinkedList<>();
        Long betId = GenerationSequenceUtil.genOrderNo();
        int i = 1;
        for (FgBetlogResponse it : recordList) {
            LocalDateTime gameTime = DateUtil.getDateTimeOfTimestamp(Long.valueOf(it.getTime() + "000"));
            LocalDateTime createTime = DateUtil.current();
            String gameNameCn = "";
            if (it.getGameId() != null) {
                DsfGmGame gmGame = new DsfGmGame();
                gmGame.setGameParam(it.getGameId().toString());
                List<DsfGmGame> dsfGmGameList = dsfGmGameMapper.select(gmGame);
                if (dsfGmGameList.size() > 0) {
                    gameNameCn = dsfGmGameList.get(0).getGameName();
                }
            }
            JSONObject remarkMap = new JSONObject();
            remarkMap.put("total_agent_uid", it.getTotalAgentUid().toString());
            remarkMap.put("agent_uid", it.getAgentUid().toString());
            remarkMap.put("game_id", it.getGameId().toString());
            if (it.getDevice() != null) {
                if (it.getDevice() == FgDeviceType.ANDROID_H.getCode()) {
                    remarkMap.put("device", FgDeviceType.ANDROID_H.getName());
                } else if (it.getDevice() == FgDeviceType.ANDROID_S.getCode()) {
                    remarkMap.put("device", FgDeviceType.ANDROID_S.getName());
                } else if (it.getDevice() == FgDeviceType.IOS_H.getCode()) {
                    remarkMap.put("device", FgDeviceType.IOS_H.getName());
                } else if (it.getDevice() == FgDeviceType.IOS_S.getCode()) {
                    remarkMap.put("device", FgDeviceType.IOS_S.getName());
                } else if (it.getDevice() == FgDeviceType.OTHER_H.getCode()) {
                    remarkMap.put("device", FgDeviceType.OTHER_H.getName());
                } else if (it.getDevice() == FgDeviceType.OTHER_S.getCode()) {
                    remarkMap.put("device", FgDeviceType.OTHER_S.getName());
                } else {
                    remarkMap.put("device", FgDeviceType.PC.getName());
                }

            }
            if (it.getType() != null) {
                if (it.getType() == FgFishType.BOSS.getCode()) {
                    remarkMap.put("type", FgFishType.BOSS.getName());
                } else if (it.getType() == FgFishType.FISH_BUY_NOTE.getCode()) {
                    remarkMap.put("type", FgFishType.FISH_BUY_NOTE.getName());
                } else if (it.getType() == FgFishType.FUND_PUNCHASE.getCode()) {
                    remarkMap.put("type", FgFishType.FUND_PUNCHASE.getName());
                } else if (it.getType() == FgFishType.JP_BILL.getCode()) {
                    remarkMap.put("type", FgFishType.JP_BILL.getName());
                } else if (it.getType() == FgFishType.LASER_CONNON.getCode()) {
                    remarkMap.put("type", FgFishType.LASER_CONNON.getName());
                } else if (it.getType() == FgFishType.LEVEL_BONUS.getCode()) {
                    remarkMap.put("type", FgFishType.LEVEL_BONUS.getName());
                } else if (it.getType() == FgFishType.LUCK_BOX.getCode()) {
                    remarkMap.put("type", FgFishType.LUCK_BOX.getName());
                } else {
                    remarkMap.put("type", FgFishType.FISH_BILL_SCENE.getName());
                }
            }

            String jsonObject = JSONObject.toJSONString(remarkMap);

            if (!StringUtils.isEmpty(it.getGt())) {
                if (it.getGt().equals(FgGameType.FISH.getGameType())) {
                    DsfBetlogGameFish betlog = new DsfBetlogGameFish();
                    betlog.setBetId(String.valueOf(betId + i));
                    betlog.setDsfPlatformBetId(it.getId());
                    //输赢
                    if (it.getAllWins() != null && it.getAllBets() != null) {
                        BigDecimal winLoss = it.getAllWins().subtract(it.getAllBets());
                        betlog.setWinLoss(winLoss);
                    }
                    betlog.setIssueNo(it.getId());
                    betlog.setDsfPlayerId(it.getPlayerName());
                    betlog.setMemberUser(it.getPlayerName());
                    betlog.setCategoryName(PlatformType.Fish.title);
                    betlog.setGameNameCn(gameNameCn);
                    betlog.setGamePlatform(GamePlatform.Fg.platformCode);
                    //判断属于哪个盘口
                    siteCodes.forEach(siteCode -> {
                        if (it.getPlayerName().startsWith(siteCode)) {
                            betlog.setSiteCode(siteCode);
                        }
                    });
                    betlog.setCreateTime(createTime);
                    betlog.setGameTime(DateUtil.getDateTimeOfTimestamp(Long.valueOf(it.getStartTime() + "000")));
                    betlog.setStakeAmount(it.getAllBets());
                    betlog.setValidStake(it.getAllBets());

                    betlog.setRemark(jsonObject);
                    videoGameBetlogs.add(betlog);
                }

                if (it.getGt().equals(FgGameType.FRUIT.getGameType()) || it.getGt().equals(FgGameType.SLOT.getGameType())) {
                    DsfBetlogGameComputer betlog = new DsfBetlogGameComputer();
                    betlog.setBetId(String.valueOf(betId + i));
                    betlog.setDsfPlatformBetId(it.getId());
                    //输赢
                    if (it.getAllWins() != null && it.getAllBets() != null) {
                        BigDecimal winLoss = it.getAllWins().subtract(it.getAllBets());
                        betlog.setWinLoss(winLoss);
                    }
                    betlog.setIssueNo(it.getId());
                    betlog.setMemberUser(it.getPlayerName());
                    betlog.setDsfPlayerId(it.getPlayerName());
                    betlog.setCategoryName(PlatformType.ComputerGame.title);
                    if (it.getGt().equals(FgGameType.FRUIT.getGameType())) {
                        betlog.setGameNameCn(gameNameCn);
                    } else {
                        betlog.setGameNameCn(gameNameCn);
                    }
                    betlog.setGamePlatform(GamePlatform.Fg.platformCode);
                    //判断属于哪个盘口
                    siteCodes.forEach(siteCode -> {
                        if (it.getPlayerName().startsWith(siteCode)) {
                            betlog.setSiteCode(siteCode);
                        }
                    });
                    betlog.setCreateTime(createTime);
                    betlog.setGameTime(gameTime);
                    betlog.setStakeAmount(it.getAllBets());
                    betlog.setValidStake(it.getAllBets());
                    betlog.setRemark(jsonObject);
                    videoGameBetlogs.add(betlog);
                }

                if (it.getGt().equals(FgGameType.POKER.getGameType())) {
                    DsfBetlogGamePoker betlog = new DsfBetlogGamePoker();
                    betlog.setBetId(String.valueOf(betId + i));
                    betlog.setDsfPlatformBetId(it.getId());
                    //输赢
                    if (it.getAllWins() != null && it.getAllBets() != null) {
                        BigDecimal winLoss = it.getAllWins().subtract(it.getAllBets());
                        betlog.setWinLoss(winLoss);
                    }
                    betlog.setIssueNo(it.getId());
                    betlog.setDsfPlayerId(it.getPlayerName());
                    betlog.setMemberUser(it.getPlayerName());
                    betlog.setCategoryName(PlatformType.Poker.title);
                    betlog.setGameNameCn(gameNameCn);
                    betlog.setGamePlatform(GamePlatform.Fg.platformCode);
                    //判断属于哪个盘口
                    siteCodes.forEach(siteCode -> {
                        if (it.getPlayerName().startsWith(siteCode)) {
                            betlog.setSiteCode(siteCode);
                        }
                    });
                    betlog.setCreateTime(createTime);
                    betlog.setGameTime(gameTime);
                    betlog.setStakeAmount(it.getAllBets());
                    betlog.setValidStake(it.getAllBets());
                    betlog.setRemark(jsonObject);
                    videoGameBetlogs.add(betlog);
                }

            }

            i++;
        }
        return videoGameBetlogs;


    }

    @Override
    public DsfBetlogRedisDto computerGame(BetTaskRequestParams betTaskRequestParams, List<String> siteCode, String returnMsg) {
        log.info("这里是Fg的拉单流程");
        String extraKey = getRedisData(betTaskRequestParams, siteCode, returnMsg,"slot");
        String method = FgConstants.BETLOG_SLOT;
        method = getMethod(betTaskRequestParams, method);
        DsfGmApi dsfGmApi = betTaskRequestParams.getDsfGmApi();
        String apiPrefix = dsfGmApi.getPrefix();
        FgResponse fgResponse = fgNetWork.post(dsfGmApi, null, method);
        if (fgResponse.getCode().equals(DsErroeCodes.Success.getCode())) {
            FgBetResponse fgBetResponse = JSONObject.parseObject(JSONObject.toJSONString(fgResponse.getData()), FgBetResponse.class);
            returnMsg += "Fg slot 注单获取成功 size = " + fgBetResponse.getData().size();
            List<FgBetlogResponse> recordList = fgBetResponse.getData();
            List<AbstractDsfBetlog> dsfBetlogs = new LinkedList<>();
            if (0 < recordList.size()) {
                if (betTaskRequestParams == null) {
                    betTaskRequestParams = new BetTaskRequestParams(fgBetResponse.getPage_key());
                } else {
                    betTaskRequestParams.setBetExtraKey(fgBetResponse.getPage_key());
                }
                dsfBetlogs = handleDsBetlogs(recordList, returnMsg, apiPrefix, siteCode);
            }
            return new DsfBetlogRedisDto(dsfBetlogs, betTaskRequestParams, returnMsg);
        }
        log.info("Fg注单异常");
        return new DsfBetlogRedisDto(Collections.emptyList(), betTaskRequestParams, returnMsg);
    }

    @Override
    public DsfBetlogRedisDto fishGame(BetTaskRequestParams betTaskRequestParams, List<String> siteCode, String returnMsg) {
        log.info("这里是Fg的拉单流程");
        String extraKey = getRedisData(betTaskRequestParams, siteCode, returnMsg,"fish");
        String method = FgConstants.BETLOG_FISH;
        method = getMethod(betTaskRequestParams, method);
        DsfGmApi dsfGmApi = betTaskRequestParams.getDsfGmApi();
        String apiPrefix = dsfGmApi.getPrefix();
        FgResponse fgResponse = fgNetWork.post(dsfGmApi, null, method);
        if (fgResponse.getCode().equals(DsErroeCodes.Success.getCode())) {
            FgBetResponse fgBetResponse = JSONObject.parseObject(JSONObject.toJSONString(fgResponse.getData()), FgBetResponse.class);
            returnMsg += "Fg fish 注单获取成功 size = " + fgBetResponse.getData().size();
            List<FgBetlogResponse> recordList = fgBetResponse.getData();
            List<AbstractDsfBetlog> dsfBetlogs = new LinkedList<>();
            if (0 < recordList.size()) {
                if (betTaskRequestParams == null) {
                    betTaskRequestParams = new BetTaskRequestParams(fgBetResponse.getPage_key());
                } else {
                    betTaskRequestParams.setBetExtraKey(fgBetResponse.getPage_key());
                }
                dsfBetlogs = handleDsBetlogs(recordList, returnMsg, apiPrefix, siteCode);
            }
            return new DsfBetlogRedisDto(dsfBetlogs, betTaskRequestParams, returnMsg);
        }
        log.info("Fg注单异常");
        return new DsfBetlogRedisDto(Collections.emptyList(), betTaskRequestParams, returnMsg);
    }

    @Override
    public DsfBetlogRedisDto fruitGame(BetTaskRequestParams betTaskRequestParams, List<String> siteCode, String returnMsg) {
        log.info("这里是Fg的拉单流程");
        String extraKey = getRedisData(betTaskRequestParams, siteCode, returnMsg,"fruit");
        String method = FgConstants.BETLOG_FRUIT;
        method = getMethod(betTaskRequestParams, method);
        DsfGmApi dsfGmApi = betTaskRequestParams.getDsfGmApi();
        String apiPrefix = dsfGmApi.getPrefix();
        FgResponse fgResponse = fgNetWork.post(dsfGmApi, null, method);
        if (fgResponse.getCode().equals(DsErroeCodes.Success.getCode())) {
            FgBetResponse fgBetResponse = JSONObject.parseObject(JSONObject.toJSONString(fgResponse.getData()), FgBetResponse.class);
            returnMsg += "Fg fruit 注单获取成功 size = " + fgBetResponse.getData().size();
            List<FgBetlogResponse> recordList = fgBetResponse.getData();
            List<AbstractDsfBetlog> dsfBetlogs = new LinkedList<>();
            if (0 < recordList.size()) {
                if (betTaskRequestParams == null) {
                    betTaskRequestParams = new BetTaskRequestParams(fgBetResponse.getPage_key());
                } else {
                    betTaskRequestParams.setBetExtraKey(fgBetResponse.getPage_key());
                }
                dsfBetlogs = handleDsBetlogs(recordList, returnMsg, apiPrefix, siteCode);
            }
            return new DsfBetlogRedisDto(dsfBetlogs, betTaskRequestParams, returnMsg);
        }
        log.info("Fg注单异常");
        return new DsfBetlogRedisDto(Collections.emptyList(), betTaskRequestParams, returnMsg);
    }
}



