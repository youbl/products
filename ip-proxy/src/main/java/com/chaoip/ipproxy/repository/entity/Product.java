package com.chaoip.ipproxy.repository.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
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
     * 产品名称
     */
    @Indexed(unique = true)
    private String name;

    /**
     * 支付类型，包月、包季度、包年等
     */
    private PackageType type;

    /**
     * 每天提取数量
     */
    private int numPerDay;

    /**
     * 每次最多提取IP数量，累计一天不超过numPerDay
     */
    private int numPerTime;

    /**
     * 每单位价格，包月就是每月价格，包年就是每年价格.
     * 单位分
     */
    private int moneyPerUnit;

    /**
     * 状态：0有效，1禁用
     */
    private int status;
    /**
     * 入库时间
     */
    private LocalDateTime creationTime;

    /**
     * 产品类型，是包月，包年等
     */
    public enum PackageType {
        MONTH,
        QUARTER,
        YEAR,
    }
}
