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
@ConfigurationProperties(prefix = "ali.verify")
@Data
public class VerifyConfig {
    private String url;
    private String appId;
    private String privateKey;
    private String publicKey;
    private String charset;
    private String signType;
    private String format;
}
