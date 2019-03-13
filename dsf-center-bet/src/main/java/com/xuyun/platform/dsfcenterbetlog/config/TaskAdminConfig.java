package com.xuyun.platform.dsfcenterbetlog.config;

import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * task config 任务调度器初始化
 */
@Configuration
@ComponentScan(basePackages = "com.invech.platform.dsfcenterbetlog.crons")
@Slf4j
public class TaskAdminConfig {

    @Value("${task.admin.addresses}")
    private String adminAddresses;

    @Value("${task.executor.appname}")
    private String appName;

    @Value("${task.executor.ip}")
    private String ip;

    @Value("${task.executor.port}")
    private int port;

    @Value("${task.accessToken}")
    private String accessToken;

    @Value("${task.executor.logpath}")
    private String logPath;

    @Value("${task.executor.logretentiondays}")
    private int logRetentionDays;


    @Bean(initMethod = "start", destroyMethod = "destroy")
    public XxlJobSpringExecutor xxlJobExecutor() {
        log.info("注册到 xxl-job调度器  config init.");
        XxlJobSpringExecutor taskExecutor = new XxlJobSpringExecutor();
        taskExecutor.setAdminAddresses(adminAddresses);
        taskExecutor.setAppName(appName);
        taskExecutor.setIp(ip);
        taskExecutor.setPort(port);
        taskExecutor.setAccessToken(accessToken);
        taskExecutor.setLogPath(logPath);
        taskExecutor.setLogRetentionDays(logRetentionDays);
        return taskExecutor;
    }

}