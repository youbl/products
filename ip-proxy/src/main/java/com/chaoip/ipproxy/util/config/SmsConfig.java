package com.chaoip.ipproxy.util.config;

import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * 短信配置
 *
 * @author youbl
 * @version 1.0
 * @date 2020/12/3 9:04
 */
@Component
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
