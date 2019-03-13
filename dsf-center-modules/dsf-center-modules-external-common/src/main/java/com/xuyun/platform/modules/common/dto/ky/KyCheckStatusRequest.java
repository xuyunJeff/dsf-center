package com.xuyun.platform.modules.common.dto.ky;

import com.xuyun.platform.dsfcenterdata.entity.DsfGmApi;
import com.xuyun.platform.dsfcenterdata.utils.AESUtil;
import com.xuyun.platform.dsfcenterdata.utils.DateUtil;
import com.xuyun.platform.dsfcenterdata.utils.MD5;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName: KyCheckStatusRequest
 * @Author: R.M.I
 * @CreateTime: 2019年03月07日 22:39:00
 * @Description: TODO
 * @Version 1.0.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class KyCheckStatusRequest extends KyRequest {
    private String orderid;

    public KyCheckStatusRequest(String s, DsfGmApi dsfGmApi, String orderid) {
        super.timestamp = DateUtil.currentTimestamp();
        super.agent = dsfGmApi.getAgyAcc();
        this.orderid = orderid;
        super.key = MD5.MD5(super.agent + super.timestamp + dsfGmApi.getMd5Key());
        String desKey = DsfGmApi.getSecureCodeMap(dsfGmApi.getSecureCode()).get("desKey").toString();
        String params = "s="+s+"&orderid="+orderid;
        try {
            super.param = AESUtil.AESEncrypt(params, desKey).replaceAll("\r","").replaceAll("\n","");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
