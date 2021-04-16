package com.chaoip.ipproxy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableMongoAuditing // 支持mongo的entity里的字段自动赋值
@EnableAsync
public class IpProxyApplication {

    public static void main(String[] args) {
        SpringApplication.run(IpProxyApplication.class, args);
    }

}
