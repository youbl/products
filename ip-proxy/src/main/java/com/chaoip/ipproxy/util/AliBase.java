package com.chaoip.ipproxy.util;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.chaoip.ipproxy.util.config.VerifyConfig;

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
    protected final VerifyConfig config;

    public AliBase(VerifyConfig verifyConfig) {
        this.config = verifyConfig;
    }

    protected AlipayClient getClient() {
        return new DefaultAlipayClient(config.getUrl(), config.getAppId(), config.getPrivateKey(),
                config.getFormat(), config.getCharset(), config.getPublicKey(), config.getSignType());
    }

    /**
     * 生成订单号，随机字符串
     *
     * @return 订单号
     */
    protected static String getTransId() {
        String ret = "CHDL" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
        ret += StrHelper.getRndStr(4, 1);
        return ret;
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
