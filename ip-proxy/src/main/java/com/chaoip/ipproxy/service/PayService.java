package com.chaoip.ipproxy.service;

import com.chaoip.ipproxy.controller.dto.ChargeDto;
import com.chaoip.ipproxy.repository.PayOrderRepository;
import com.chaoip.ipproxy.repository.entity.OrderStatus;
import com.chaoip.ipproxy.repository.entity.PayOrder;
import com.chaoip.ipproxy.util.AliPayHelper;
import com.chaoip.ipproxy.util.WechatPay;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * 支付服务类
 */
@Service
@Slf4j
public class PayService {
    private final AliPayHelper aliPayHelper;
    private final WechatPay wechatPay;

    private final PayOrderRepository payOrderRepository;
    private final ApplicationEventPublisher eventPublisher; // 发送支付事件用

    public PayService(AliPayHelper aliPayHelper,
                      WechatPay wechatPay,
                      PayOrderRepository payOrderRepository,
                      ApplicationEventPublisher eventPublisher) {
        this.aliPayHelper = aliPayHelper;
        this.wechatPay = wechatPay;
        this.payOrderRepository = payOrderRepository;
        this.eventPublisher = eventPublisher;
    }

    /**
     * 申请支付，并返回支付宝url
     *
     * @param money 金额，单位分
     * @param name  账号
     * @return 订单
     */
    public PayOrder addOrder(ChargeDto money, String name) throws Exception {
        PayOrder order;
        if (money.getPayType() == 1) {
            order = aliPayHelper.getPayUrl(name, money.getMoney(), money.getTitle(), money.getDescription());
        } else {
            order = wechatPay.getPayUrl(name, money.getMoney(), money.getTitle() + money.getDescription());
        }
        return save(order);
    }

    private PayOrder save(PayOrder order) {
        return payOrderRepository.save(order);
    }

    public List<PayOrder> findOrder(String name) {
        if (StringUtils.isEmpty(name)) {
            return payOrderRepository.findByOrderByCreationTimeDesc();
        }
        return payOrderRepository.findByNameOrderByCreationTimeDesc(name);
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
    @Synchronized // 避免并发导致重复加钱
    @Transactional
    public boolean queryOrderStatus(String orderNo) throws Exception {
        PayOrder order = findByOrder(orderNo);
        if (order == null) {
            throw new RuntimeException("订单不存在: " + orderNo);
        }
        if (order.getMoney() <= 0) {
            throw new RuntimeException(orderNo + " 订单金额有误：" + order.getMoney());
        }
        if (order.getStatus() != OrderStatus.NO_PAY) {
            throw new RuntimeException("您的订单已处理完毕: " + orderNo);
        }

        boolean ret;
        if (order.getPayType() == 1) {
            ret = aliPayHelper.queryPayResult(order);
        } else {
            ret = wechatPay.queryPayResult(order);
        }
        order.setPayTime(LocalDateTime.now());
        if (ret) {
            order.setStatus(OrderStatus.SUCCESS);
        } else {
            order.setStatus(OrderStatus.FAIL);
        }
        order = payOrderRepository.save(order);
        if (order.getStatus() == OrderStatus.SUCCESS) {
            // 发送成功事件，要保存后，有id
            eventPublisher.publishEvent(order);
        }
        return ret;
    }

    /**
     * 管理员给用户加订单并充值
     *
     * @param money 金额
     */
    public void addMoneyAndOrder(ChargeDto money) {
        // 加订单流水
        String orderNo = money.getName() + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
        PayOrder order = PayOrder.builder()
                .orderNo(orderNo)
                .name(money.getName())
                .money(money.getMoney())
                .payUrl("")
                .payType(3)
                .title(money.getTitle())
                .description(money.getDescription())
                .status(OrderStatus.SUCCESS)
                .build();
        save(order);// 发送事件
        eventPublisher.publishEvent(order);
    }

}
