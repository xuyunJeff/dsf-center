package com.invech.platform.modules.common.dto.ky;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @ClassName: KyCheckStatusReponse
 * @Author: R.M.I
 * @CreateTime: 2019年03月07日 22:44:00
 * @Description: TODO
 * @Version 1.0.0
 */
@Data
public class KyCheckStatusReponse extends KyReponseD {
    private Integer status;
    private BigDecimal money;
}
