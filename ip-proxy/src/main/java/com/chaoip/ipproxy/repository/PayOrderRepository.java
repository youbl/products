package com.chaoip.ipproxy.repository;

import com.chaoip.ipproxy.repository.entity.PayOrder;

import java.util.List;

/**
 * 站点配置仓储
 *
 * @author youbl
 * @version 1.0
 * @date 2020/11/17 19:56
 */
public interface PayOrderRepository extends BaseRepository<PayOrder, Long> {
    /**
     * 根据订单号找订单
     *
     * @param orderNo 订单号
     * @return 订单记录
     */
    PayOrder findByOrderNo(String orderNo);

    List<PayOrder> findByNameOrderByCreationTimeDesc(String name);
}
