package com.chaoip.ipproxy.service;

import com.alipay.api.AlipayApiException;
import com.chaoip.ipproxy.controller.dto.ChargeDto;
import com.chaoip.ipproxy.repository.PayOrderRepository;
import com.chaoip.ipproxy.repository.entity.BeinetUser;
import com.chaoip.ipproxy.repository.entity.PayOrder;
import com.chaoip.ipproxy.security.BeinetUserService;
import com.chaoip.ipproxy.util.AliPayHelper;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 支付服务类
 */
@Service
@Slf4j
public class PayService {
    private final AliPayHelper aliPayHelper;
    private final PayOrderRepository payOrderRepository;
    private final BeinetUserService beinetUserService;   // 用于加减余额

    public PayService(AliPayHelper aliPayHelper,
                      PayOrderRepository payOrderRepository,
                      BeinetUserService beinetUserService) {
        this.aliPayHelper = aliPayHelper;
        this.payOrderRepository = payOrderRepository;
        this.beinetUserService = beinetUserService;
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

    public List<PayOrder> findOrder(String name) {
        List<PayOrder> ret = payOrderRepository.findByNameOrderByCreationTimeDesc(name);
        ret.stream().forEach(item -> {
            item.setPayUrl(""); // 不返回支付地址
        });
        return ret;
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
    public boolean queryOrderStatus(String orderNo) throws JsonProcessingException, AlipayApiException {
        PayOrder order = findByOrder(orderNo);
        if (order == null) {
            throw new RuntimeException("订单不存在: " + orderNo);
        }
        if (order.getMoney() <= 0) {
            throw new RuntimeException(orderNo + " 订单金额有误：" + order.getMoney());
        }
        if (order.getStatus() != PayOrder.PayStatus.NO_PAY) {
            throw new RuntimeException("您的订单已处理完毕: " + orderNo);
        }

        boolean ret = aliPayHelper.queryPayResult(order);
        order.setPayTime(LocalDateTime.now());
        if (ret) {
            order.setStatus(PayOrder.PayStatus.SUCCESS);
            addMoney(order.getName(), order.getMoney());
        } else {
            order.setStatus(PayOrder.PayStatus.FAIL);
        }
        payOrderRepository.save(order);
        return ret;
    }

    /**
     * 给指定 的账户添加金额
     *
     * @param name  账户
     * @param money 增加金额，可为负数
     */
    void addMoney(String name, int money) {
        BeinetUser user = beinetUserService.findByName(name);
        if (user == null) {
            throw new RuntimeException("充值失败：用户未找到:" + name);
        }
        if (user.getMoney() == null) {
            user.setMoney(0);
        }
        user.setMoney(user.getMoney() + money);

        beinetUserService.save(user);
    }
}
