package com.invech.platform.dsfcenterdata.dto;

import com.invech.platform.dsfcenterdata.enums.GamePlatform;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName: WithdrawBalanceAll
 * @Author: R.M.I
 * @CreateTime: 2019年02月25日 20:54:00
 * @Description: TODO
 * @Version 1.0.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WithdrawBalanceAll {
    private String transactionId;
    private GamePlatform gamePlatform;
}
