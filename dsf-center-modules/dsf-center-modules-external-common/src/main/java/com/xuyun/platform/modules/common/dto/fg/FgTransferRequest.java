package com.xuyun.platform.modules.common.dto.fg;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName: FgTransferRequest
 * @Author: R.M.I
 * @CreateTime: 2019年03月06日 19:11:00
 * @Description: TODO
 * @Version 1.0.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class FgTransferRequest {
    private Integer amount;
    private String externaltransactionid;

    @Override
    public String toString() {
        return "&amount=" + amount +"&externaltransactionid=" + externaltransactionid ;
    }
}
