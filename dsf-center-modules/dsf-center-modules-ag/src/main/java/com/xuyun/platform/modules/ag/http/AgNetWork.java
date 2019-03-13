package com.xuyun.platform.modules.ag.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xuyun.platform.dsfcenterdata.entity.DsfGmApi;
import com.xuyun.platform.dsfcenterdata.enums.ErrorCode;
import com.xuyun.platform.dsfcenterdata.response.DsfInvokeException;
import com.xuyun.platform.dsfcenterdata.response.RRException;
import com.xuyun.platform.dsfcenterdata.utils.DESEncrypt;
import com.xuyun.platform.dsfcenterdata.utils.MD5;
import com.xuyun.platform.modules.common.config.XmlUtil;
import com.xuyun.platform.modules.common.constants.AgConstants;
import com.xuyun.platform.modules.common.dto.ag.AgDataDto;
import com.xuyun.platform.modules.common.dto.ag.AgResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * @ClassName: AgNetWork
 * @Author: R.M.I
 * @CreateTime: 2019年02月25日 23:00:00
 * @Description: Ag的基礎請求
 * @Version 1.0.0
 */
@Component
@Slf4j
public class AgNetWork {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    ObjectMapper objectMapper;

    public AgResponseDto post(DsfGmApi dsfGmApi, AgDataDto agDataDto) {
        StringBuffer buffer = new StringBuffer();
        //pcUrl2 为gc.xxx 用来调用api
        buffer.append(dsfGmApi.getPcUrl2()).append(AgConstants.AGIN_M_DOBUSINESS);
        buffer.append(getParams(DsfGmApi.getSecureCodeMap(dsfGmApi.getSecureCode()).get(AgConstants.AGIN_DESCODE_KEY).toString(), dsfGmApi.getMd5Key(), agDataDto));
        log.info(buffer.toString());
        String resp= restTemplate.postForObject(buffer.toString(), null, String.class);
        try {
            return XmlUtil.getAginResult(resp);
        } catch (Exception e) {
            log.error("Ag 接口响应错误 agDataDto ={} ， dsfGmApi ={} ,url = {}", agDataDto, dsfGmApi, buffer.toString());
            log.error("Ag 接口响应错误 resp", resp);
            throw new DsfInvokeException(ErrorCode.DSF_GAME_PLATFORM_REQUEST_ERROR_AG);
        }
    }

    public static String getParams(String secureCode, String md5Key, AgDataDto agDataDto) {
        StringBuilder stringBuffer = new StringBuilder();
        try {
            DESEncrypt d = new DESEncrypt(secureCode);
            String params = d.encrypt(agDataDto.toString());
            String key = MD5.getMD5(params + md5Key);
            stringBuffer.append("params=").append(params).append("&key=").append(key);
        } catch (Exception e) {
            throw new RRException("AG DES加密错误，请检查第三方配置");
        }
        return stringBuffer.toString();
    }

}
