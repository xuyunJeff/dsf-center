package com.xuyun.platform.modules.common.dto.ds.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @ClassName: DsBalanceResponse
 * @Author: R.M.I
 * @CreateTime: 2019年02月27日 21:35:00
 * @Description: TODO
 * @Version 1.0.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class DsBalanceResponse {
    private BigDecimal balance;
}
