package com.xuyun.platform.dsfcenterdata.response;

import com.xuyun.platform.dsfcenterdata.enums.ErrorCode;
import lombok.Data;

/**
 * @ClassName: DsfInvokeException
 * @Author: R.M.I
 * @CreateTime: 2019年03月06日 00:24:00
 * @Description: TODO
 * @Version 1.0.0
 */
@Data
public class DsfInvokeException extends RuntimeException {
    int code = 3000;
    String msg = "第三方游戏平台请求错误";
    int dsfErrorCode;
    String dsfErrorMsg;

    public DsfInvokeException(ErrorCode error) {
        this.code = code;
        this.msg = msg;
        this.dsfErrorCode = error.getCode();
        this.dsfErrorMsg = error.getMsg();
    }


    public DsfInvokeException(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public DsfInvokeException(String message, int code, String msg) {
        super(message);
        this.code = code;
        this.msg = msg;
    }

    public DsfInvokeException(String message, Throwable cause, int code, String msg) {
        super(message, cause);
        this.code = code;
        this.msg = msg;
    }

    public DsfInvokeException(Throwable cause, int code, String msg) {
        super(cause);
        this.code = code;
        this.msg = msg;
    }

    public DsfInvokeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, int code, String msg) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.code = code;
        this.msg = msg;
    }
}
