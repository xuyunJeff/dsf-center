package com.invech.platform.dsfcenterdao.config;

import com.github.pagehelper.autoconfigure.PageHelperAutoConfiguration;
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
