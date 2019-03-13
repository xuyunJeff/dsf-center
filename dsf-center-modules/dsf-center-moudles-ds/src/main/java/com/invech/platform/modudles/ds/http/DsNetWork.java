package com.invech.platform.modudles.ds.http;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.invech.platform.dsfcenterdata.entity.DsfGmApi;
import com.invech.platform.dsfcenterdata.enums.ErrorCode;
import com.invech.platform.dsfcenterdata.response.DsfInvokeException;
import com.invech.platform.dsfcenterdata.response.RRException;
import com.invech.platform.modules.common.dto.ds.request.DsRequest;
import com.invech.platform.modules.common.dto.ds.response.DsResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.NoHttpResponseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

/**
 * @ClassName: AgNetWork
 * @Author: R.M.I
 * @CreateTime: 2019年02月25日 23:00:00
 * @Description: Ag的基礎請求
 * @Version 1.0.0
 */
@Component
@Slf4j
public class DsNetWork {

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
    public DsResponse post(DsfGmApi dsfGmApi, DsRequest dsRequest) {
        String path = DsfGmApi.getSecureCodeMap(dsfGmApi.getSecureCode()).get("apiUrl").toString();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
        HttpEntity entity = new HttpEntity<>(dsRequest, headers);
        log.info(JSONObject.toJSONString(entity));
        String resp = restTemplate.postForObject(path, entity, String.class);
        if (resp == null) {
            log.error("Ds 请求异常 resp ={}", resp);
            throw new DsfInvokeException(ErrorCode.DSF_GAME_PLATFORM_REQUEST_ERROR_Ds);
        }
        try {
            log.info("Ds 请求结果 resp ={}", resp.substring(0, resp.length() > 300 ? 300 : resp.length()));
            return objectMapper.readValue(resp, DsResponse.class);
        } catch (IOException e) {
            log.error("Ds 请求结果 resp ={}", resp);
            throw new DsfInvokeException(ErrorCode.DSF_GAME_PLATFORM_REQUEST_ERROR_Ds);
        }
    }


}
