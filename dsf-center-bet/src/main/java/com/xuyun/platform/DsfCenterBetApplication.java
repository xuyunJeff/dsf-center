package com.xuyun.platform;

import com.xuyun.platform.dsfcenterbetlog.crons.CreateMonthTableTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
@EnableCaching
public class DsfCenterBetApplication implements CommandLineRunner {
    @Autowired
    CreateMonthTableTask createMonthTableTask;

    public static void main(String[] args) {
        SpringApplication.run(DsfCenterBetApplication.class, args);

    }

    @Override
    public void run(String... args) throws Exception {
        createMonthTableTask.execute();
    }
}

