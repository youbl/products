package com.chaoip.ipproxy.service;

import com.chaoip.ipproxy.repository.RealOrderRepository;
import com.chaoip.ipproxy.repository.entity.RealOrder;
import org.springframework.stereotype.Service;

/**
 * 实名认证服务类
 *
 * @author youbl
 * @version 1.0
 * @date 2020/11/17 19:57
 */
@Service
public class RealOrderService {
    private final RealOrderRepository realOrderRepository;

    public RealOrderService(RealOrderRepository realOrderRepository) {
        this.realOrderRepository = realOrderRepository;
    }

    /**
     * 插入一个二维码
     *
     * @param code 阿里地址
     * @return 结果
     */
    public RealOrder addOrder(RealOrder code) {
        return realOrderRepository.save(code);
    }

    public RealOrder findByName(String name) {
        return realOrderRepository.findTopByNameOrderByCreationTimeDesc(name);
    }

    public RealOrder findByOrder(String orderNo) {
        return realOrderRepository.findTopByOrderNo(orderNo);
    }
}
