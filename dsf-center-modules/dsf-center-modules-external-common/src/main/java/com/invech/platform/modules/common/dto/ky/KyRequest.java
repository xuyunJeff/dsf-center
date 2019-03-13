package com.invech.platform.modules.common.dto.ky;

import com.invech.platform.dsfcenterdata.utils.DateUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName: KyRequest
 * @Author: R.M.I
 * @CreateTime: 2019年03月07日 12:05:00
 * @Description: TODO
 * @Version 1.0.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class KyRequest {

     String s;
    // 代理编号（平台提供）
     String agent;
    // 时间戳(Unix 时间戳带上毫秒),获取当前时间（1488781836949）
     Long timestamp;

    //参数加密字符串
    /**
     * 参数加密字符串 param=
     * （s=1&account=111111）
     * s：操作子类型:1
     * account：会员帐号）
     * Encrypt.AESEncrypt(param,DESK
     * ey);
     * DESKey：平台供
     */
     String param;

    //Md5 校验字符串Encrypt. MD5 (agent+timestamp+MD5Key);
     String key;


}
