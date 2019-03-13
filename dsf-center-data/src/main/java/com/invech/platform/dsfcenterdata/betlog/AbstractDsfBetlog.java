package com.invech.platform.dsfcenterdata.betlog;

import com.invech.platform.dsfcenterdata.enums.PlatformType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @ClassName: AbstractDsfBetlog
 * @Author: R.M.I
 * @CreateTime: 2019年03月02日 20:07:00
 * @Description: TODO
 * @Version 1.0.0
 */
@Table(name = "dsf_betlog")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class AbstractDsfBetlog {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    private String siteCode;

    private String betId;

    private String dsfPlatformBetId;

    private String categoryName;

    private String gamePlatform;

    private String memberUser;

    private String dsfPlayerId;

    private String gameNameCn;



    /**
     * 投注金额
     */
    private BigDecimal stakeAmount;
    /**
     * 有效投注
     */
    private BigDecimal validStake;
    /**
     * 输赢
     */
    private BigDecimal winLoss;
    /**
     * 奖池金额
     */
    private BigDecimal jackpot;

    private LocalDateTime gameTime;

    private LocalDateTime createTime;

    private String gameResult;
    /**
     *  其他内容,保存json字符串
     */
    private String remark;

    @Transient
    private PlatformType platformType;

    @Transient
    private List<DsfBetlogGameVideoItem> itemList;

}
