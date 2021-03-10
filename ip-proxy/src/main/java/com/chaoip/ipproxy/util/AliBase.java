package com.chaoip.ipproxy.util;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.chaoip.ipproxy.service.SiteConfigService;
import com.chaoip.ipproxy.util.config.AliConfigBase;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * AliBase
 *
 * @author youbl
 * @version 1.0
 * @date 2020/12/7 22:31
 */
public abstract class AliBase {
    protected final SiteConfigService configService;
    protected AliConfigBase config;

    public AliBase(SiteConfigService configService) {
        this.configService = configService;
    }

    public abstract AliConfigBase getConfig() throws Exception;

    protected AlipayClient getClient() throws Exception {
        if (this.config == null) {
            this.config = getConfig();
        }

        if (StringUtils.isEmpty(config.getUrl()) ||
                StringUtils.isEmpty(config.getAppId()) ||
                StringUtils.isEmpty(config.getPrivateKey()) ||
                StringUtils.isEmpty(config.getPublicKey())) {
            throw new RuntimeException("请先进行支付宝相关配置");
        }
        return new DefaultAlipayClient(config.getUrl(), config.getAppId(), config.getPrivateKey(),
                config.getFormat(), config.getCharset(), config.getPublicKey(), config.getSignType());
    }

    /**
     * 生成订单号，随机字符串
     *
     * @return 订单号
     */
    public static String getTransId() {
        String ret = "CDL" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
        ret += StrHelper.getRndStr(4, 1);
        return ret;
    }

    /**
     * 获取回调地址
     *
     * @param orderNo 订单号
     * @return url
     */
    public String getCallback(String orderNo) {
        String callback = checkUrl(config.getCallback());

        return callback + orderNo;
    }

    /**
     * 检查并完善地址
     *
     * @param url 地址
     * @return 修复的地址
     */
    protected String checkUrl(String url) {
        assert url != null;
        url = url.trim();
        assert !url.isEmpty();
        if (url.charAt(url.length() - 1) != '/')
            url += '/';
        return url;
    }
}
