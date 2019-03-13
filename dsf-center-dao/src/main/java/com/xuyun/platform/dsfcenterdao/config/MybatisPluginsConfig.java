package com.xuyun.platform.dsfcenterdao.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MybatisPluginsConfig {

    @Bean
    public MybatisInterceptor MybatisInterceptor(){
        return new MybatisInterceptor();
    }

//    @Bean
//    public PageHelperAutoConfiguration pageHelperAutoConfiguration (){
//        return new PageHelperAutoConfiguration();
//    }
}
