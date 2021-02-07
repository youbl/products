package com.chaoip.ipproxy.service;

import com.chaoip.ipproxy.repository.SiteConfigRepository;
import com.chaoip.ipproxy.repository.entity.SiteConfig;
import com.chaoip.ipproxy.util.config.AliPayConfig;
import com.chaoip.ipproxy.util.config.SmsConfig;
import com.chaoip.ipproxy.util.config.VerifyConfig;
import com.chaoip.ipproxy.util.config.WechatConfig;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

/**
 * RouteService
 *
 * @author youbl
 * @version 1.0
 * @date 2020/11/17 19:57
 */
@Service
public class SiteConfigService {
    private static final String ALIPAY_KEY = "alipay";
    private static final String WECHATPAY_KEY = "wechatpay";
    private static final String SMS_KEY = "sms";
    private static final String VERIFY_KEY = "verify";

    private final SiteConfigRepository siteConfigRepository;
    private final ObjectMapper mapper;

    public SiteConfigService(SiteConfigRepository siteConfigRepository, ObjectMapper mapper) {
        this.siteConfigRepository = siteConfigRepository;
        this.mapper = mapper;
    }

    /**
     * 读取支付宝支付配置
     *
     * @return 配置
     * @throws JsonProcessingException 异常
     */
    public AliPayConfig getAliPayConfig() throws JsonProcessingException, InstantiationException, IllegalAccessException {
        return getConfig(ALIPAY_KEY, AliPayConfig.class);
    }

    /**
     * 保存支付宝支付配置
     *
     * @param config 配置
     * @throws JsonProcessingException 异常
     */
    public void savetAliPayConfig(AliPayConfig config) throws JsonProcessingException {
        saveConfig(config, ALIPAY_KEY);
    }

    /**
     * 读取微信支付配置
     *
     * @return 配置
     * @throws JsonProcessingException 异常
     */
    public WechatConfig getWechatConfig() throws JsonProcessingException, InstantiationException, IllegalAccessException {
        return getConfig(WECHATPAY_KEY, WechatConfig.class);
    }

    /**
     * 保存支付宝支付配置
     *
     * @param config 配置
     * @throws JsonProcessingException 异常
     */
    public void saveWechatConfig(WechatConfig config) throws JsonProcessingException {
        saveConfig(config, WECHATPAY_KEY);
    }

    /**
     * 读取短信配置
     *
     * @return 配置
     * @throws JsonProcessingException 异常
     */
    public SmsConfig getSmsConfig() throws JsonProcessingException, InstantiationException, IllegalAccessException {
        return getConfig(SMS_KEY, SmsConfig.class);
    }

    /**
     * 保存短信配置
     *
     * @param config 配置
     * @throws JsonProcessingException 异常
     */
    public void savetSmsConfig(SmsConfig config) throws JsonProcessingException {
        saveConfig(config, SMS_KEY);
    }

    /**
     * 读取支付宝实名认证配置
     *
     * @return 配置
     * @throws JsonProcessingException 异常
     */
    public VerifyConfig getVerifyConfig() throws JsonProcessingException, InstantiationException, IllegalAccessException {
        return getConfig(VERIFY_KEY, VerifyConfig.class);
    }

    /**
     * 保存支付宝实名认证配置
     *
     * @param config 配置
     * @throws JsonProcessingException 异常
     */
    public void savetVerifyConfig(VerifyConfig config) throws JsonProcessingException {
        saveConfig(config, VERIFY_KEY);
    }

    private <T> T getConfig(String key, Class<T> clazz) throws JsonProcessingException, IllegalAccessException, InstantiationException {
        SiteConfig record = siteConfigRepository.findByKey(key);
        if (record != null) {
            return mapper.readValue(record.getValue(), clazz);
        }
        return clazz.newInstance();
    }

    private void saveConfig(Object config, String key) throws JsonProcessingException {
        SiteConfig record = siteConfigRepository.findByKey(key);
        if (record == null) {
            record = new SiteConfig();
            record.setKey(key);
        }
        record.setValue(mapper.writeValueAsString(config));
        siteConfigRepository.save(record);
    }
}
