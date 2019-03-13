package com.invech.platform.modules.common.dto.fg;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.invech.platform.dsfcenterdata.utils.StringUtils;
import com.invech.platform.modules.common.enums.FgDeviceType;
import com.invech.platform.modules.common.enums.FgFishType;
import com.invech.platform.modules.common.enums.FgGameType;
import lombok.Data;

import java.math.BigDecimal;

/**
 * FG拉单返回数据
 */

@Data
public class FgBetlogResponse {
    private String id;

    @JsonProperty(value="total_agent_uid")
    private Integer totalAgentUid;
    @JsonProperty(value="agent_uid")
    private Integer agentUid;
    @JsonProperty(value="player_name")
    private String playerName;
    @JsonProperty(value="game_id")
    private Integer gameId;
    private String gt;
    private Integer device;
    private Integer time;
    @JsonProperty(value="end_chips")
    private BigDecimal endChips;
    @JsonProperty(value="all_bets")
    private BigDecimal allBets;
    @JsonProperty(value="all_wins")
    private BigDecimal allWins;
    @JsonProperty(value="total_bets")
    private Double totalBets;
    @JsonProperty(value="jackpot_bonus")
    private BigDecimal jackpotBonus;
    @JsonProperty(value="jp_contri")
    private BigDecimal jpContri;
    private BigDecimal result;

    //fish添加以下字段
    private Integer type;
    @JsonProperty(value="scene_id")
    private Integer sceneId;
    @JsonProperty(value="bullet_count")
    private Integer bulletCount;
    @JsonProperty(value="start_chips")
    private Double startChips;
    @JsonProperty(value="start_time")
    private Long startTime;

    public static String getGt(String gt){
        if(StringUtils.isEmpty(gt)){
            return gt;
        }
        return FgGameType.valueOf(gt).getGameType();
    }

    public static String getDevice(Integer device){
        if(device == 0){
            return  device.toString();
        }
        return FgDeviceType.getName(device);
    }

    public static  String  getTypeName(Integer type){
        if(type == 0){
            return  type.toString();
        }
        return FgFishType.getName(type);
    }

}
