package com.chaoip.ipproxy.service;

import com.chaoip.ipproxy.controller.dto.RouteDto;
import com.chaoip.ipproxy.repository.entity.BeinetUser;
import com.chaoip.ipproxy.repository.entity.ProductOrder;
import com.chaoip.ipproxy.repository.entity.Route;
import com.chaoip.ipproxy.security.BeinetUserService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 提取IP单独定义的服务类
 */
@Service
public class IpGetService {
    private final ProductOrderService productOrderService;
    private final BeinetUserService userService;
    private final RouteService routeService;

    public IpGetService(ProductOrderService productOrderService, BeinetUserService userService, RouteService routeService) {
        this.productOrderService = productOrderService;
        this.userService = userService;
        this.routeService = routeService;
    }

    /**
     * 根据条件，提取IP数据
     *
     * @param condition 条件
     * @return 数据
     */
    public List<Route> getIpByOrder(RouteDto condition) {
        ProductOrder order = productOrderService.findValidOrder(condition.getOrderNo());
        BeinetUser user = userService.loadUserByUsername(order.getName());
        if (!user.getSecurity().equals(condition.getSign())) {
            throw new IllegalArgumentException("签名不匹配:" + condition.getSign());
        }

        int leftNum = (order.getIpNumPerDay() - order.getIpNumToday());// 剩余可提取个数
        if (leftNum <= 0) {
            throw new IllegalArgumentException("IP已提取完毕:" + condition.getOrderNo());
        }
        int limitNum = condition.getPageSize();
        if (limitNum <= 0) {
            limitNum = 10;
        }
        limitNum = Math.min(leftNum, limitNum);
        condition.setPageSize(limitNum);

        // 查路由数据
        List<Long> usedIdList = productOrderService.getRouteIdsByOrderNo(condition.getOrderNo());
        List<Route> ret = routeService.find(condition, usedIdList);
        if (ret == null || ret.isEmpty()) {
            return new ArrayList<>();
        }

        // 已提取IP插入明细表，并更新productOrder表的今日已提取IP数
        productOrderService.updateIpGetRecord(order, ret);
        return ret;
    }
}
