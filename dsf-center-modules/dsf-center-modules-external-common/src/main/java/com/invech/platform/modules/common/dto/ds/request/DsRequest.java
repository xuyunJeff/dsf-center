package com.invech.platform.modules.common.dto.ds.request;

import com.invech.platform.dsfcenterdata.entity.DsfGmApi;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName: DsRequest
 * @Author: R.M.I
 * @CreateTime: 2019年02月27日 17:36:00
 * @Description: TODO
 * @Version 1.0.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DsRequest<T> {
    private String hashCode;
    private String command;
    private T params;

    public DsRequest(T params) {
        this.params = params;
    }

    public DsRequest(T params,String command) {
        this.params = params;
        this.command =command;
    }

    public static String getHashCode(DsfGmApi dsfGmApi) {
        return DsfGmApi.getSecureCodeMap(dsfGmApi.getSecureCode()).get("hashCode").toString();
    }
}
