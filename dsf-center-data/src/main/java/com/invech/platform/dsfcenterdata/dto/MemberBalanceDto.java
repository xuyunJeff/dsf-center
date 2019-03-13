package com.invech.platform.dsfcenterdata.dto;

import com.invech.platform.dsfcenterdata.entity.DsfMemberUser;
import com.invech.platform.dsfcenterdata.enums.GamePlatform;
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
