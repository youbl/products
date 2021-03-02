package com.chaoip.ipproxy.repository.entity;

import lombok.Data;

/**
 * 支付订单表
 *
 * @author youbl
 * @version 1.0
 * @date 2020/11/17 19:53
 */
@Data
public class PayOrderDto {
    /**
     * 用户账号
     */
    private String name;

    /**
     * 订单号
     */
    private String orderNo;

    private String title;

    /**
     * 第几页
     */
    private int pageNum;

    /**
     * 每页条数
     */
    private int pageSize;
}
