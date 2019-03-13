package com.invech.platform.modules.common.config;

import org.apache.commons.net.ftp.FTPClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName: FtpConfig
 * @Author: R.M.I
 * @CreateTime: 2019年03月11日 15:43:00
 * @Description: TODO
 * @Version 1.0.0
 */
@Configuration
public class FtpConfig {

    @Bean
    public FTPClient init(){
        return  new FTPClient();
    }
}
