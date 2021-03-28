package com.chaoip.ipproxy.service;

import com.chaoip.ipproxy.event.PublishHelper;
import com.chaoip.ipproxy.repository.entity.BeinetUser;
import com.chaoip.ipproxy.security.BeinetUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 用户余额操作服务类
 */
@Service
@Slf4j
public class UserMoneyService {
    private final BeinetUserService userService;

    public UserMoneyService(BeinetUserService userService) {
        this.userService = userService;
    }

    /**
     * 给指定用户加钱，可以为负数，即扣钱
     *
     * @param name  用户名
     * @param money 金额
     * @return 用户
     */
    public BeinetUser addMoney(String name, int money) {
        BeinetUser user = userService.loadUserByUsername(name);
        int moneyReal = user.getMoney() + money;
        user.setMoney(moneyReal);

        user = userService.save(user);
        String message = name + " 充值 " + money + " 充值后余额 " + moneyReal;
        PublishHelper.publish(PublishHelper.EventType.AddMoney, message);
        return user;
    }

}
