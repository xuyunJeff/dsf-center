package com.invech.platform.dsfcenterservice.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


/**
 * MVC配置
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private AuthTokenFilter authTokenFilter;
//    @Autowired
//    private LoginUserHandlerMethodArgumentResolver loginUserHandlerMethodArgumentResolver;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
       // registry.addInterceptor(authTokenFilter).addPathPatterns("/api/**").excludePathPatterns("/api/site/getSchemaNameSecurityCode");
        registry.addInterceptor(authTokenFilter).addPathPatterns("/api/**").excludePathPatterns("/api/site/enableSite").excludePathPatterns("/api/admin/betlogs");
    }

//    @Override
//    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
//        argumentResolvers.add(loginUserHandlerMethodArgumentResolver);
//    }
}