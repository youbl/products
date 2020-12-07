package com.chaoip.ipproxy.repository;

import com.chaoip.ipproxy.repository.entity.RealOrder;

/**
 * 实名认证订单仓储类
 *
 * @author youbl
 * @version 1.0
 * @date 2020/11/17 19:56
 */
public interface RealOrderRepository extends BaseRepository<RealOrder, Long> {
    RealOrder findTopByNameOrderByCreationTimeDesc(String name);

    RealOrder findTopByOrderNo(String orderNo);
}
