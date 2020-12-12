package com.chaoip.ipproxy.repository.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/**
 * 产品表
 *
 * @author youbl
 * @version 1.0
 * @date 2020/12/12 22:40
 */
@Data
@Builder
@Document
public class Product {
    /**
     * 主键
     */
    @Id
    private long id;

    /**
     * 支付类型，包月、包季度、包年等
     */
    private PayType type;

    /**
     * 每天提取数量
     */
    private int numPerDay;

    /**
     * 每次最多提取IP数量，累计一天不超过numPerDay
     */
    private int numPerTime;

    /**
     * 状态：0有效，1禁用
     */
    private int status;
    /**
     * 入库时间
     */
    private LocalDateTime creationTime;

    public enum PayType {
        MONTH,
        QUARTER,
        YEAR,
    }
}
