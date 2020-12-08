package com.chaoip.ipproxy.service;

import com.alipay.api.AlipayApiException;
import com.chaoip.ipproxy.controller.dto.ChargeDto;
import com.chaoip.ipproxy.controller.dto.SmsDto;
import com.chaoip.ipproxy.repository.ValidCodeRepository;
import com.chaoip.ipproxy.repository.entity.PayOrder;
import com.chaoip.ipproxy.repository.entity.ValidCode;
import com.chaoip.ipproxy.util.AliPayHelper;
import com.chaoip.ipproxy.util.SmsHelper;
import com.chaoip.ipproxy.util.StrHelper;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;

/**
 * 支付服务类
 */
@Service
@Slf4j
public class PayService {
    private final AliPayHelper aliPayHelper;

    public PayService(AliPayHelper aliPayHelper) {
        this.aliPayHelper = aliPayHelper;
    }

    /**
     * 申请支付，并返回支付宝url
     *
     * @param money 金额，单位分
     * @param name  账号
     * @return url
     */
    public String getPayUrl(ChargeDto money, String name) throws UnsupportedEncodingException, AlipayApiException, JsonProcessingException {
        PayOrder order = aliPayHelper.getPayUrl(name, money.getMoney(), money.getTitle(), money.getDescription());
        
        return order.getPayUrl();
    }
}
