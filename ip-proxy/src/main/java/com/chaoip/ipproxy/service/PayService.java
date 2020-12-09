package com.chaoip.ipproxy.service;

import com.alipay.api.AlipayApiException;
import com.chaoip.ipproxy.controller.dto.ChargeDto;
import com.chaoip.ipproxy.repository.PayOrderRepository;
import com.chaoip.ipproxy.repository.entity.PayOrder;
import com.chaoip.ipproxy.util.AliPayHelper;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 支付服务类
 */
@Service
@Slf4j
public class PayService {
    private final AliPayHelper aliPayHelper;
    private final PayOrderRepository payOrderRepository;

    public PayService(AliPayHelper aliPayHelper, PayOrderRepository payOrderRepository) {
        this.aliPayHelper = aliPayHelper;
        this.payOrderRepository = payOrderRepository;
    }

    /**
     * 申请支付，并返回支付宝url
     *
     * @param money 金额，单位分
     * @param name  账号
     * @return 订单
     */
    public PayOrder addOrder(ChargeDto money, String name) throws AlipayApiException, JsonProcessingException {
        PayOrder order = aliPayHelper.getPayUrl(name, money.getMoney(), money.getTitle(), money.getDescription());
        return payOrderRepository.save(order);
    }

    /**
     * 根据订单号找订单
     *
     * @param orderNo 订单号
     * @return 订单记录
     */
    public PayOrder findByOrder(String orderNo) {
        return payOrderRepository.findByOrderNo(orderNo);
    }

    /**
     * 查询指定的订单是否支付成功
     *
     * @param orderNo 订单号
     * @return 成功与否
     */
    public boolean queryOrderStatus(String orderNo) throws JsonProcessingException, AlipayApiException {
        PayOrder order = findByOrder(orderNo);
        if (order == null) {
            throw new RuntimeException("订单不存在: " + orderNo);
        }
        if (order.getStatus() != PayOrder.PayStatus.NO_PAY) {
            throw new RuntimeException("您的订单已处理完毕: " + orderNo);
        }

        boolean ret = aliPayHelper.queryPayResult(order);
        order.setPayTime(LocalDateTime.now());
        if (ret) {
            order.setStatus(PayOrder.PayStatus.SUCCESS);
        } else {
            order.setStatus(PayOrder.PayStatus.FAIL);
        }
        payOrderRepository.save(order);
        return ret;
    }
}
