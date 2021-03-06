package com.xuyun.platform.dsfcenterdata.dto;

import com.xuyun.platform.dsfcenterdata.entity.DsfMemberUser;
import com.xuyun.platform.dsfcenterdata.enums.GamePlatform;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @ClassName: PlayerBalance
 * @Author: R.M.I
 * @CreateTime: 2019年02月25日 20:38:00
 * @Description: TODO
 * @Version 1.0.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberBalanceDto {
    private DsfMemberUser dsfMemberUser;
    private BigDecimal balance;
    private GamePlatform gamePlatform;
}
