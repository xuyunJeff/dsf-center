package com.xuyun.platform.modules.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter
public enum FgFishType {

    /**
     * 捕猎投注类型
     */
    FISH_BILL_SCENE("场景捕鱼账单", 1, "Fish_BILL_SCENE"),
    JP_BILL("JP奖账单", 2, "JP_BILL"),
    FISH_BUY_NOTE("买鱼注单", 6, "FISH_BUY_NOTE"),
    LASER_CONNON("激光炮", 14, "LASER_CONNON"),
    BOSS("boss赛功能", 15, "BOSS"),
    FUND_PUNCHASE("基金购买", 16, "FUND_PUNCHASE"),
    LEVEL_BONUS("等级奖金", 17, "LEVEL_BONUS"),
    LUCK_BOX("幸运宝箱", 17, "LUCK_BOX");

    private String name;
    private Integer code;
    private String type;


    private  FgFishType(String name, int code) {
        this.name = name;
        this.code = code;
    }


    public static String getName(int code) {
        for (FgFishType c : FgFishType.values()) {
            if (c.getCode() == code) {
                return c.name;
            }
        }
        return null;
    }

    public static Integer getCode(String name) {
        for (FgFishType c : FgFishType.values()) {
            if (c.getName().equals(name)) {
                return c.code;
            }
        }
        return null;
    }


}
