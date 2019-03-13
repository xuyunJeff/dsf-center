package com.invech.platform.modules.common.dto.fg;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @ClassName: FgBalanceResponse
 * @Author: R.M.I
 * @CreateTime: 2019年03月06日 17:37:00
 * @Description: TODO
 * @Version 1.0.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class FgBalanceResponse {
    private BigDecimal balance;
    private String currency;
}
