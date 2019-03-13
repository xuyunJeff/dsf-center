package com.invech.platform.modules.common.dto.ds.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName: DsBetRequest
 * @Author: R.M.I
 * @CreateTime: 2019年03月01日 17:00:00
 * @Description: TODO
 * @Version 1.0.0
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class DsBetRequest {
    private String count = "1000";
    private String beginId;

    public DsBetRequest(String beginId) {
        this.beginId = beginId;
    }
}
