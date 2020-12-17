package com.chaoip.ipproxy.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/**
 * 购买订单表
 *
 * @author youbl
 * @version 1.0
 * @date 2020/11/17 19:53
 */
@Data
@Builder
@AllArgsConstructor
@Document
public class ProductOrder {
    public ProductOrder() {

    }

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
     * 购买的产品id
     */
    private long productId;

    /**
     * 购买时长，如果是包月，就是几个月；如果是包年，就是几年。
     */
    private int buyNum;
    /**
     * 购买用途
     */
    private OrderUsing using;
    /**
     * 用途描述
     */
    private String description;
    /**
     * 待支付金额
     */
    private int money;

    /**
     * 支付通道, 0余额，1支付宝，2微信
     */
    private int payType;

    /**
     * 在线支付时生成的支付订单id
     */
    private long payOrderId;
    /**
     * 每天可提取IP数
     */
    private int ipNumPerDay;

    /**
     * 今天已提取IP数，不属于数据库字段
     */
    @Transient
    private int ipNumToday;

    /**
     * 支付成功时间
     */
    private LocalDateTime payTime;

    /**
     * 订单到期时间
     */
    private LocalDateTime endTime;

    /**
     * 支付状态
     */
    private OrderStatus status;

    /**
     * 入库时间
     */
    @CreatedDate
    private LocalDateTime creationTime;

    public enum OrderUsing {
        /**
         * 爬虫
         */
        CRAWLER,
        /**
         * 浏览器代理
         */
        BROWSER,
        /**
         * 网购、秒杀
         */
        BUYER,
        /**
         * 视频播放
         */
        VIDEO,
        /**
         * 软件用
         */
        SOFT,
        /**
         * 刷票
         */
        VOTE,
        /**
         * 发帖
         */
        BBS,
        /**
         * 游戏
         */
        GAME,
        /**
         * 注册
         */
        REGISTER,
        /**
         * QQ
         */
        QQ,
        /**
         * YY
         */
        YY,
        /**
         * 其它
         */
        OTHER,
    }
}
