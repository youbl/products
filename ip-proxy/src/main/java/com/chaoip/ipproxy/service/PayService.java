package com.chaoip.ipproxy.service;

import com.alipay.api.AlipayApiException;
import com.chaoip.ipproxy.controller.dto.ChargeDto;
import com.chaoip.ipproxy.repository.PayOrderRepository;
import com.chaoip.ipproxy.repository.entity.BeinetUser;
import com.chaoip.ipproxy.repository.entity.OrderStatus;
import com.chaoip.ipproxy.repository.entity.PayOrder;
import com.chaoip.ipproxy.security.BeinetUserService;
import com.chaoip.ipproxy.util.AliPayHelper;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
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
    public boolean queryOrderStatus(String orderNo) throws JsonProcessingException, AlipayApiException {
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

        boolean ret = aliPayHelper.queryPayResult(order);
        order.setPayTime(LocalDateTime.now());
        if (ret) {
            order.setStatus(OrderStatus.SUCCESS);
            addMoney(order.getName(), order.getMoney());
        } else {
            order.setStatus(OrderStatus.FAIL);
        }
        payOrderRepository.save(order);
        return ret;
    }

    /**
     * 管理员给用户充值
     *
     * @param money 金额
     * @return 用户
     */
    @Transactional
    public BeinetUser addMoneyAndOrder(ChargeDto money) {
        BeinetUser user = addMoney(money.getName(), money.getMoney());

        // 加订单流水
        String orderNo = money.getName() + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
        PayOrder order = PayOrder.builder()
                .orderNo(orderNo)
                .name(money.getName())
                .money(money.getMoney())
                .payUrl("")
                .title(money.getTitle())
                .description(money.getDescription())
                .status(OrderStatus.SUCCESS)
                .build();
        save(order);
        return user;
    }

    /**
     * 给指定 的账户添加金额
     *
     * @param name  账户
     * @param money 增加金额，可为负数
     */
    private BeinetUser addMoney(String name, int money) {
        BeinetUser user = beinetUserService.findByName(name, false);
        if (user == null) {
            throw new RuntimeException("充值失败：用户未找到:" + name);
        }
        user.setMoney(user.getMoney() + money);

        return beinetUserService.save(user);
    }
}
