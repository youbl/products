package com.chaoip.ipproxy.util.config;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 阿里实名认证配置
 *
 * @author youbl
 * @version 1.0
 * @date 2020/12/3 9:04
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class VerifyConfig extends AliConfigBase {
    private String shortUrl;  // 实名认证短码二维码地址
}
