package com.invech.platform.modules.common.dto.ds.response;

import com.invech.platform.modules.common.enums.DsBetType;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @ClassName: DsReportDetailReponse
 * @Author: R.M.I
 * @CreateTime: 2019年03月02日 14:22:00
 * @Description: TODO
 * @Version 1.0.0
 */
@Data
public class DsReportDetailReponse {
    private String betType;
    private BigDecimal betAmount;
    private BigDecimal winLossAmount;
    private Long betTime;

    public static String getBetTypeCn(String betType) {
        return DsBetType.valueOf(betType).getDsBetValue();
    }
}
