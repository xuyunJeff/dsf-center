package com.invech.platform.modules.common.dto.ds.response;

import com.invech.platform.modules.common.enums.DsErroeCodes;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @ClassName: DsResponse
 * @Author: R.M.I
 * @CreateTime: 2019年02月27日 18:01:00
 * @Description: TODO
 * @Version 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DsResponse<T> {
    /*
    {"request":null,"errorCode":0,"errorMessage":null,"logId":1421085079191,
    "params":{"link":"http://ds.iasia999.com/liveflash/dingsheng.jsp?code=35b51521-b57c-4933-8884-7be05f1314c9&lc=en&line=0"}}
     */
    private String request;
    private Integer errorCode;
    private String errorMessage;
    private Long logId;
    private T params;

    public DsErroeCodes getErrorCode(){
       return DsErroeCodes.dsError(this.errorCode);
    }
}
