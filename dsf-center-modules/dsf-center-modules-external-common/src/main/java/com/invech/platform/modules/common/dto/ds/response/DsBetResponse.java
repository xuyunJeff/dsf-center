package com.invech.platform.modules.common.dto.ds.response;

import lombok.Data;

import java.util.List;

/**
 * @ClassName: DsBetResponse
 * @Author: R.M.I
 * @CreateTime: 2019年03月01日 17:24:00
 * @Description: TODO
 * @Version 1.0.0
 */
@Data
public class DsBetResponse {
    private List<DsBetlogReponse> recordList;

}
