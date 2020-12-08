package com.chaoip.ipproxy.repository.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/**
 * 支付订单表
 *
 * @author youbl
 * @version 1.0
 * @date 2020/11/17 19:53
 */
@Data
@Builder
@Document
public class PayOrder {
    /**
     * 主键
     */
    @Id
    private long id;

    /**
     * 用户账号
     */
    @Indexed
    private String name;

    /**
     * 订单号
     */
    @Indexed(unique = true)
    private String orderNo;

    /**
     * 支付宝支付地址
     */
    private String payUrl;

    /**
     * 申请支付金额
     */
    private int money;

    /**
     * 支付成功时间
     */
    private LocalDateTime payTime;

    /**
     * 入库时间
     */
    @CreatedDate
    private LocalDateTime creationTime;
}
