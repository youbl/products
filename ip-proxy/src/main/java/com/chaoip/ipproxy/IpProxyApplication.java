package com.chaoip.ipproxy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableMongoAuditing // 支持mongo的entity里的字段自动赋值
public class IpProxyApplication {

    public static void main(String[] args) {
        SpringApplication.run(IpProxyApplication.class, args);
    }

}
