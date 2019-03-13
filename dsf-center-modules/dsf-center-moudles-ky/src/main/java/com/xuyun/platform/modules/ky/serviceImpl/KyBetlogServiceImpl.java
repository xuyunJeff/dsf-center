package com.xuyun.platform.modules.ky.serviceImpl;

import com.alibaba.fastjson.JSONObject;
import com.xuyun.platform.dsfcenterdao.mapper.DsfGmGameMapper;
import com.xuyun.platform.dsfcenterdao.service.BetDaoService;
import com.xuyun.platform.dsfcenterdata.AbstractBetlogService;
import com.invech.platform.dsfcenterdata.betlog.*;
import com.xuyun.platform.dsfcenterdata.betlog.AbstractDsfBetlog;
import com.xuyun.platform.dsfcenterdata.entity.DsfGmApi;
import com.xuyun.platform.dsfcenterdata.entity.DsfGmGame;
import com.xuyun.platform.dsfcenterdata.enums.GamePlatform;
import com.xuyun.platform.dsfcenterdata.enums.PlatformType;
import com.xuyun.platform.dsfcenterdata.utils.DateUtil;
import com.xuyun.platform.dsfcenterdata.utils.GenerationSequenceUtil;
import com.xuyun.platform.modules.common.constants.KyConstans;
import com.invech.platform.modules.common.dto.ky.*;
import com.xuyun.platform.modules.common.dto.ky.*;
import com.xuyun.platform.modules.common.enums.KyErrorCode;
import com.xuyun.platform.modules.ky.http.KyNetWork;
import com.xuyun.platform.dsfcenterdata.betlog.BetTaskRequestParams;
import com.xuyun.platform.dsfcenterdata.betlog.DsfBetlogGamePoker;
import com.xuyun.platform.dsfcenterdata.betlog.DsfBetlogRedisDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * @ClassName: DsBetService
 * @Author: R.M.I
 * @CreateTime: 2019年03月01日 14:12:00
 * @Description: TODO
 * @Version 1.0.0
 */
@Slf4j
@Service("KyBetlogService")
public class KyBetlogServiceImpl extends AbstractBetlogService {

    @Autowired
    KyNetWork kyNetWork;
    @Autowired
    BetDaoService betDaoService;
    @Autowired
    DsfGmGameMapper dsfGmGameMapper;

    @Override
    public DsfBetlogRedisDto videoGame(BetTaskRequestParams betTaskRequestParams, List<String> siteCode, String returnMsg) {
        return null;
    }

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
        log.info("这里是Ky的拉单流程");
        DsfGmApi dsfGmApi = betTaskRequestParams.getDsfGmApi();
        String apiPrefix = dsfGmApi.getPrefix();
//        LocalDateTime sTime = betTaskRequestParams.getSTime();
//        LocalDateTime eTime = betTaskRequestParams.getETime();
//        LocalDateTime now = DateUtil.current();
        KyBetRequest kyBetRequest = new KyBetRequest(KyConstans.QUERY_GAME_BET, dsfGmApi, betTaskRequestParams.getSTime(), betTaskRequestParams.getETime());
        KyReponse<KyBetReponse> response = kyNetWork.get(dsfGmApi, kyBetRequest);
        KyDataReponse dsResponse = JSONObject.parseObject(JSONObject.toJSONString(response.getD()), KyDataReponse.class);
        if (dsResponse.getCode().equals(KyErrorCode.Success.getCode())) {
            List<KyBetRecordReponse> recordList = dsResponse.getList();
            if (0 < recordList.size()) {
                List<AbstractDsfBetlog> dsfBetlogs = handleDsBetlogs(recordList, returnMsg, apiPrefix, siteCode);
                return new DsfBetlogRedisDto(dsfBetlogs, betTaskRequestParams, returnMsg);
            }
        }
        log.info("Ky注单异常");
        returnMsg += "Ky注单异常" + response.toString();
        return new DsfBetlogRedisDto(Collections.emptyList(), betTaskRequestParams, returnMsg);
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
    private List<AbstractDsfBetlog> handleDsBetlogs(List<KyBetRecordReponse> recordList, String returnMsg, String apiPrefix, List<String> siteCodes) {
        int s = 1;
        Long betId = GenerationSequenceUtil.genOrderNo();
        List<AbstractDsfBetlog> gameBetlogList = new LinkedList<>();
        LocalDateTime createTime = DateUtil.current();
        for (KyBetRecordReponse it : recordList) {
            if (recordList.toString().contains("gameId")) {
                for (int i = 0; i < it.getGameId().length; i++) {
                    DsfBetlogGamePoker betlog = new DsfBetlogGamePoker();
                    betlog.setBetId(String.valueOf(betId + s));
                    betlog.setDsfPlayerId(it.getAccounts()[i]);
                    betlog.setIssueNo(it.getServerId()[i].toString());
                    betlog.setTableName(it.getTableId()[i].toString());
                    betlog.setDsfPlatformBetId(it.getGameId()[i]);
                    betlog.setDsfPlayerId(StringUtils.substringAfter(it.getAccounts()[i], "_"));
                    String gameNameCn = "";
                    if (it.getKindId() != null) {
                        DsfGmGame gmGame = new DsfGmGame();
                        gmGame.setGameParam(it.getKindId()[i].toString());
                        List<DsfGmGame> dsfGmGameList = dsfGmGameMapper.select(gmGame);
                        if (dsfGmGameList.size() > 0) {
                            gameNameCn = dsfGmGameList.get(0).getGameName();
                        }
                    }
                    //判断属于哪个盘口
                    String lineCode = it.getLineCode()[i];
                    siteCodes.forEach(siteCode -> {
                        if (lineCode.endsWith(siteCode)) {
                            betlog.setSiteCode(siteCode);
                        }
                    });
                    JSONObject remarkMap = new JSONObject();
                    remarkMap.put("chairId", it.getChairId()[i]);
                    remarkMap.put("userCount", it.getUserCount()[i]);
                    remarkMap.put("revenue", it.getRevenue()[i]);
                    remarkMap.put("channelId", it.getChannelId()[i]);
                    betlog.setRemark(remarkMap.toString());
                    betlog.setGameNameCn(gameNameCn);
                    betlog.setCategoryName(PlatformType.Poker.title);
                    betlog.setGamePlatform(GamePlatform.Ky.platformCode);
                    betlog.setValidStake(it.getCellScore()[i]);
                    betlog.setStakeAmount(it.getAllBet()[i]);
                    betlog.setWinLoss(it.getProfit()[i].subtract(betlog.getStakeAmount()));
                    betlog.setGameTime(it.getGameStartTime()[i]);
                    betlog.setCreateTime(createTime);
                    gameBetlogList.add(betlog);
                }

            }


        }

        return gameBetlogList;
    }

    @Override
    public DsfBetlogRedisDto computerGame(BetTaskRequestParams betTaskRequestParams, List<String> siteCode, String returnMsg) {
        return this.videoGame(betTaskRequestParams, siteCode, returnMsg);
    }

    @Override
    public DsfBetlogRedisDto fishGame(BetTaskRequestParams betTaskRequestParams, List<String> siteCode, String returnMsg) {
        return super.fishGame(betTaskRequestParams, siteCode, returnMsg);
    }

    @Override
    public DsfBetlogRedisDto fruitGame(BetTaskRequestParams betTaskRequestParams, List<String> siteCode, String returnMsg) {
        return this.videoGame(betTaskRequestParams, siteCode, returnMsg);
    }
}



