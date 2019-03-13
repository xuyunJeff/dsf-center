package com.invech.platform.modules.common.dto.ky;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class KyBetRecordReponse {
    @JsonProperty(value="GameID")
    private String[] gameId;
    @JsonProperty(value="Accounts")
    private String[] accounts;
    @JsonProperty(value="ServerID")
    private Integer[] serverId;
    @JsonProperty(value="KindID")
    private Integer[] kindId;
    @JsonProperty(value="TableID")
    private Integer[] tableId;
    @JsonProperty(value="ChairID")
    private Integer[] chairId;
    @JsonProperty(value="UserCount")
    private Integer[] userCount;
    @JsonProperty(value="CardValue")
    private String[] cardValue;
    @JsonProperty(value="CellScore")
    private BigDecimal[] cellScore;
    @JsonProperty(value="AllBet")
    private BigDecimal[] allBet;
    @JsonProperty(value="Profit")
    private BigDecimal[] profit;
    @JsonProperty(value="Revenue")
    private BigDecimal[] revenue;
    @JsonProperty(value="GameStartTime")
    private LocalDateTime[] gameStartTime;
    @JsonProperty(value="GameEndTime")
    private LocalDateTime[] gameEndTime;
    @JsonProperty(value="ChannelID")
    private Integer[] channelId;
    @JsonProperty(value="LineCode")
    private String[] lineCode;


}
