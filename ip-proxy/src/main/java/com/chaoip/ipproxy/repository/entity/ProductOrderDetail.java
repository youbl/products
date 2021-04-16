package com.chaoip.ipproxy.repository.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/**
 * 订单IP提取详情表
 *
 * @author youbl
 * @version 1.0
 * @date 2020/11/17 19:53
 */
@Data
@Builder
@Document
// 建联合索引, 1为正序，-1为倒序
@CompoundIndexes({
        @CompoundIndex(name = "idx_order_time", def = "{orderNo:1, creationTime:-1}", unique = false)
})
public class ProductOrderDetail {
    /**
     * 主键
     */
    @Id
    private long id;

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 入库时间
     */
    private LocalDateTime creationTime;

    // 以下为代理信息备份

    /**
     * 代理表id
     */
    private long routeId;

    /**
     * 代理服务IP
     */
    private String ip;
    /**
     * 代理端口
     */
    private int port;
    /**
     * 代理协议，如 http socks
     */
    private String protocal;
    /**
     * 代理服务所在地域
     */
    private String area;
    /**
     * 运营商
     */
    private String operators;

    /**
     * 代理过期的绝对时间
     */
    private LocalDateTime expireTime;

}
