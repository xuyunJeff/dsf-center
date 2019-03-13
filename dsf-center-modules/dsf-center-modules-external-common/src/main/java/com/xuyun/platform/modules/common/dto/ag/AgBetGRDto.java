package com.xuyun.platform.modules.common.dto.ag;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @ClassName: AgBetGRDto
 * @Author: R.M.I
 * @CreateTime: 2019年03月08日 21:57:00
 * @Description: TODO
 * @Version 1.0.0
 */
@NoArgsConstructor
@Data
@AllArgsConstructor
public class AgBetGRDto {
    private String dataType;//下注记录详情
    private Long billNo;//注单流水号
    private String playerName;//玩家账号
    private String agentCode;//代理商编号
    private String gameCode;//游戏局号
    private BigDecimal netAmount;//玩家输赢额度
    private Date betTime;//投注时间
    private String gameType;//游戏类型
    private BigDecimal betAmount;//投注金额
    private BigDecimal validBetAmount;//有效投注额度
    private String flag;//结算状态
    private String playType;//游戏玩法
    private String currency;//货币类型
    private String tableCode;//桌子编号
    private String loginIP;//玩家IP
    private Date recalcuTime;//注单重新派彩时间
    private String platformType;//平台类型
    private String remark;//额外信息
    private String round;//平台内的大厅类型
    private String result;//结果
    private BigDecimal beforeCredit;//玩家下注前的剩余额度
    private String deviceType;//设备类型
}
