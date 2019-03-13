package com.invech.platform.modules.common.dto.fg;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName: FgResponse
 * @Author: R.M.I
 * @CreateTime: 2019年03月06日 14:37:00
 * @Description: TODO
 * @Version 1.0.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FgResponse<T> {
    private Integer code;
    private String msg;
    private T data;
}
