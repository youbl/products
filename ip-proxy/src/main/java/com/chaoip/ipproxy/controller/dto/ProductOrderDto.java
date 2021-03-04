package com.chaoip.ipproxy.controller.dto;

import com.chaoip.ipproxy.repository.entity.ProductOrder;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * ProductOrderDto
 *
 * @author youbl
 * @version 1.0
 * @date 2020/12/14 0:07
 */
@Data
public class ProductOrderDto {
    /**
     * 用户账号
     */
    private String name;

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 要购买的IP有效时长，为Product.ipValidTime数组的索引值
     */
    private int buyIpTime;

    /**
     * 购买时长，几个月
     */
    @Min(value = 1, message = "购买时长最小为1")
    @Max(value = 36, message = "购买时长最大为36")
    private int buyNum;

    /**
     * 购买的产品id
     */
    @Min(value = 1, message = "产品id不能小于1")
    private long productId;

    /**
     * 购买用途
     */
    private ProductOrder.OrderUsing using;
    /**
     * 用途描述
     */
    private String description;

    /**
     * 页面计算的价格，仅用于后端对比
     */
    private int payMoney;

    /**
     * 支付通道, 0余额，1支付宝，2微信
     */
    private int payType;


    /**
     * 第几页
     */
    private int pageNum;

    /**
     * 每页条数
     */
    private int pageSize;
}
