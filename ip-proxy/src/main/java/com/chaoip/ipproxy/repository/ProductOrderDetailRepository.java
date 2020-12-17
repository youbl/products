package com.chaoip.ipproxy.repository;

import com.chaoip.ipproxy.repository.entity.PayOrder;
import com.chaoip.ipproxy.repository.entity.ProductOrderDetail;

import java.time.LocalDateTime;
import java.util.List;

/**
 * IP提取记录表仓储类
 *
 * @author youbl
 * @version 1.0
 * @date 2020/11/17 19:56
 */
public interface ProductOrderDetailRepository extends BaseRepository<ProductOrderDetail, Long> {
    /**
     * 统计时间范围内，某订单的记录数
     *
     * @param orderNo   订单
     * @param startTime 时间起始
     * @param endTime   时间结束
     * @return 总数
     */
    int countByOrderNoAndCreationTimeBetween(String orderNo, LocalDateTime startTime, LocalDateTime endTime);
}
