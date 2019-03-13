package com.xuyun.platform.modudles.ds.serviceImpl;

import com.alibaba.fastjson.JSONObject;
import com.xuyun.platform.dsfcenterdao.service.BetDaoService;
import com.xuyun.platform.dsfcenterdata.AbstractBetlogService;
import com.invech.platform.dsfcenterdata.betlog.*;
import com.xuyun.platform.dsfcenterdata.betlog.*;
import com.xuyun.platform.dsfcenterdata.entity.DsfGmApi;
import com.xuyun.platform.dsfcenterdata.enums.GamePlatform;
import com.xuyun.platform.dsfcenterdata.enums.PlatformType;
import com.xuyun.platform.dsfcenterdata.utils.DateUtil;
import com.xuyun.platform.dsfcenterdata.utils.GenerationSequenceUtil;
import com.xuyun.platform.dsfcenterdata.utils.StringUtils;
import com.xuyun.platform.modudles.ds.http.DsNetWork;
import com.xuyun.platform.modules.common.constants.DsConstants;
import com.xuyun.platform.modules.common.dto.ds.request.DsBetRequest;
import com.xuyun.platform.modules.common.dto.ds.request.DsRequest;
import com.xuyun.platform.modules.common.dto.ds.response.DsBetResponse;
import com.xuyun.platform.modules.common.dto.ds.response.DsBetlogReponse;
import com.xuyun.platform.modules.common.dto.ds.response.DsReportDetailReponse;
import com.xuyun.platform.modules.common.dto.ds.response.DsResponse;
import com.xuyun.platform.modules.common.enums.DsErroeCodes;
import com.xuyun.platform.modules.common.enums.DsGameType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName: DsBetService
 * @Author: R.M.I
 * @CreateTime: 2019年03月01日 14:12:00
 * @Description: TODO
 * @Version 1.0.0
 */
@Slf4j
@Service("DsBetlogService")
public class DsBetlogServiceImpl extends AbstractBetlogService {

    @Autowired
    DsNetWork dsNetWork;
    @Autowired
    BetDaoService betDaoService;

    /**
     * 每次的拉单beginId会存在redis,永不过期
     * 同时 xxl-job 数据库中会把任务记录会保存每次的beginId,丢失的情况下直接xxl日志查就行
     * 执行在任务中加入执行参数
     *
     * @param betTaskRequestParams
     * @return
     */
    @Override
    public DsfBetlogRedisDto videoGame(BetTaskRequestParams betTaskRequestParams, List<String> siteCode, String returnMsg) {
        log.info("这里是Ds的拉单流程");
        DsfGmApi dsfGmApi = betTaskRequestParams.getDsfGmApi();
        String apiPrefix = dsfGmApi.getPrefix();
        String extraKey = betTaskRequestParams.getBetExtraKey();
        if (StringUtils.isEmpty(extraKey)) {
            betTaskRequestParams = betDaoService.getBetRedisKey(dsfGmApi.getApiName());
            extraKey = betTaskRequestParams.getBetExtraKey();
        }
        DsBetRequest dsBetRequest = new DsBetRequest(extraKey);
        DsRequest dsRequest = new DsRequest<>(dsBetRequest, DsConstants.COMMAND_GET_RECORD_BY_SEQUENCENO);
        dsRequest.setHashCode(DsRequest.getHashCode(dsfGmApi));
        DsResponse<DsBetResponse> dsResponse = dsNetWork.post(dsfGmApi, dsRequest);
        if (dsResponse.getErrorCode().equals(DsErroeCodes.Success)) {
            DsBetResponse betResponse = JSONObject.parseObject(JSONObject.toJSONString(dsResponse.getParams()), DsBetResponse.class);
            returnMsg += "Ds 注单获取成功 size = " + betResponse.getRecordList().size();
            String pictureUrl = DsfGmApi.getSecureCodeMap(dsfGmApi.getSecureCode()).get("resultPictureUrl").toString();
            //此处获取Ds的sequenceNo 最大值 ,要更新缓存
            List<DsBetlogReponse> recordList = betResponse.getRecordList();
            if (0 < recordList.size()) {
                Comparator<DsBetlogReponse> comparator = Comparator.comparing(DsBetlogReponse::getSequenceNo);
                extraKey = recordList.stream().max(comparator).get().getSequenceNo().toString();
                betTaskRequestParams.setBetExtraKey(extraKey);
                List<AbstractDsfBetlog> dsfBetlogs = handleDsBetlogs(recordList, returnMsg, apiPrefix, siteCode, pictureUrl);
                return new DsfBetlogRedisDto(dsfBetlogs, betTaskRequestParams, returnMsg);
            }
        }
        log.info("Ds注单异常");
        returnMsg += "Ds注单异常" + dsResponse.toString();
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
    private List<AbstractDsfBetlog> handleDsBetlogs(List<DsBetlogReponse> recordList, String returnMsg, String apiPrefix, List<String> siteCodes, String pictureUrl) {
        List<AbstractDsfBetlog> videoGameBetlogs = new LinkedList<>();
        int i = 1;
        Long betId = GenerationSequenceUtil.genOrderNo();
        for (DsBetlogReponse it : recordList) {
            DsGameType gameTypeEnum = DsBetlogReponse.getGameTypeEnum(it.getGameType());
            String gameType = DsBetlogReponse.getGameType(it.getGameType());
            String hall = DsBetlogReponse.getHall(it.getHall());
            LocalDateTime gameTime = DateUtil.getDateTimeOfTimestamp(it.getEndTime());
            LocalDateTime createTime = DateUtil.current();
            String result = DsBetlogReponse.getBankerResult(it.getBankerResult(), gameTypeEnum);
            //new 一个子类对象
            DsfBetlogGameVideo betlog = new DsfBetlogGameVideo();
            betlog.setBetId(String.valueOf(betId + i));
            betlog.setDsfPlatformBetId(it.getId().toString());
            betlog.setDsfPlatformBetId(it.getId().toString());
            betlog.setCategoryName(PlatformType.VideoGame.title);
            betlog.setGamePlatform(GamePlatform.Ds.platformCode);
            betlog.setDsfPlayerId(it.getUserName());
            //判断属于哪个盘口
            siteCodes.forEach(siteCode -> {
                if (it.getUserName().startsWith(siteCode)) {
                    betlog.setSiteCode(siteCode);
                }
            });
            betlog.setGameNameCn(gameType);
            betlog.setTableName(it.getTableName());
            betlog.setIssueNo(it.getShoeInfoId() + ":" + it.getIssueNo());
            betlog.setStakeAmount(it.getStakeAmount());
            betlog.setValidStake(it.getValidStake());
            betlog.setWinLoss(it.getWinLoss());
            betlog.setJackpot(BigDecimal.ZERO);
            betlog.setCreateTime(createTime);
            betlog.setGameTime(gameTime);
            betlog.setResultPictureUrl(pictureUrl + "?" + it.getResultImgName());
            betlog.setRemark(hall);
            betlog.setGameResult(result);
            List<DsReportDetailReponse> details = it.getLiveMemberReportDetails();
            betlog.setItemList(details.stream().map(detail -> new DsfBetlogGameVideoItem(it.getId().toString(), DsReportDetailReponse.getBetTypeCn(detail.getBetType()),
                    DateUtil.getDateTimeOfTimestamp(detail.getBetTime()), detail.getBetAmount(),
                    detail.getWinLossAmount(), DateUtil.current(), DateUtil.current(), GamePlatform.Ds.platformCode)).collect(Collectors.toList()));
            videoGameBetlogs.add(betlog);
            i++;
        }
        return videoGameBetlogs;
    }

}
