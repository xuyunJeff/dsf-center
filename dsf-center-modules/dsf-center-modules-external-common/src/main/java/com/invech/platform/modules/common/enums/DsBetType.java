package com.invech.platform.modules.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @ClassName: DsBetType
 * @Author: R.M.I
 * @CreateTime: 2019年03月02日 14:42:00
 * @Description: TODO
 * @Version 1.0.0
 */
@Getter
@AllArgsConstructor
public enum DsBetType {
    BC_BANKER("庄", DsGameType.BACCARAT),
    BC_BANKER_NC("庄免佣", DsGameType.BACCARAT),
    BC_PLAYER("闲", DsGameType.BACCARAT),
    BC_TIE("和", DsGameType.BACCARAT),
    BC_BANKER_PAIR("庄对", DsGameType.BACCARAT),
    BC_PLAYER_PAIR("闲对", DsGameType.BACCARAT),
    BC_ARBITRARILY_PAIR("任意对子", DsGameType.BACCARAT),
    BC_PERFECT_PAIR("完美对子", DsGameType.BACCARAT),
    BC_BANKER_SUPER_SIX("超级六点", DsGameType.BACCARAT),
    BC_BIG("大", DsGameType.BACCARAT),
    BC_SMALL("小", DsGameType.BACCARAT),
    BC_BANKER_INSURANCE("庄保险", DsGameType.BACCARAT),
    BC_PLAYER_INSURANCE("闲保险", DsGameType.BACCARAT),
    RL_0("直注: 0", DsGameType.ROULETTE),
    RL_1("直注: 1", DsGameType.ROULETTE),
    RL_2("直注: 2", DsGameType.ROULETTE),
    RL_3("直注: 3", DsGameType.ROULETTE),
    RL_4("直注: 4", DsGameType.ROULETTE),
    RL_5("直注: 5", DsGameType.ROULETTE),
    RL_6("直注: 6", DsGameType.ROULETTE),
    RL_7("直注: 7", DsGameType.ROULETTE),
    RL_8("直注: 8", DsGameType.ROULETTE),
    RL_9("直注: 9", DsGameType.ROULETTE),
    RL_10("直注: 10", DsGameType.ROULETTE),
    RL_11("直注: 11", DsGameType.ROULETTE),
    RL_12("直注: 12", DsGameType.ROULETTE),
    RL_13("直注: 13", DsGameType.ROULETTE),
    RL_14("直注: 14", DsGameType.ROULETTE),
    RL_15("直注: 15", DsGameType.ROULETTE),
    RL_16("直注: 16", DsGameType.ROULETTE),
    RL_17("直注: 17", DsGameType.ROULETTE),
    RL_18("直注: 18", DsGameType.ROULETTE),
    RL_19("直注: 19", DsGameType.ROULETTE),
    RL_20("直注: 20", DsGameType.ROULETTE),
    RL_21("直注: 21", DsGameType.ROULETTE),
    RL_22("直注: 22", DsGameType.ROULETTE),
    RL_23("直注: 23", DsGameType.ROULETTE),
    RL_24("直注: 24", DsGameType.ROULETTE),
    RL_25("直注: 25", DsGameType.ROULETTE),
    RL_26("直注: 26", DsGameType.ROULETTE),
    RL_27("直注: 27", DsGameType.ROULETTE),
    RL_28("直注: 28", DsGameType.ROULETTE),
    RL_29("直注: 29", DsGameType.ROULETTE),
    RL_30("直注: 30", DsGameType.ROULETTE),
    RL_31("直注: 31", DsGameType.ROULETTE),
    RL_32("直注: 32", DsGameType.ROULETTE),
    RL_33("直注: 33", DsGameType.ROULETTE),
    RL_34("直注: 34", DsGameType.ROULETTE),
    RL_35("直注: 35", DsGameType.ROULETTE),
    RL_36("直注: 37", DsGameType.ROULETTE),
    RL_0_1("分注: 0,1", DsGameType.ROULETTE),
    RL_0_2("分注: 0,2", DsGameType.ROULETTE),
    RL_0_3("分注: 0,3", DsGameType.ROULETTE),
    RL_1_2("分注: 1,2", DsGameType.ROULETTE),
    RL_1_4("分注: 1,4", DsGameType.ROULETTE),
    RL_2_3("分注: 2,3", DsGameType.ROULETTE),
    RL_2_5("分注: 2,5", DsGameType.ROULETTE),
    RL_3_6("分注: 3,6", DsGameType.ROULETTE),
    RL_4_5("分注: 4,5", DsGameType.ROULETTE),
    RL_4_7("分注: 4,7", DsGameType.ROULETTE),
    RL_5_6("分注: 5,6", DsGameType.ROULETTE),
    RL_5_8("分注: 5,8", DsGameType.ROULETTE),
    RL_6_9("分注: 6,9", DsGameType.ROULETTE),
    RL_7_8("分注: 7,8", DsGameType.ROULETTE),
    RL_7_10("分注: 7,10", DsGameType.ROULETTE),
    RL_8_9("分注: 8,9", DsGameType.ROULETTE),
    RL_8_11("分注: 8,11", DsGameType.ROULETTE),
    RL_9_12("分注: 9,12", DsGameType.ROULETTE),
    RL_10_11("分注: 10,11", DsGameType.ROULETTE),
    RL_10_13("分注: 10,13", DsGameType.ROULETTE),
    RL_11_12("分注: 11,12", DsGameType.ROULETTE),
    RL_11_14("分注: 11,14", DsGameType.ROULETTE),
    RL_12_15("分注: 12,15", DsGameType.ROULETTE),
    RL_13_14("分注: 13,14", DsGameType.ROULETTE),
    RL_13_16("分注: 13,16", DsGameType.ROULETTE),
    RL_14_15("分注: 14,15", DsGameType.ROULETTE),
    RL_14_17("分注: 14,17", DsGameType.ROULETTE),
    RL_15_18("分注: 15,18", DsGameType.ROULETTE),
    RL_16_17("分注: 16,17", DsGameType.ROULETTE),
    RL_16_19("分注: 16,19", DsGameType.ROULETTE),
    RL_17_18("分注: 17,18", DsGameType.ROULETTE),
    RL_17_20("分注: 17,20", DsGameType.ROULETTE),
    RL_18_21("分注: 18,21", DsGameType.ROULETTE),
    RL_19_20("分注: 19,20", DsGameType.ROULETTE),
    RL_19_22("分注: 19,22", DsGameType.ROULETTE),
    RL_20_21("分注: 20,21", DsGameType.ROULETTE),
    RL_20_23("分注: 20,23", DsGameType.ROULETTE),
    RL_21_24("分注: 21,24", DsGameType.ROULETTE),
    RL_22_23("分注: 22,23", DsGameType.ROULETTE),
    RL_22_25("分注: 22,25", DsGameType.ROULETTE),
    RL_23_24("分注: 23,24", DsGameType.ROULETTE),
    RL_23_26("分注: 23,26", DsGameType.ROULETTE),
    RL_24_27("分注: 24,27", DsGameType.ROULETTE),
    RL_25_26("分注: 23,26", DsGameType.ROULETTE),
    RL_25_28("分注: 25,28", DsGameType.ROULETTE),
    RL_26_27("分注: 26,27", DsGameType.ROULETTE),
    RL_26_29("分注: 26,29", DsGameType.ROULETTE),
    RL_27_30("分注: 27,30", DsGameType.ROULETTE),
    RL_28_29("分注: 28,29", DsGameType.ROULETTE),
    RL_28_31("分注: 28,31", DsGameType.ROULETTE),
    RL_29_30("分注: 29,30", DsGameType.ROULETTE),
    RL_29_32("分注: 29,32", DsGameType.ROULETTE),
    RL_30_33("分注: 30,33", DsGameType.ROULETTE),
    RL_31_32("分注: 31,32", DsGameType.ROULETTE),
    RL_31_34("分注: 31,34", DsGameType.ROULETTE),
    RL_32_33("分注: 32,33", DsGameType.ROULETTE),
    RL_32_35("分注: 32,35", DsGameType.ROULETTE),
    RL_33_36("分注: 33,36", DsGameType.ROULETTE),
    RL_34_35("分注: 34,35", DsGameType.ROULETTE),
    RL_35_36("分注: 35,36", DsGameType.ROULETTE),
    RL_0_1_2("衔注: 0,1,2", DsGameType.ROULETTE),
    RL_0_2_3("衔注: 0,2,3", DsGameType.ROULETTE),
    RL_1_2_3("衔注: 1,2,3", DsGameType.ROULETTE),
    RL_4_5_6("衔注: 4,5,6", DsGameType.ROULETTE),
    RL_7_8_9("衔注: 7,8,9", DsGameType.ROULETTE),
    RL_10_11_12("衔注: 10,11,12", DsGameType.ROULETTE),
    RL_13_14_15("衔注: 13,14,15", DsGameType.ROULETTE),
    RL_16_17_18("衔注: 16,17,18", DsGameType.ROULETTE),
    RL_19_20_21("衔注: 19,20,21", DsGameType.ROULETTE),
    RL_22_23_24("衔注: 22,23,24", DsGameType.ROULETTE),
    RL_25_26_27("衔注: 25,26,27", DsGameType.ROULETTE),
    RL_28_29_30("衔注: 28,29,30", DsGameType.ROULETTE),
    RL_31_32_33("衔注: 31,32,33", DsGameType.ROULETTE),
    RL_34_35_36("衔注: 34,35,36", DsGameType.ROULETTE),
    RL_0_1_2_3("角注: 0,1,2,3", DsGameType.ROULETTE),
    RL_1_2_4_5("角注: 1,2,4,5", DsGameType.ROULETTE),
    RL_2_3_5_6("角注: 2,3,5,6", DsGameType.ROULETTE),
    RL_4_5_7_8("角注: 4,5,7,8", DsGameType.ROULETTE),
    RL_5_6_8_9("角注: 5,6,8,9", DsGameType.ROULETTE),
    RL_7_8_10_11("角注: 7,8,10,11", DsGameType.ROULETTE),
    RL_8_9_11_12("角注: 8,9,11,12", DsGameType.ROULETTE),
    RL_10_11_13_14("角注: 10,11,13,14", DsGameType.ROULETTE),
    RL_11_12_14_15("角注: 11,12,14,15", DsGameType.ROULETTE),
    RL_13_14_16_17("角注: 13,14,16,17", DsGameType.ROULETTE),
    RL_14_15_17_18("角注: 14,15,17,18", DsGameType.ROULETTE),
    RL_16_17_19_20("角注: 16,17,19,20", DsGameType.ROULETTE),
    RL_17_18_20_21("角注: 17,18,20,21", DsGameType.ROULETTE),
    RL_19_20_22_23("角注: 19,20,22,23", DsGameType.ROULETTE),
    RL_20_21_23_24("角注: 20,21,23,24", DsGameType.ROULETTE),
    RL_25_26_28_29("角注: 25,26,28,29", DsGameType.ROULETTE),
    RL_26_27_29_30("角注: 26,27,29,30", DsGameType.ROULETTE),
    RL_28_29_31_32("角注: 28,29,31,32", DsGameType.ROULETTE),
    RL_29_30_32_33("角注: 29,30,32,33", DsGameType.ROULETTE),
    RL_31_32_34_35("角注: 31,32,34,35", DsGameType.ROULETTE),
    RL_32_33_35_36("角注: 32,33,35,36", DsGameType.ROULETTE),
    RL_1_2_3_4_5_6("线注: 1,2,3,4,5,6", DsGameType.ROULETTE),
    RL_4_5_6_7_8_9("线注: 4,5,6,7,8,9", DsGameType.ROULETTE),
    RL_7_8_9_10_11_12("线注: 7,8,9,10,11,12", DsGameType.ROULETTE),
    RL_10_11_12_13_14_15("线注: 10,11,12,13,14,15", DsGameType.ROULETTE),
    RL_13_14_15_16_17_18("线注: 13,14,15,16,17,18", DsGameType.ROULETTE),
    RL_16_17_18_19_20_21("线注: 16,17,18,19,20,21", DsGameType.ROULETTE),
    RL_19_20_21_22_23_24("线注: 19,20,21,22,23,24", DsGameType.ROULETTE),
    RL_22_23_24_25_26_27("线注: 22,23,24,25,26,27", DsGameType.ROULETTE),
    RL_25_26_27_28_29_30("线注: 25,26,27,28,29,30", DsGameType.ROULETTE),
    RL_28_29_30_31_32_33("线注: 28,29,30,31,32,33", DsGameType.ROULETTE),
    RL_31_32_33_34_35_36("线注: 31,32,33,34,35,36", DsGameType.ROULETTE),
    RL_ROW_1("列注: 1", DsGameType.ROULETTE),
    RL_ROW_2("列注: 2", DsGameType.ROULETTE),
    RL_ROW_3("列注: 3", DsGameType.ROULETTE),
    RL_COL_1("打注: 1", DsGameType.ROULETTE),
    RL_COL_2("打注: 2", DsGameType.ROULETTE),
    RL_COL_3("打注: 3", DsGameType.ROULETTE),
    RL_EVEN("双", DsGameType.ROULETTE),
    RL_RED("红", DsGameType.ROULETTE),
    RL_BLACK("黑", DsGameType.ROULETTE),
    RL_ODD("单", DsGameType.ROULETTE),
    RL_BIG("大", DsGameType.ROULETTE),
    RL_SMALL("小", DsGameType.ROULETTE),
    DT_DRAGON("龙", DsGameType.DRAGON_TIGER),
    DT_TIGER("虎", DsGameType.DRAGON_TIGER),
    DT_TIE("和", DsGameType.DRAGON_TIGER),
    DT_DRAGON_RED("龙红", DsGameType.DRAGON_TIGER),
    DT_DRAGON_BLACK("龙黑", DsGameType.DRAGON_TIGER),
    DT_DRAGON_ONE("龙单", DsGameType.DRAGON_TIGER),
    DT_DRAGON_DOUBLE("龙双", DsGameType.DRAGON_TIGER),
    DT_TIGER_RED("虎红", DsGameType.DRAGON_TIGER),
    DT_TIGER_BLACK("虎黑", DsGameType.DRAGON_TIGER),
    DT_TIGER_ONE("虎单", DsGameType.DRAGON_TIGER),
    DT_TIGER_DOUBLE("虎双", DsGameType.DRAGON_TIGER),
    SB_SMALL("小", DsGameType.SICBO),
    SB_BIG("大", DsGameType.SICBO),
    SB_ODD("单", DsGameType.SICBO),
    SB_EVEN("双", DsGameType.SICBO),
    SB_PAIR_1("对子: 1,1", DsGameType.SICBO),
    SB_PAIR_2("对子: 2,2", DsGameType.SICBO),
    SB_PAIR_3("对子: 3,3", DsGameType.SICBO),
    SB_PAIR_4("对子: 4,4", DsGameType.SICBO),
    SB_PAIR_5("对子: 5,5", DsGameType.SICBO),
    SB_PAIR_6("对子: 6,6", DsGameType.SICBO),
    SB_TRIPLE_1("围骰: 111", DsGameType.SICBO),
    SB_TRIPLE_2("围骰: 222", DsGameType.SICBO),
    SB_TRIPLE_3("围骰: 333", DsGameType.SICBO),
    SB_TRIPLE_4("围骰: 444", DsGameType.SICBO),
    SB_TRIPLE_5("围骰: 555", DsGameType.SICBO),
    SB_TRIPLE_6("围骰: 666", DsGameType.SICBO),
    SB_ANYTRIPLE("全围", DsGameType.SICBO),
    SB_TWO_1_2("组合: 12", DsGameType.SICBO),
    SB_TWO_1_3("组合: 13", DsGameType.SICBO),
    SB_TWO_1_4("组合: 14", DsGameType.SICBO),
    SB_TWO_1_5("组合: 15", DsGameType.SICBO),
    SB_TWO_1_6("组合: 16", DsGameType.SICBO),
    SB_TWO_2_3("组合: 23", DsGameType.SICBO),
    SB_TWO_2_4("组合: 24", DsGameType.SICBO),
    SB_TWO_2_5("组合: 25", DsGameType.SICBO),
    SB_TWO_2_6("组合: 26", DsGameType.SICBO),
    SB_TWO_3_4("组合: 34", DsGameType.SICBO),
    SB_TWO_3_5("组合: 35", DsGameType.SICBO),
    SB_TWO_3_6("组合: 36", DsGameType.SICBO),
    SB_TWO_4_5("组合: 45", DsGameType.SICBO),
    SB_TWO_4_6("组合: 46", DsGameType.SICBO),
    SB_TWO_5_6("组合: 56", DsGameType.SICBO),
    SB_ANYONE_1("三军1", DsGameType.SICBO),
    SB_ANYONE_2("三军2", DsGameType.SICBO),
    SB_ANYONE_3("三军3", DsGameType.SICBO),
    SB_ANYONE_4("三军4", DsGameType.SICBO),
    SB_ANYONE_5("三军5", DsGameType.SICBO),
    SB_ANYONE_6("三军6", DsGameType.SICBO),
    SB_SUM_4("点数4", DsGameType.SICBO),
    SB_SUM_5("点数5", DsGameType.SICBO),
    SB_SUM_6("点数6", DsGameType.SICBO),
    SB_SUM_7("点数7", DsGameType.SICBO),
    SB_SUM_8("点数8", DsGameType.SICBO),
    SB_SUM_9("点数9", DsGameType.SICBO),
    SB_SUM_10("点数10", DsGameType.SICBO),
    SB_SUM_11("点数11", DsGameType.SICBO),
    SB_SUM_12("点数12", DsGameType.SICBO),
    SB_SUM_13("点数13", DsGameType.SICBO),
    SB_SUM_14("点数14", DsGameType.SICBO),
    SB_SUM_15("点数15", DsGameType.SICBO),
    SB_SUM_16("点数16", DsGameType.SICBO),
    SB_SUM_17("点数17", DsGameType.SICBO),
    XOC_ZERO("全白", DsGameType.XOC_DIA),
    XOC_ONE("３白１红", DsGameType.XOC_DIA),
    XOC_TWO("２白２红", DsGameType.XOC_DIA),
    XOC_THREE("１白３红", DsGameType.XOC_DIA),
    XOC_FOUR("全红", DsGameType.XOC_DIA),
    XOC_ODD("单", DsGameType.XOC_DIA),
    XOC_EVEN("双", DsGameType.XOC_DIA),
    BB_PALYER1_EQUAL("闲1平倍", DsGameType.BULL_BULL),
    BB_PALYER1_DOUBLE("闲1翻倍", DsGameType.BULL_BULL),
    BB_PALYER2_EQUAL("闲2平倍", DsGameType.BULL_BULL),
    BB_PALYER2_DOUBLE("闲2翻倍", DsGameType.BULL_BULL),
    BB_PALYER3_EQUAL("闲3平倍", DsGameType.BULL_BULL),
    BB_PALYER3_DOUBLE("闲3翻倍", DsGameType.BULL_BULL);


    private String dsBetValue;
    private DsGameType dsGameType;

}
