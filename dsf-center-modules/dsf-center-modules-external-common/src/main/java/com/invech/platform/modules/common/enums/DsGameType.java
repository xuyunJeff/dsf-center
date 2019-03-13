package com.invech.platform.modules.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @ClassName: DsGameType
 * @Author: R.M.I
 * @CreateTime: 2019年03月01日 17:30:00
 * @Description: TODO
 * @Version 1.0.0
 */
@AllArgsConstructor
@Getter
public enum DsGameType {
    /**
     * 下注游戏类型:
     * BACCARAT        百家乐
     * DRAGON_TIGER   龙虎
     * ROULETTE        轮盘
     * BACCARAT_INSURANCE　保险百家乐
     * SICBO,     骰宝
     * XOC_DIA   色碟
     * BULL_BULL  牛牛
     */
    BACCARAT("百家乐"),
    DRAGON_TIGER("龙虎"),
    ROULETTE("轮盘"),
    BACCARAT_INSURANCE("保险百家乐"),
    SICBO("骰宝"),
    XOC_DIA("色碟"),
    BULL_BULL("牛牛");

    private String gameNameCn;

}
