package com.invech.platform.modules.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter
public enum FgGameType {

    FISH("fish", "捕鱼"),
    SLOT("slot", "老虎机"),
    FRUIT("fruit", "水果机"),
    POKER("poker", "棋牌");

    public String gameType;

    public String name;

    public static FgGameType getGameType(String gameType) {
        for (FgGameType fgGameType : FgGameType.values()) {
            return fgGameType;
        }
        return null;
    }

}
