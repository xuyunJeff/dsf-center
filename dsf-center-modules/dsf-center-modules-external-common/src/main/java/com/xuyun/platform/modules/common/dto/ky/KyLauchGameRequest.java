package com.xuyun.platform.modules.common.dto.ky;

import com.xuyun.platform.dsfcenterdata.entity.DsfGmApi;
import com.xuyun.platform.dsfcenterdata.utils.AESUtil;
import com.xuyun.platform.dsfcenterdata.utils.DateUtil;
import com.xuyun.platform.dsfcenterdata.utils.MD5;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName: KyLauchGameRequest
 * @Author: R.M.I
 * @CreateTime: 2019年03月07日 14:20:00
 * @Description: TODO
 * @Version 1.0.0
 */
@AllArgsConstructor
@Data
@Slf4j
public class KyLauchGameRequest extends KyRequest {
    private String account;
    private Integer money;
    //流水号（格式：代理编号+yyyyMMddHHmmssSSS+ account,长度不能超过 100 字符串）
    private String orderid;
    private String ip;
    private String lineCode;
    private String KindID;

    public KyLauchGameRequest(String s, DsfGmApi dsfGmApi, String account, String ip, String lineCode, String KindID) {
        super.timestamp = DateUtil.currentTimestamp();
        super.agent = dsfGmApi.getAgyAcc();
        this.account = account;
        this.orderid = super.agent + DateUtil.getSimpleDateFormat(DateUtil.FORMAT_17_DATE_TIME, super.timestamp) + account;
        super.key = MD5.MD5(super.agent + super.timestamp + dsfGmApi.getMd5Key());
        String desKey = DsfGmApi.getSecureCodeMap(dsfGmApi.getSecureCode()).get("desKey").toString();
        log.info(desKey);
        this.KindID = KindID;
        String params = "s="+s+"&account="+account+"&money=0&orderid="+orderid+"&ip="+ip+"&lineCode="+lineCode+"&KindID="+KindID;
        log.info(params);
        try {
            super.param = AESUtil.AESEncrypt(params, desKey).replaceAll("\r","").replaceAll("\n","");
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.ip = ip;
        this.lineCode = lineCode;
    }

}
