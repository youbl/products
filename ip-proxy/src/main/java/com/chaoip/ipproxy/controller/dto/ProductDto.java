package com.chaoip.ipproxy.controller.dto;

import com.chaoip.ipproxy.repository.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {
    /**
     * 主键
     */
    private long id;

    /**
     * 产品名称
     */
    private String name;

    /**
     * 支付类型，包月、包季度、包年等
     */
    private Product.PackageType type;

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


    /**
     * 第几页
     */
    private int pageNum;

    /**
     * 每页条数
     */
    private int pageSize;
}
