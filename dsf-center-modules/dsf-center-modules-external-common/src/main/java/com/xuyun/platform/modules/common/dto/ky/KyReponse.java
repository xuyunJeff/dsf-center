package com.xuyun.platform.modules.common.dto.ky;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName: KyReponse
 * @Author: R.M.I
 * @CreateTime: 2019年03月07日 12:13:00
 * @Description: TODO
 * @Version 1.0.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class KyReponse<T> {
    private Integer s ;
    private String m;
    private T d;
}
