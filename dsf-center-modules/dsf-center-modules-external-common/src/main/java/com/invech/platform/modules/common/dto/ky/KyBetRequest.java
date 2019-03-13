package com.invech.platform.modules.common.dto.ky;

import com.invech.platform.dsfcenterdata.entity.DsfGmApi;
import com.invech.platform.dsfcenterdata.utils.AESUtil;
import com.invech.platform.dsfcenterdata.utils.DateUtil;
import com.invech.platform.dsfcenterdata.utils.MD5;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

@Data
@AllArgsConstructor
@Slf4j
public class KyBetRequest extends KyRequest {
    //操作子类型
    String s;
    //开始时间
    Date startTime;
    //结束时间
    Date endTime;

    public KyBetRequest(String s, DsfGmApi dsfGmApi, Date startTime, Date endTime) {
        super.agent = dsfGmApi.getAgyAcc();
        super.timestamp = DateUtil.currentTimestamp();
        this.s = s;
        this.startTime = startTime;
        this.endTime = endTime;
        String params = "s="+s+"&startTime="+ startTime.getTime()+"&endTime="+
               endTime.getTime();
        log.info(params);
        String desKey = DsfGmApi.getSecureCodeMap(dsfGmApi.getSecureCode()).get("desKey").toString();
        log.info(desKey);
        try {
            this.param = AESUtil.AESEncrypt(params, desKey).replaceAll("\r", "").replaceAll("\n", "");
        }catch (Exception e){
            e.printStackTrace();
        }
        super.key = MD5.MD5(super.agent + super.timestamp + dsfGmApi.getMd5Key());
    }
}
