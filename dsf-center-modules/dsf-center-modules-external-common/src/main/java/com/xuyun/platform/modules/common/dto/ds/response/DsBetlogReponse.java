package com.xuyun.platform.modules.common.dto.ds.response;

import com.xuyun.platform.modules.common.constants.DsConstants;
import com.xuyun.platform.modules.common.enums.DsGameType;
import com.xuyun.platform.modules.common.enums.DsHallType;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName: DsBetlogReponse
 * @Author: R.M.I
 * @CreateTime: 2019年03月02日 14:24:00
 * @Description: TODO
 * @Version 1.0.0
 */
@Data
public class DsBetlogReponse {
    private Long id;
    private Long sequenceNo;
    private String userName;
    private String gameType;
    private String hall;
    private String tableInfoId;
    private String shoeInfoId;
    private String tableName;
    private String issueNo;
    // 结果
    private String bankerResult;
    //投注
    private BigDecimal stakeAmount;

    private BigDecimal validStake;
    //佣金额
    private BigDecimal commAmount;

    private BigDecimal winLoss;

    private BigDecimal comm;

    private BigDecimal balanceAfter;

    private Long endTime;

    private String reportDate;

    private String ip;

    private String resultImgName;

    //Ds 子注单
    private List<DsReportDetailReponse> liveMemberReportDetails;



    public static String getGameType(String gameType) {
        return DsGameType.valueOf(gameType).getGameNameCn();
    }

    public static DsGameType getGameTypeEnum(String gameType) {
        return DsGameType.valueOf(gameType);
    }

    public static String getHall(String hall) {
        return DsHallType.valueOf(hall).getHallName();
    }

    public static String getBankerResult(String bankerResult,DsGameType gameTypeEnum) {
        if ("-1".equals(bankerResult)) {
            return "取消";
        }
        switch (gameTypeEnum) {
            case BACCARAT:
                //[1,0,2,9,1]~[[2,51,52],[5,19,21]]
                return baccaratResult(bankerResult);
            case DRAGON_TIGER:
                //[1,6,1,2,1,1]~[[2],[5]]
                return dragonTigerResult(bankerResult);
            case ROULETTE:
                return "点数" + bankerResult;
            case BACCARAT_INSURANCE:
                return baccaratInsuranceResult(bankerResult);
            case SICBO:
                return "三个骰点数" + bankerResult;
            case XOC_DIA:
                return xocDiaResult(bankerResult);
            case BULL_BULL:
                return bullBullResult(bankerResult);
        }
        return null;
    }


    //[7,13,1,12,0,4,50,0,0,51,0]~[[47],[32,26,30,35,21],[45,12,14,22,31],[28,15,7,50,29],[41,46,4,51,5]]
    private static String bullBullResult(String bankerResult) {
        String[] res = bankerResult.replaceAll("\\[", "").replaceAll("]", "")
                .replace("~", ",").split(",");
        String param1 = bullBullResultFirstParam(res[0], res[1], res[2], res[3], res[4], res[5], res[6], res[7], res[8], res[9], res[10]);
        String param2 = "头牌: " + bankerPokerParams(res[11]);
        String param3 = bankerPokerParams(res[12], res[13], res[14], res[15], res[16]);
        String param4 = bankerPokerParams(res[17], res[18], res[19], res[20], res[21]);
        String param5 = bankerPokerParams(res[22], res[23], res[24], res[25], res[26]);
        String param6 = bankerPokerParams(res[27], res[28], res[29], res[30], res[31]);
        return param1 + "~[" + param2 + "," + param3 + "," + param4 + "," + param5 + "," + param6 + "]";
    }

    private static String bullBullResultFirstParam(String... res) {
        String param1 = "庄: " + ("0".equals(res[0]) ? "没牛" : "11".equals(res[0]) ? "五公" : "牛" + res[0]);
        String param2 = "庄最大牌面: " + bankerPokerParams(res[1]);

        String param3 = "闲1: " + ("0".equals(res[2]) ? "没牛" : "11".equals(res[2]) ? "五公" : "牛" + res[2]);
        String param4 = "闲1最大牌面: " + bankerPokerParams(res[3]);
        String param5 = "闲1: " + ("0".equals(res[4]) ? "输" : "赢");

        String param6 = "闲2: " + ("0".equals(res[5]) ? "没牛" : "11".equals(res[5]) ? "五公" : "牛" + res[5]);
        String param7 = "闲2最大牌面: " + bankerPokerParams(res[6]);
        String param8 = "闲2: " + ("0".equals(res[7]) ? "输" : "赢");

        String param9 = "闲3: " + ("0".equals(res[8]) ? "没牛" : "11".equals(res[8]) ? "五公" : "牛" + res[8]);
        String param10 = "闲3最大牌面: " + bankerPokerParams(res[9]);
        String param11 = "闲3: " + ("0".equals(res[10]) ? "输" : "赢");
        return "[" + param1 + "," + param2 + "," + param3 + "," + param4 + "," + param5 + "," + param6 + "," + param7 + "," + param8 + "," + param9 + "," + param10 + "," + param11 + "]";
    }

    private static String xocDiaResult(String bankerResult) {
        String res = bankerResult.replaceAll("\\[", "").replaceAll("]", "");
        String param = "1".equals(res) ? "3白1红" : "2".equals(res) ? "2白2红" : "3".equals(res) ? "1白3红" : "全红";
        return "[" + param + "]";
    }


    private static String baccaratInsuranceResult(String bankerResult) {
        String[] res = bankerResult.replaceAll("\\[", "").replaceAll("]", "")
                .replace("~", "").split(",");
        String param1 = baccaratResultFirstParams(res[0], res[1], res[2], res[3], res[4], res[5]);
        String param2 = bankerPokerParams(res[6], res[7], res[8]);
        String param3 = bankerPokerParams(res[9], res[10], res[11]);
        return param1 + "~[" + param2 + "," + param3 + "]";
    }


    private static String dragonTigerResult(String bankerResult) {
        String[] res = bankerResult.replaceAll("\\[", "").replaceAll("]", "")
                .replace("~", ",").split(",");
        String param1 = dragonTigerResultFirstParams(res[0], res[1], res[2], res[3], res[4], res[5]);
        String param2 = bankerPokerParams(res[6], res[7]);
        return param1 + "~[" + param2 + "]";
    }

    private static String dragonTigerResultFirstParams(String... params) {
        StringBuffer res = new StringBuffer("[");
        res.append("1".equals(params[0]) ? "虎赢," : "2".equals(params[0]) ? "龙赢," : "和赢,");
        res.append("赢家的点数: " + params[1] + ",");
        res.append("1".equals(params[2]) ? "龙单," : "龙双,");
        res.append("1".equals(params[3]) ? "虎单," : "虎双,");
        res.append("1".equals(params[4]) ? "龙红," : "龙黑,");
        res.append("1".equals(params[5]) ? "虎红" : "虎黑");
        res.append("]");
        return res.toString();
    }

    /**
     * [1,0,2,9,1]~[[2,51,52],[5,19,21]]
     * 5,3,3
     *
     * @param bankerResult
     * @return
     */
    private static String baccaratResult(String bankerResult) {
//        String[] res = bankerResult.replaceAll("\\[", "").replaceAll("]", "")
//                .replace("~", ",").split(",");
        String[] res = bankerResult.split("~");
        String[] res1 =res[0].replaceAll("\\[", "").replaceAll("]", "").split(",");
        String param1 = baccaratResultFirstParams(res1[0], res1[1], res1[2], res1[3], res1[4]);
        String[] res2 =res[1].split("],\\[");

        String param2 = bankerPokerParams(res2[0].replaceAll("\\[", "").replaceAll("]", "").split(","));
        String param3 = bankerPokerParams(res2[1].replaceAll("\\[", "").replaceAll("]", "").split(","));
        return param1 + "~[" + param2 + "," + param3 + "]";
    }

    private static String bankerPokerParams(String... params) {
        StringBuffer res = new StringBuffer("[");
        List<Integer> pokerNumber = Arrays.stream(params).map(it -> Integer.valueOf(it)).collect(Collectors.toList());
        res.append(DsConstants.getPoker(pokerNumber)).append("]");
        return res.toString();
    }

    private static String baccaratResultFirstParams(String... params) {
        StringBuffer res = new StringBuffer("[");
        res.append("1".equals(params[0]) ? "庄," : "2".equals(params[0]) ? "闲," : "和,");
        res.append("0".equals(params[1]) ? "没有对子," : "1".equals(params[1]) ? "庄对," : "2".equals(params[1]) ? "闲对" : "庄对闲对,");
        res.append("1".equals(params[2]) ? "大," : "小,");
        res.append("赢家的点数: " + params[3] + ",");
        if (params.length == 5) {
            res.append("0".equals(params[4]) ? "无对子" : "1".equals(params[4]) ? "任意对子" : "完美对子");
        } else if (params.length == 6) {
            res.append("庄家保险: ");
            res.append("0".equals(params[4]) ? "没有保险," : "1".equals(params[4]) ? "赢," : "2".equals(params[4]) ? "输," : "和,");
            res.append("闲家保险: ");
            res.append("0".equals(params[5]) ? "没有保险" : "1".equals(params[5]) ? "赢" : "2".equals(params[5]) ? "输" : "和");
        }
        res.append("]");
        return res.toString();
    }
}
