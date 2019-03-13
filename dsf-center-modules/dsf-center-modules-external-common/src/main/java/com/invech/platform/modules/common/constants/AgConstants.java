package com.invech.platform.modules.common.constants;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName: AgConstants
 * @Author: R.M.I
 * @CreateTime: 2019年02月25日 22:11:00
 * @Description: TODO
 * @Version 1.0.0
 */
public class AgConstants {

    public static Map<String, String> AGIN_VIDEO_GAMES;

    static {
        Map<String, String> map = new HashMap<>();
        map.put("BAC", "百家乐");
        map.put("CBAC", "包桌百家乐");
        map.put("LINK", "多台");
        map.put("DT", "龙虎");
        map.put("SHB", "骰宝");
        map.put("ROU", "轮盘");
        map.put("FT", "番摊");
        map.put("LBAC", "竞咪百家乐");
        map.put("ULPK", "终极德州扑克");
        map.put("SBAC", "保险百家乐");
        map.put("NN", "牛牛");
        map.put("BJ", "21 点");
        map.put("ZJH", "炸金花");
        map.put("BF", "斗牛");
        AGIN_VIDEO_GAMES = Collections.unmodifiableMap(map);
    }

    public static Map<String, String> AGIN_ROUND;

    static {
        Map<String, String> map = new HashMap<>();
        map.put("DSP","国际厅");
        map.put("AGQ","旗舰厅");
        map.put("VIP","包桌厅");
        map.put("LED","竞咪厅");
        map.put("AGHH","豪华厅");
        map.put("SLOT","电子游戏");
        map.put("EMA","欧洲厅");
        map.put("AGNW","新世界厅");
        AGIN_ROUND = Collections.unmodifiableMap(map);
    }

    public static Map<String, String> AGIN_PLAY_TYPE;
    static {
        Map<String,String> playType = new HashMap<>();
        playType.put("1","庄");
        playType.put("2","闲");
        playType.put("3","和");
        playType.put("4","庄对");
        playType.put("5","闲对");
        playType.put("6","大");
        playType.put("7","小");
        playType.put("8","庄保险");
        playType.put("9","闲保险");
        playType.put("11","庄免佣");
        playType.put("12","庄龙宝");
        playType.put("13","闲龙宝");
        playType.put("14","超级六");
        playType.put("15","任意对子");
        playType.put("16","完美对子");
        playType.put("21","龙");
        playType.put("22","虎");
        playType.put("23","和（龙虎）");
        playType.put("41","大(big)");
        playType.put("42","小(small)");
        playType.put("43","单(odd)");
        playType.put("44","双(even)");
        playType.put("45","全围(allwei)");
        playType.put("46","围1(wei1)");
        playType.put("47","围2(wei2)");
        playType.put("48","围3(wei3)");
        playType.put("49","围4(wei4)");
        playType.put("50","围5(wei5)");
        playType.put("51","围6(wei6)");
        playType.put("52","单点1(single1)");
        playType.put("53","单点2(single2)");
        playType.put("54","单点3(single3)");
        playType.put("55","单点4(single4)");
        playType.put("56","单点5(single5)");
        playType.put("57","单点6(single6)");
        playType.put("58","对子1(double1)");
        playType.put("59","对子2(double2)");
        playType.put("60","对子3(double3)");
        playType.put("61","对子4(double4)");
        playType.put("62","对子5(double5)");
        playType.put("63","对子6(double6)");
        playType.put("64","组合12(combine12)");
        playType.put("65","组合13(combine13)");
        playType.put("66","组合14(combine14)");
        playType.put("67","组合15(combine15)");
        playType.put("68","组合16(combine16)");
        playType.put("69","组合23(combine23)");
        playType.put("70","组合24(combine24)");
        playType.put("71","组合25(combine25)");
        playType.put("72","组合26(combine26)");
        playType.put("73","组合34(combine34)");
        playType.put("74","组合35(combine35)");
        playType.put("75","组合36(combine36)");
        playType.put("76","组合45(combine45)");
        playType.put("77","组合46(combine46)");
        playType.put("78","组合56(combine56)");
        playType.put("79","和值4(sum4)");
        playType.put("80","和值5(sum5)");
        playType.put("81","和值6(sum6)");
        playType.put("82","和值7(sum7)");
        playType.put("83","和值8(sum8)");
        playType.put("84","和值9(sum9)");
        playType.put("85","和值10(sum10)");
        playType.put("86","和值11(sum11)");
        playType.put("87","和值12(sum12)");
        playType.put("88","和值13(sum13)");
        playType.put("89","和值14(sum14)");
        playType.put("90","和值15(sum15)");
        playType.put("91","和值16(sum16)");
        playType.put("92","和值17(sum17)");
        playType.put("101","直接注");
        playType.put("102","分注");
        playType.put("103","街注");
        playType.put("104","三数");
        playType.put("105","4个号码");
        playType.put("106","角注");
        playType.put("107","列注(列1)");
        playType.put("108","列注(列2)");
        playType.put("109","列注(列3)");
        playType.put("110","线注");
        playType.put("111","打一");
        playType.put("112","打二");
        playType.put("123","打三");
        playType.put("114","红");
        playType.put("115","黑");
        playType.put("116","大");
        playType.put("117","小");
        playType.put("118","单");
        playType.put("119","双");
        playType.put("130","1番");
        playType.put("131","2番");
        playType.put("132","3番");
        playType.put("133","4番");
        playType.put("134","1念2");
        playType.put("135","1念3");
        playType.put("136","1念4");
        playType.put("137","2念1");
        playType.put("138","2念3");
        playType.put("139","2念4");
        playType.put("140","3念1");
        playType.put("141","3念2");
        playType.put("142","3念4");
        playType.put("143","4念1");
        playType.put("144","4念2");
        playType.put("145","4念3");
        playType.put("146","角(1,2)");
        playType.put("147","单");
        playType.put("148","角(1,4)");
        playType.put("149","角(2,3)");
        playType.put("150","双");
        playType.put("151","角(3,4)");
        playType.put("152","1,2四通");
        playType.put("153","1,2三通");
        playType.put("154","1,3四通");
        playType.put("155","1,3二通");
        playType.put("156","1,4三通");
        playType.put("157","1,4二通");
        playType.put("158","2,3四通");
        playType.put("159","2,3一通");
        playType.put("160","2,4三通");
        playType.put("161","2,4一通");
        playType.put("162","3,4二通");
        playType.put("163","3,4一通");
        playType.put("164","三门(3,2,1)");
        playType.put("165","三门(2,1,4)");
        playType.put("166","三门(1,4,3)");
        playType.put("167","三门(4,3,2)");
        playType.put("180","底注+盲注");
        playType.put("181","一倍加注");
        playType.put("182","二倍加注");
        playType.put("183","三倍加注");
        playType.put("184","四倍加注");
        playType.put("211","闲1平倍");
        playType.put("212","闲1翻倍");
        playType.put("213","闲2平倍");
        playType.put("214","闲2翻倍");
        playType.put("215","闲3平倍");
        playType.put("216","闲3翻倍");
        playType.put("207","庄1平倍");
        playType.put("208","庄1翻倍");
        playType.put("209","庄2平倍");
        playType.put("210","庄2翻倍");
        playType.put("217","庄3平倍");
        playType.put("218","庄3翻倍");
        playType.put("220","底注");
        playType.put("221","分牌");
        playType.put("222","保险");
        playType.put("223","分牌保险");
        playType.put("224","加注");
        playType.put("225","分牌加注");
        playType.put("226","完美对子");
        playType.put("227","21+3");
        playType.put("228","旁注");
        playType.put("229","旁注分牌");
        playType.put("230","旁注保险");
        playType.put("231","旁注分牌保险");
        playType.put("232","旁注加注");
        playType.put("233","旁注分牌加注");
        playType.put("260","龙");
        playType.put("261","凤");
        playType.put("262","对8以上");
        playType.put("263","同花");
        playType.put("264","顺子");
        playType.put("265","豹子");
        playType.put("266","同花顺");
        playType.put("270","黑牛");
        playType.put("271","红牛");
        playType.put("272","和");
        playType.put("273","牛一");
        playType.put("274","牛二");
        playType.put("275","牛三");
        playType.put("276","牛四");
        playType.put("277","牛五");
        playType.put("278","牛六");
        playType.put("279","牛七");
        playType.put("280","牛八");
        playType.put("281","牛九");
        playType.put("282","牛牛");
        playType.put("283","双牛牛");
        playType.put("284","银牛/金牛/炸弹/五小牛");
        AGIN_PLAY_TYPE = Collections.unmodifiableMap(playType);
    }
    // 接口函数专用关键字
    public static final String AGIN_FUN_CHECKORCREATEGAMEACCOUT = "lg";//检测并创建游戏账号
    public static final String AGIN_FUN_PREPARETRANSFERCREDIT = "tc";//预备转账
    public static final String AGIN_FUN_TRANSFERCREDITCONFIRM = "tcc";//转账确认
    public static final String AGIN_FUN_GETBALANCE = "gb";//余额查找
    public static final String AGIN_FUN_QUERYORDERSTATUS = "qos";//状态查询
    public static final String AGIN_M_DOBUSINESS = "doBusiness.do?";
    public static final String AGIN_M_FORWARDGAME = "forwardGame.do?";

    public static final String AGIN_FUN_SUCC_KEY = "0";
    public static final String AGIN_FUN_UNKNOW_KEY = "1";
    public static final String AGIN_FUN_FAILED_KEY = "2";
    //public static final String AGIN_SEQUENCE="none";

    public static final String AGIN_DESCODE_KEY = "desCode";

    public static final int FTP_READ_SIZE = 300;

    public interface Actype {
        int trueAccount = 1;// 真钱账户
        int testAccount = 0;// 试玩账号
    }

    public interface OddTpye {
        // 人民币下注范围
        String OddA = "A";// 20-50000
        String OddB = "B";// 50-5000
        String OddC = "C";// 20-10000
        String OddD = "D";// 200-20000
        String OddE = "E";// 300-30000
        String OddF = "F";// 400-40000
        String OddG = "G";// 500-50000
        String OddH = "H";// 1000-100000
        String OddI = "I";// 2000-200000
    }

    public interface CurTpye {
        // 币种
        String curCny = "CNY";// 人民币
        String curKrw = "KRW";// 韩元
        String curMyr = "MYR";// 马来西亚币
        String curUsd = "USD";// 美元
        String curJpy = "JPY";// 日元
        String curThb = "THB";// 泰铢
        String curBtc = "BTC";// 比特币
        String curIdr = "IDR";// 印尼盾
        String curVnd = "VND";// 越南盾
        String curEur = "EUR";// 欧元
        String curInr = "INR";// 印度卢比
    }

    public interface RetCode {
        int succ_code = 0;// 成功
        int fail_code = 1;// 失败 , 订单未处理状态
        int inv_code = 2;// 无效
    }

    public interface LangCode {
        // 语言
        int cn_code = 1;// zh-cn (简体中文)
        int tw_code = 2;// zh-tw (䌓体中文）
        int us_code = 3;// en-us(英语)
        int jp_code = 4;// euc-jp(日语) 4
    }

    /**
     * 预备转账 返回结果
     */
    public interface PPreTrf {
        int fail = 0; //失败
        int suc = 1; //成功
    }

    public interface Res {
        String succ = "0";//成功
        String fail = "1";//失败，订单处理中
        String net_err = "network_error";//网络问题导致资料遗失
        String error = "error";//转账错误, 参看 msg 错误信息描述
    }

}
