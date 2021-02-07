package com.chaoip.ipproxy.util.config;

import lombok.Data;

/**
 * 阿里实名认证配置
 *
 * @author youbl
 * @version 1.0
 * @date 2020/12/3 9:04
 */
@Data
public class AliConfigBase {
    private String url;         // API地址
    private String appId;
    private String privateKey;
    private String publicKey;
    private String charset;
    private String signType;
    private String format;

    private String callback;  // 回调地址
}
