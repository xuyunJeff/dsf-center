package com.xuyun.platform.modules.common.dto.ds.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName: DsSecurty
 * @Author: R.M.I
 * @CreateTime: 2019年02月27日 17:39:00
 * @Description: TODO
 * @Version 1.0.0
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class DsSecurty {
    private String HashCode;
    private String apiUrl;
}
