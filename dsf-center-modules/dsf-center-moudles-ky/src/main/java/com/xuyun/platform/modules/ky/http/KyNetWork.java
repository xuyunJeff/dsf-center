package com.xuyun.platform.modules.ky.http;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xuyun.platform.dsfcenterdata.entity.DsfGmApi;
import com.xuyun.platform.dsfcenterdata.enums.ErrorCode;
import com.xuyun.platform.dsfcenterdata.response.DsfInvokeException;
import com.xuyun.platform.dsfcenterdata.utils.StringUtils;
import com.xuyun.platform.modules.common.constants.KyConstans;
import com.xuyun.platform.modules.common.dto.ky.KyReponse;
import com.xuyun.platform.modules.common.dto.ky.KyRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;

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
public class KyNetWork {

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
     * @param request
     * @return
     */
    public KyReponse get(DsfGmApi dsfGmApi, KyRequest request) {
        String url = kyUrl(dsfGmApi, request);
        log.info("path = {} \n, body = {}", url, JSONObject.toJSONString(request));
        HttpResponse resp = HttpRequest.get(url)
                .timeout(13000)//超时，毫秒
                .execute();
        if (!resp.isOk()) {
            log.error("Ky 请求异常 resp ={}", resp);
            throw new DsfInvokeException(ErrorCode.DSF_GAME_PLATFORM_REQUEST_ERROR_KY);
        }
        try {
            String response = resp.body();
            log.info("Ky 请求结果 resp ={}", response.substring(0, response.length() > 300 ? 300 : response.length()));

            JsonNode jsonNode = objectMapper.readTree(response);
            return objectMapper.readValue(jsonNode.toString(), KyReponse.class);
        } catch (IOException e) {
            log.error("Ky 请求结果 resp ={}", resp);
            throw new DsfInvokeException(ErrorCode.DSF_GAME_PLATFORM_PARAM_ERROR_KY);
        }
    }

    private static String kyUrl(DsfGmApi dsfGmApi, KyRequest kyRequest) {
        String url = "";
        if (!StringUtils.isEmpty(kyRequest.getS())) {
            if (kyRequest.getS().equals(KyConstans.QUERY_GAME_BET)) {
                JSONObject betUrl = JSONObject.parseObject(dsfGmApi.getSecureCode());
                url = betUrl.getString("betlogUrl");
            } else {
                url = dsfGmApi.getPcUrl();
            }
        } else {
            url = dsfGmApi.getPcUrl();
        }

        return (url.endsWith("/") ? url.substring(0, url.length() - 1) : url) + "?agent=" + kyRequest.getAgent() + "&timestamp=" + kyRequest.getTimestamp() + "&param=" +
                kyRequest.getParam() + "&key=" + kyRequest.getKey();
    }

}
