package com.xuyun.platform.modules.common.config;

import org.apache.commons.logging.Log;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.client.HttpMessageConverterExtractor;
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.List;

/**
 * @ClassName: DsfHttpMessageConverterExtractor
 * @Author: R.M.I
 * @CreateTime: 2019年03月05日 12:13:00
 * @Description: TODO
 * @Version 1.0.0
 */
public class DsfHttpMessageConverterExtractor<T> extends HttpMessageConverterExtractor<T> {
    public DsfHttpMessageConverterExtractor(Class<T> responseType, List<HttpMessageConverter<?>> messageConverters) {
        super(responseType, messageConverters);
    }

    public DsfHttpMessageConverterExtractor(Type responseType, List<HttpMessageConverter<?>> messageConverters) {
        super(responseType, messageConverters);
    }

    @Override
    protected MediaType getContentType(ClientHttpResponse response) {
        try {
            response.getHeaders().getContentType();
        }catch (IllegalArgumentException e){
            HttpHeaders httpHeaders=response.getHeaders();
            httpHeaders.set(HttpHeaders.CONTENT_TYPE,MediaType.TEXT_XML_VALUE);
            ClientHttpResponse newResponse = new ClientHttpResponse() {
                @Override
                public HttpStatus getStatusCode() throws IOException {
                    return response.getStatusCode();
                }

                @Override
                public int getRawStatusCode() throws IOException {
                    return response.getRawStatusCode();
                }

                @Override
                public String getStatusText() throws IOException {
                    return response.getStatusText();
                }

                @Override
                public void close() {

                }

                @Override
                public InputStream getBody() throws IOException {
                    return response.getBody();
                }

                @Override
                public HttpHeaders getHeaders() {
                    return httpHeaders;
                }
            };
            return super.getContentType(newResponse);
        }

        return super.getContentType(response);
    }
}
