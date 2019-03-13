package com.xuyun.platform.modules.common.dto.ky;

import com.xuyun.platform.dsfcenterdata.entity.DsfGmApi;
import com.xuyun.platform.dsfcenterdata.utils.AESUtil;
import com.xuyun.platform.dsfcenterdata.utils.DateUtil;
import com.xuyun.platform.dsfcenterdata.utils.MD5;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName: KyBalanceRequest
 * @Author: R.M.I
 * @CreateTime: 2019年03月07日 20:01:00
 * @Description: TODO
 * @Version 1.0.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Slf4j
public class KyBalanceRequest  extends KyRequest {
    /**
     * 参数加密字符串 param=
     * （s=7&account=111111）
     * s：操作子类型:7
     * account：会员帐号）
     * Encrypt.AESEncrypt(param,DESK
     * ey);
     * DESKey：平台ᨀ供
     */

    private String account;

    public KyBalanceRequest(String s, DsfGmApi dsfGmApi, String account) {
        super.timestamp = DateUtil.currentTimestamp();
        super.agent = dsfGmApi.getAgyAcc();
        this.account = account;
        super.key = MD5.MD5(super.agent + super.timestamp + dsfGmApi.getMd5Key());
        String desKey = DsfGmApi.getSecureCodeMap(dsfGmApi.getSecureCode()).get("desKey").toString();
        String params = "s=" + s + "&account=" + account ;
        log.info(params);
        try {
            super.param = AESUtil.AESEncrypt(params, desKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
