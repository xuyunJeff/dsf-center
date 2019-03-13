package com.xuyun.platform.modules.common.config;

import com.xuyun.platform.dsfcenterdata.enums.ErrorCode;
import com.xuyun.platform.dsfcenterdata.response.RRException;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.lang.Nullable;
import org.springframework.web.client.*;

import java.util.List;

/**
 * @ClassName: HttpRestTemplate
 * @Author: R.M.I
 * @CreateTime: 2019年03月05日 13:15:00
 * @Description: TODO
 * @Version 1.0.0
 */
public class HttpRestTemplate  extends RestTemplate {

    public HttpRestTemplate(ClientHttpRequestFactory requestFactory) {
        super(requestFactory);
    }

    public HttpRestTemplate() {
        super();
    }


    public HttpRestTemplate(List<HttpMessageConverter<?>> messageConverters) {
        super(messageConverters);
    }

    @Override
    @Nullable
    public <T> T postForObject(String url, @Nullable Object request, Class<T> responseType,
                               Object... uriVariables) throws RestClientException {
        RequestCallback requestCallback = httpEntityCallback(request, responseType);
        HttpMessageConverterExtractor<T> responseExtractor = new DsfHttpMessageConverterExtractor(responseType, getMessageConverters());
        try {
            return execute(url, HttpMethod.POST, requestCallback, responseExtractor, uriVariables);
        }catch (ResourceAccessException exception){
            throw new RRException(ErrorCode.DSF_GAME_PLATFORM_REQUEST_ERROR);
        }

    }
}
