package com.chaoip.ipproxy.service;

import com.chaoip.ipproxy.controller.dto.ChargeDto;
import com.chaoip.ipproxy.repository.entity.BeinetUser;
import com.chaoip.ipproxy.repository.entity.PayOrder;
import com.chaoip.ipproxy.security.BeinetUserService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * ManagerService
 *
 * @author youbl
 * @version 1.0
 * @date 2020/12/9 14:15
 */
@Service
public class ManagerService {
    private final PayService payService;
    private final BeinetUserService userService;

    public ManagerService(PayService payService, BeinetUserService userService) {
        this.payService = payService;
        this.userService = userService;
    }

    /**
     * 为指定用户充值
     *
     * @param money 金额
     */
    public void chargeUser(ChargeDto money) {
        if (userService.findByName(money.getName(), false) == null) {
            throw new IllegalArgumentException("要充值的用户不存在: " + money.getName());
        }
        payService.addMoneyAndOrder(money);
    }

    public List<PayOrder> findCharges() {
        return payService.findOrder();
    }
}
