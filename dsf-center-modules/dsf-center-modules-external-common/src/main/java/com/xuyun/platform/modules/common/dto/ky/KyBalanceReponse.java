package com.xuyun.platform.modules.common.dto.ky;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

/**
 * @ClassName: KyBalanceReponse
 * @Author: R.M.I
 * @CreateTime: 2019年03月07日 20:08:00
 * @Description: TODO
 * @Version 1.0.0
 */
@Slf4j
@Data
public class KyBalanceReponse extends KyReponseD {
    BigDecimal totalMoney;
    BigDecimal freeMoney;
    Integer status;
    Integer gameStatus;
}
