package com.chaoip.ipproxy.util.config;

import lombok.Data;

/**
 * 微信支付配置
 *
 * @author youbl
 * @version 1.0
 * @date 2020/12/3 9:04
 */
@Data
public class WechatConfig {
    private String payurl;      // 下单接口API地址
    private String queryurl;    // 查询接口API地址
    private String appId;       // 商户AppId
    private String privateKey;  // 商户私钥
    private String mchId;       // 商户号
    private String mchSerialNo; // 商户证书序列号
    private String apiV3Key;    // V3秘钥

    private String callback;    // 回调地址
}
