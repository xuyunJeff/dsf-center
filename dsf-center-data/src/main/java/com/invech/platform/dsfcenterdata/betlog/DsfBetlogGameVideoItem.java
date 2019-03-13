package com.invech.platform.dsfcenterdata.betlog;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @ClassName: DsfBetlogGameVideoItem
 * @Author: R.M.I
 * @CreateTime: 2019年03月04日 14:31:00
 * @Description: TODO
 * @Version 1.0.0
 */
@Table(name = "dsf_betlog_game_video_item")
@Data
@NoArgsConstructor
public class DsfBetlogGameVideoItem {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    /**
     * 父注单Id
     */
    private String dsfPlatformBetId;

    /**
     * 玩家投注类型
     */
    private String betType;

    private LocalDateTime betTime;

    private BigDecimal betAmount;

    private BigDecimal betWinLoss;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private String gamePlatform;

    public DsfBetlogGameVideoItem(String dsfPlatformBetId, String betType, LocalDateTime betTime, BigDecimal betAmount, BigDecimal betWinLoss, LocalDateTime createTime, LocalDateTime updateTime, String gamePlatform) {
        this.dsfPlatformBetId = dsfPlatformBetId;
        this.betType = betType;
        this.betTime = betTime;
        this.betAmount = betAmount;
        this.betWinLoss = betWinLoss;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.gamePlatform = gamePlatform;
    }
}
