package com.chaoip.ipproxy.service;

import com.chaoip.ipproxy.repository.SiteConfigRepository;
import com.chaoip.ipproxy.repository.entity.SiteConfig;
import com.chaoip.ipproxy.util.config.AliPayConfig;
import com.chaoip.ipproxy.util.config.SmsConfig;
import com.chaoip.ipproxy.util.config.VerifyConfig;
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
    public AliPayConfig getAliPayConfig() throws JsonProcessingException {
        SiteConfig record = siteConfigRepository.findByKey(ALIPAY_KEY);
        if (record != null) {
            return mapper.readValue(record.getValue(), AliPayConfig.class);
        }
        return new AliPayConfig();
    }

    /**
     * 保存支付宝支付配置
     *
     * @param config 配置
     * @throws JsonProcessingException 异常
     */
    public void savetAliPayConfig(AliPayConfig config) throws JsonProcessingException {
        SiteConfig record = siteConfigRepository.findByKey(ALIPAY_KEY);
        if (record == null) {
            record = new SiteConfig();
            record.setKey(ALIPAY_KEY);
        }
        record.setValue(mapper.writeValueAsString(config));
        siteConfigRepository.save(record);
    }

    /**
     * 读取短信配置
     *
     * @return 配置
     * @throws JsonProcessingException 异常
     */
    public SmsConfig getSmsConfig() throws JsonProcessingException {
        SiteConfig record = siteConfigRepository.findByKey(SMS_KEY);
        if (record != null) {
            return mapper.readValue(record.getValue(), SmsConfig.class);
        }
        return new SmsConfig();
    }

    /**
     * 保存短信配置
     *
     * @param config 配置
     * @throws JsonProcessingException 异常
     */
    public void savetSmsConfig(SmsConfig config) throws JsonProcessingException {
        SiteConfig record = siteConfigRepository.findByKey(SMS_KEY);
        if (record == null) {
            record = new SiteConfig();
            record.setKey(SMS_KEY);
        }
        record.setValue(mapper.writeValueAsString(config));
        siteConfigRepository.save(record);
    }

    /**
     * 读取支付宝实名认证配置
     *
     * @return 配置
     * @throws JsonProcessingException 异常
     */
    public VerifyConfig getVerifyConfig() throws JsonProcessingException {
        SiteConfig record = siteConfigRepository.findByKey(VERIFY_KEY);
        if (record != null) {
            return mapper.readValue(record.getValue(), VerifyConfig.class);
        }
        return new VerifyConfig();
    }

    /**
     * 保存支付宝实名认证配置
     *
     * @param config 配置
     * @throws JsonProcessingException 异常
     */
    public void savetVerifyConfig(VerifyConfig config) throws JsonProcessingException {
        SiteConfig record = siteConfigRepository.findByKey(VERIFY_KEY);
        if (record == null) {
            record = new SiteConfig();
            record.setKey(VERIFY_KEY);
        }
        record.setValue(mapper.writeValueAsString(config));
        siteConfigRepository.save(record);
    }
}
