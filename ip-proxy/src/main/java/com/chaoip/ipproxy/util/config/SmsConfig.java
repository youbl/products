package com.chaoip.ipproxy.util.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * SmsConfig
 *
 * @author youbl
 * @version 1.0
 * @date 2020/12/3 9:04
 */
@Component
@ConfigurationProperties(prefix = "ali.sms")
@Data
public class SmsConfig {
    private String domain;
    private String version;
    private String region;
    private String ak;
    private String sk;
    private String sign;
    private String template;
}
