package com.invech.platform.modules.common.dto.ag;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AgBetBRDto extends AgBetGRDto {

    private String slottype;//老虎机类型
    private Long mainbillno;//主订单号
    private BigDecimal betAmountBase;//扣除jackpot的投注 額度
    private BigDecimal betAmountBonus;//Jackpot下注额度
    private  BigDecimal netAmountBase;//游戏派彩
    private  BigDecimal netAmountBonus;//Jackpot派彩
}
