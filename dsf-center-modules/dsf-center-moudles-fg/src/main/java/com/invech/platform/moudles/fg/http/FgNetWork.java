package com.invech.platform.moudles.fg.http;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.invech.platform.dsfcenterdata.entity.DsfGmApi;
import com.invech.platform.dsfcenterdata.enums.ErrorCode;
import com.invech.platform.dsfcenterdata.response.DsfInvokeException;
import com.invech.platform.dsfcenterdata.utils.StringUtils;
import com.invech.platform.modules.common.constants.FgConstants;
import com.invech.platform.modules.common.dto.fg.FgResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Map;

/**
 * @ClassName: AgNetWork
 * @Author: R.M.I
 * @CreateTime: 2019年02月25日 23:00:00
 * @Description: Ag的基礎請求
 * @Version 1.0.0
 */
@Component
@Slf4j
public class FgNetWork {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    ObjectMapper objectMapper;

    /**
     * public <T> T genericMethod(Class<T> tClass)throws InstantiationException ,
     * IllegalAccessException{
     * T instance = tClass.newInstance();
     * return instance;
     * }
     *
     * @param dsfGmApi
     * @param dsRequest
     * @return
     */
    public <T> FgResponse post(DsfGmApi dsfGmApi, T dsRequest,String method)  {
        String path = DsfGmApi.getSecureCodeMap(dsfGmApi.getSecureCode()).get("apiUrl").toString() + method;
        HttpHeaders headers = FgNetWork.fgHttpHeadlers(dsfGmApi);
        HttpEntity entity = new HttpEntity<>(dsRequest != null?dsRequest.toString():null, headers);
        log.info("path = {} , body = {}",path , JSONObject.toJSONString(entity));
        String resp = restTemplate.postForObject(path, entity, String.class);
        if (StringUtils.isEmpty(resp)) {
            log.error("Fg 请求异常 resp ={}", resp);
            throw new DsfInvokeException(ErrorCode.DSF_GAME_PLATFORM_REQUEST_ERROR_FG);
        }
        try {
            log.info("Fg 请求结果 resp ={}", resp.substring(0, resp.length() > 300 ? 300 : resp.length()));
            return objectMapper.readValue(resp, FgResponse.class);
        } catch (IOException e) {
            log.error("Fg 请求结果 resp ={}", resp);
            throw new DsfInvokeException(ErrorCode.DSF_GAME_PLATFORM_PARAM_ERROR_FG);
        }
    }

    /**
     * {'apiUrl':'https://api.ppro.98078.net','merchantcode':'196f947f9c00ee8e20af23cc0d5b5926','merchantname':'uat_yingfa1936'}
     * @param dsfGmApi
     * @return
     */
    private static HttpHeaders fgHttpHeadlers(DsfGmApi dsfGmApi){
        Map<String, Object> map= DsfGmApi.getSecureCodeMap(dsfGmApi.getSecureCode());
        String merchantcode = map.get(FgConstants.MERCHANT_CODE).toString();
        String merchantname = map.get(FgConstants.MERCHANT_NAME).toString();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.add(FgConstants.MERCHANT_CODE, merchantcode);
        headers.add(FgConstants.MERCHANT_NAME, merchantname);
        return headers;
    }

}
