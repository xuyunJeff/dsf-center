package com.invech.platform.modules.ag.serviceImpl;

import com.alibaba.fastjson.JSON;
import com.invech.platform.dsfcenterdao.service.BetDaoService;
import com.invech.platform.dsfcenterdata.AbstractBetlogService;
import com.invech.platform.dsfcenterdata.betlog.*;
import com.invech.platform.dsfcenterdata.constants.ApiConstants;
import com.invech.platform.dsfcenterdata.entity.DsfGmApi;
import com.invech.platform.dsfcenterdata.entity.DsfGmGame;
import com.invech.platform.dsfcenterdata.enums.GamePlatform;
import com.invech.platform.dsfcenterdata.enums.PlatformType;
import com.invech.platform.dsfcenterdata.utils.DateUtil;
import com.invech.platform.dsfcenterdata.utils.GenerationSequenceUtil;
import com.invech.platform.dsfcenterdata.utils.StringUtils;
import com.invech.platform.modules.ag.http.AgFtpNetWork;
import com.invech.platform.modules.common.constants.AgConstants;
import com.invech.platform.modules.common.dto.ag.AgBetBRDto;
import com.invech.platform.modules.common.dto.ag.AgBetGRDto;
import com.invech.platform.modules.common.dto.ag.AgBetHSRDto;
import com.invech.platform.modules.common.dto.ag.AgBetRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 * @ClassName: AgBetlogServiceImpl
 * @Author: R.M.I
 * @CreateTime: 2019年03月08日 21:25:00
 * @Description: TODO
 * @Version 1.0.0
 */
@Service("AgBetlogService")
@Slf4j
public class AgBetlogServiceImpl extends AbstractBetlogService {

    @Autowired
    AgFtpNetWork agFtpNetWork;

    @Autowired
    BetDaoService betDaoService;


    private final int timeEquation = 12 * 60 * 60 * 1000;



    @Override
    public DsfBetlogRedisDto videoGame(BetTaskRequestParams betTaskRequestParams, List<String> siteCode, String returnMsg) {
        returnMsg += "\n 开始 ag  type = AGIN 数据下载 开始时间 : betExtraKey =" + betTaskRequestParams.getBetExtraKey();
        Map<String, String> agFileStringMap = this.getAgBetRequest(betTaskRequestParams, "AGIN", PlatformType.VideoGame);
        List<AgBetGRDto> agBets = new LinkedList<>();
        for (String fileStr : agFileStringMap.keySet()) {
           AgConstants.AGIN_VIDEO_GAMES.keySet().forEach(it ->{
                //只有在捕鱼王场景下的投注记录才写入日志表
                if (agFileStringMap.get(fileStr).contains(it)){
                    /**将数据转换成MAP**/
                    String[] strs = agFileStringMap.get(fileStr).split("\" ");
                    Map map = new HashMap();
                    for (String s : strs) {
                        String[] str = s.trim().split("=");
                        if (str.length == 2) {
                            map.put(str[0], str[1].replace("\"", ""));
                        }
                    }
                    AgBetGRDto model = JSON.parseObject(JSON.toJSONString(map), AgBetGRDto.class);
                    agBets.add(model);
                }
            });

            String mintus = fileStr.replace(".xml", "");
            /**
             *  存到redis 北京時間,进入后自动减12小时
             * */
            String redisKey = betTaskRequestParams.getDsfGmApi().getApiName() + ":" + PlatformType.ComputerGame.code;
            long timestamp = DateUtil.parse(mintus, DateUtil.FORMAT_12_DATE_TIME_2).getTime() + timeEquation;
            if (timestamp > Long.valueOf(betTaskRequestParams.getBetExtraKey()) && betTaskRequestParams.isRedisUpdate()) {
                betTaskRequestParams.setBetExtraKey(String.valueOf(timestamp));
                betDaoService.putBetRedisKey(redisKey, betTaskRequestParams);
            }
        }
        returnMsg += "\n ag " + "type =  AGIN 获取 size = " + agBets.size();
        if (agBets.size() > 0) {
            List<AbstractDsfBetlog> dsfFishBets = this.handlerVideoBets(siteCode, agBets);
            return new DsfBetlogRedisDto(dsfFishBets, betTaskRequestParams, returnMsg);
        }
        return new DsfBetlogRedisDto(Collections.emptyList(), betTaskRequestParams, returnMsg);
    }

    private List<AbstractDsfBetlog> handlerVideoBets(List<String> siteCodes, List<AgBetGRDto> agBets) {
        List<AbstractDsfBetlog> computerGameBetlogs = new LinkedList<>();
        int i = 1;
        Long betId = GenerationSequenceUtil.genOrderNo();
        for (AgBetGRDto it : agBets) {
            DsfBetlogGameVideo bet = new DsfBetlogGameVideo();
            bet.setDsfPlatformBetId(it.getBillNo().toString());
            bet.setBetId(String.valueOf(betId+ i));
            //判断属于哪个盘口
            siteCodes.forEach(siteCode -> {
                if (it.getPlayerName().startsWith(siteCode)) {
                    bet.setSiteCode(siteCode);
                }
            });
            bet.setGamePlatform(GamePlatform.Agin.platformCode);
            bet.setDsfPlayerId(it.getPlayerName());
            bet.setCreateTime(DateUtil.current());
            bet.setGameTime(DateUtil.getDateTimeOfTimestamp(it.getBetTime().getTime()+timeEquation));
            bet.setWinLoss(it.getNetAmount());
            bet.setStakeAmount(it.getBetAmount());
            String gameName = AgConstants.AGIN_VIDEO_GAMES.get(it.getGameType());
            bet.setGameNameCn(gameName==null?ApiConstants.UNAVAILABLE_GAME_NAME:gameName);
            bet.setCategoryName(PlatformType.VideoGame.title);
            bet.setJackpot(BigDecimal.ZERO);
            bet.setValidStake(it.getValidBetAmount());
            bet.setIssueNo(it.getBillNo().toString());
            bet.setTableName(AgConstants.AGIN_ROUND.get(it.getRound())+":"+it.getTableCode());
            DsfBetlogGameVideoItem  item= new DsfBetlogGameVideoItem(it.getBillNo().toString(),AgConstants.AGIN_PLAY_TYPE.get(it.getPlayType()),bet.getGameTime(),bet.getStakeAmount(),bet.getWinLoss(),DateUtil.current(),DateUtil.current(),
                    bet.getGamePlatform());
            bet.setItemList(Collections.singletonList(item));
            computerGameBetlogs.add(bet);
            i ++ ;
        }
        return computerGameBetlogs;
    }


    @Override
    public DsfBetlogRedisDto computerGame(BetTaskRequestParams betTaskRequestParams, List<String> siteCode, String returnMsg) {
        BetTaskRequestParams betTaskRequestParams1 = null;
        BetTaskRequestParams betTaskRequestParams2 = null;
        BetTaskRequestParams betTaskRequestParams3 = null;
        try {
            betTaskRequestParams1 = betTaskRequestParams.clone();
            betTaskRequestParams2 = betTaskRequestParams.clone();
            betTaskRequestParams3 = betTaskRequestParams.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        List<AbstractDsfBetlog> dsfBetlogs = new CopyOnWriteArrayList<>();
        returnMsg += "\n 开始 ag  type = AGIN 数据下载: betExtraKey =" + betTaskRequestParams.getBetExtraKey();
        Map<String, String> agFileStringMap = this.getAgBetRequest(betTaskRequestParams1, "AGIN", PlatformType.ComputerGame);
        dsfBetlogs.addAll(this.computerGameEBR(betTaskRequestParams1, siteCode, returnMsg, agFileStringMap, "AGIN"));
        returnMsg += "\n ag " + "type =  AGIN 获取 size = " + dsfBetlogs.size();
        returnMsg += "\n 开始 ag type =  XIN 数据下载";
        Map<String, String> xinFileStringMap = this.getAgBetRequest(betTaskRequestParams2, "XIN", PlatformType.ComputerGame);
        List<AbstractDsfBetlog> dsfBetlogs2 =this.computerGameEBR(betTaskRequestParams2, siteCode, returnMsg, xinFileStringMap, "XIN");
        dsfBetlogs.addAll(dsfBetlogs2);
        returnMsg += "\n ag type = XIN 获取 size = " + dsfBetlogs2.size();
        returnMsg += "\n 开始 ag  type = YOPLAY 数据下载";
        Map<String, String> yoplayFileStringMap = this.getAgBetRequest(betTaskRequestParams3, "YOPLAY", PlatformType.ComputerGame);
        List<AbstractDsfBetlog> dsfBetlogs3 =this.computerGameEBR(betTaskRequestParams3, siteCode, returnMsg, yoplayFileStringMap, "YOPLAY");
        returnMsg += "\n ag type = YOPLAY 获取 size = " + dsfBetlogs3.size();
        dsfBetlogs.addAll(dsfBetlogs3);
        return new DsfBetlogRedisDto(dsfBetlogs, betTaskRequestParams, returnMsg);
    }



    private List<AbstractDsfBetlog> computerGameEBR(BetTaskRequestParams betTaskRequestParams, List<String> siteCode, String returnMsg, Map<String, String> fileStringMap, String agbetType) {

        List<AgBetBRDto> agBets = new LinkedList<>();
        for (String fileStr : fileStringMap.keySet()) {
            //只有在电子游戏的下的投注记录才写入日志表
            if (fileStringMap.get(fileStr).contains("EBR") || fileStringMap.get(fileStr).contains("YOPLAY")) {
                /**将数据转换成MAP**/
                String[] strs = fileStringMap.get(fileStr).split("\" ");
                Map map = new HashMap();
                for (String s : strs) {
                    String[] str = s.trim().split("=");
                    if (str.length == 2) {
                        map.put(str[0], str[1].replace("\"", ""));
                    }
                }
                AgBetBRDto model = JSON.parseObject(JSON.toJSONString(map), AgBetBRDto.class);
                agBets.add(model);
            }
            String mintus = fileStr.replace(".xml", "");
            /**
             *  存到redis 北京時間,进入后自动减12小时
             * */
            long timestamp = DateUtil.parse(mintus, DateUtil.FORMAT_12_DATE_TIME_2).getTime() + timeEquation;
            String redisKey = betTaskRequestParams.getDsfGmApi().getApiName() + ":" + PlatformType.ComputerGame.code + ":" + agbetType;
            if (timestamp > Long.valueOf(betTaskRequestParams.getBetExtraKey()) && betTaskRequestParams.isRedisUpdate()) {
                betTaskRequestParams.setBetExtraKey(String.valueOf(timestamp));
                betDaoService.putBetRedisKey(redisKey, betTaskRequestParams);
            }
        }
        if (agBets.size() > 0) {
            List<AbstractDsfBetlog> dsfComputerBets = this.handlerEBRBets(siteCode, agBets);

            return dsfComputerBets;
        }
        return Collections.emptyList();
    }

    private List<AbstractDsfBetlog> handlerEBRBets(List<String> siteCodes, List<AgBetBRDto> agBets) {
        List<String> gameTypes = agBets.stream().map(it -> it.getGameType()).collect(Collectors.toList());
        List<DsfGmGame> gmGameList = betDaoService.gameListAg(gameTypes);
        List<AbstractDsfBetlog> computerGameBetlogs = new LinkedList<>();
        int i = 1;
        Long betId = GenerationSequenceUtil.genOrderNo();
        for (AgBetBRDto it : agBets) {
            DsfBetlogGameComputer bet = new DsfBetlogGameComputer();
            bet.setGamePlatform(GamePlatform.Agin.platformCode);
            bet.setBetId(String.valueOf(betId + i));
            bet.setDsfPlatformBetId(it.getMainbillno().toString());
            bet.setCategoryName(PlatformType.ComputerGame.title);
            bet.setDsfPlayerId(it.getPlayerName());
            //判断属于哪个盘口
            siteCodes.forEach(siteCode -> {
                if (it.getPlayerName().startsWith(siteCode)) {
                    bet.setSiteCode(siteCode);
                }
            });
            bet.setWinLoss(it.getNetAmount());
            bet.setCreateTime(DateUtil.current());
            bet.setGameTime(DateUtil.getDateTimeOfTimestamp(it.getBetTime().getTime()+timeEquation));
            bet.setValidStake(it.getValidBetAmount());
            bet.setStakeAmount(it.getBetAmount());
            String gameName = null;
            try {
                gameName = gmGameList.stream().filter(item -> item.getGameParam().replace(" ", "").equals(it.getGameType())).findFirst().get().getGameName();
            } catch (Exception e) {
                gameName = ApiConstants.UNAVAILABLE_GAME_NAME;
            }
            bet.setGameNameCn(gameName);
            bet.setJackpot(it.getNetAmountBonus());
            bet.setGameResult(it.getResult());
            bet.setIssueNo(it.getBillNo().toString());
            computerGameBetlogs.add(bet);
        }
        return computerGameBetlogs;
    }


    @Override
    public DsfBetlogRedisDto fishGame(BetTaskRequestParams betTaskRequestParams, List<String> siteCode, String returnMsg) {
        returnMsg += "\n 开始 ag  type = HUNTER 数据下载: betExtraKey =" + betTaskRequestParams.getBetExtraKey();
        Map<String, String> fileStringMap = this.getAgBetRequest(betTaskRequestParams, "HUNTER", PlatformType.Fish);
        List<AgBetHSRDto> agBets = new LinkedList<>();
        for (String fileStr : fileStringMap.keySet()) {
            //只有在捕鱼王场景下的投注记录才写入日志表
            if (fileStringMap.get(fileStr).contains("HSR")) {
                /**将数据转换成MAP**/
                String[] strs = fileStringMap.get(fileStr).split("\" ");
                Map map = new HashMap();
                for (String s : strs) {
                    String[] str = s.trim().split("=");
                    if (str.length == 2) {
                        map.put(str[0], str[1].replace("\"", ""));
                    }
                }
                AgBetHSRDto model = JSON.parseObject(JSON.toJSONString(map), AgBetHSRDto.class);
                /***统计只写入投注注单**/
                if (model.getType() != 1) {
                    continue;
                }
                agBets.add(model);

            }
            String mintus = fileStr.replace(".xml", "");
            /**
             *  存到redis 北京時間,进入后自动减12小时
             * */
            String redisKey = betTaskRequestParams.getDsfGmApi().getApiName() + ":" + PlatformType.ComputerGame.code;
            long timestamp = DateUtil.parse(mintus, DateUtil.FORMAT_12_DATE_TIME_2).getTime() + timeEquation;
            if (timestamp > Long.valueOf(betTaskRequestParams.getBetExtraKey()) && betTaskRequestParams.isRedisUpdate()) {
                betTaskRequestParams.setBetExtraKey(String.valueOf(timestamp));
                betDaoService.putBetRedisKey(redisKey, betTaskRequestParams);
            }
        }
        returnMsg += "\n ag type = HUNTER 获取 size = " + agBets.size();
        if (agBets.size() > 0) {
            List<AbstractDsfBetlog> dsfFishBets = this.handlerHunterBets(siteCode, agBets);
            return new DsfBetlogRedisDto(dsfFishBets, betTaskRequestParams, returnMsg);
        }
        return new DsfBetlogRedisDto(Collections.emptyList(), betTaskRequestParams, returnMsg);
    }


    private List<AbstractDsfBetlog> handlerHunterBets(List<String> siteCodes, List<AgBetHSRDto> agBets) {
        List<AbstractDsfBetlog> fishGameBetlogs = new LinkedList<>();
        int i = 1;
        Long betId = GenerationSequenceUtil.genOrderNo();
        for (AgBetHSRDto it : agBets) {
            DsfBetlogGameFish bet = new DsfBetlogGameFish();
            bet.setGamePlatform(GamePlatform.Agin.platformCode);
            bet.setBetId(String.valueOf(betId + i));
            bet.setDsfPlatformBetId(it.getId());
            bet.setCategoryName(PlatformType.Fish.title);
            bet.setDsfPlayerId(it.getPlayerName());
            bet.setTableName(it.getRoomid());
            //判断属于哪个盘口
            siteCodes.forEach(siteCode -> {
                if (it.getPlayerName().startsWith(siteCode)) {
                    bet.setSiteCode(siteCode);
                }
            });
            String gameName = Integer.valueOf(it.getRoomid().split("-")[1]) > 2000 ? "Ag捕鱼王2D" : "Ag捕鱼王3D";
            bet.setGameNameCn(gameName);
            bet.setIssueNo(it.getSceneId().toString());
            bet.setGameTime(DateUtil.getDateTimeOfTimestamp(it.getCreationTime().getTime() + timeEquation));
            bet.setWinLoss(it.getTransferAmount());
            bet.setStakeAmount(it.getCost());
            bet.setJackpot(it.getJackpotcomm());
            bet.setValidStake(it.getCost());
            bet.setPlatformType(PlatformType.Fish);
            bet.setCreateTime(DateUtil.current());
            fishGameBetlogs.add(bet);

        }
        return fishGameBetlogs;
    }


    /**
     * ag 获取ftp 服务器文件
     * <p>
     * 每日次最多读取300个文件
     *
     * @param betTaskRequestParams
     * @param agBetType
     * @return
     */
    @Async
    public Map<String, String> getAgBetRequest(BetTaskRequestParams betTaskRequestParams, String agBetType, PlatformType platformType) {
        DsfGmApi api = betTaskRequestParams.getDsfGmApi();
        Map<String, Object> secureCodeMap = DsfGmApi.getSecureCodeMap(api.getSecureCode());
        Long startTime = DateUtil.currentTimestamp() - timeEquation;
        if (StringUtils.isEmpty(betTaskRequestParams.getBetExtraKey())) {
            BetTaskRequestParams params = betDaoService.getBetRedisKey(api.getApiName() + ":" + platformType.code + ":" + agBetType);
            if (params != null) {
                startTime = Long.valueOf(params.getBetExtraKey()) - timeEquation;
            }
        } else {
            startTime = Long.valueOf(betTaskRequestParams.getBetExtraKey()) - timeEquation;
        }
        betTaskRequestParams.setBetExtraKey(startTime.toString());
        AgBetRequest request = new AgBetRequest(secureCodeMap.get("ftpUrl"),
                secureCodeMap.get("ftpAccount"),
                secureCodeMap.get("ftpPassword"),
                "/" + agBetType + "/",
                api,
                startTime);
        return agFtpNetWork.downFile(request);
    }

}
