package com.invech.platform.modules.common.dto.ky;

import com.invech.platform.dsfcenterdata.entity.DsfGmApi;
import com.invech.platform.dsfcenterdata.utils.AESUtil;
import com.invech.platform.dsfcenterdata.utils.DateUtil;
import com.invech.platform.dsfcenterdata.utils.MD5;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

/**
 * @ClassName: KyTransferRequest
 * @Author: R.M.I
 * @CreateTime: 2019年03月07日 20:21:00
 * @Description: TODO
 * @Version 1.0.0
 */
@Data
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
public class KyTransferRequest extends KyRequest {
    /**
     * s=2&account=111111&money=10
     * 0&orderid=1000120170306143036
     * 949111111
     */
    private String account;
    private BigDecimal money;
    private String orderid;

    public KyTransferRequest(String s, DsfGmApi dsfGmApi, String account,BigDecimal money,String orderid) {
        super.timestamp = Long.valueOf(orderid.substring(orderid.length()-13));
        super.agent = dsfGmApi.getAgyAcc();
        this.account = account;
        this.orderid = orderid;
        super.key = MD5.MD5(super.agent + super.timestamp + dsfGmApi.getMd5Key());
        String desKey = DsfGmApi.getSecureCodeMap(dsfGmApi.getSecureCode()).get("desKey").toString();
        String params = "s=" + s + "&account=" + account + "&money="+money+"&orderid=" + orderid ;
        try {
            super.param = AESUtil.AESEncrypt(params, desKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
