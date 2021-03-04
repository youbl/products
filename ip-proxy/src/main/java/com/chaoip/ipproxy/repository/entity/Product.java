package com.chaoip.ipproxy.repository.entity;

import lombok.AllArgsConstructor;
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
     * 支付类型，包月、包季度、包年、按量等
     */
    private PackageType type;

    /**
     * ip有效时长，数组
     */
    private IpTime[] ipValidTime;

    /**
     * 每单位价格，包月就是每月价格，包年就是每年价格.
     * 数组，与 ipValidTime 一一对应
     * 单位分（不是元）
     */
    private Integer[] moneyPerUnit;

    /**
     * 每日最大IP数
     */
    private int numPerDay;

    /**
     * 单次最多提取IP数量，累计一天不超过numPerDay
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

    @Data
    @AllArgsConstructor
    public static class IpTime {
        /**
         * ip有效时长最小值
         */
        private int minuteMin;
        /**
         * ip有效时长最大值
         */
        private int minuteMax;
    }

    /**
     * 产品类型，是包月，包年等
     */
    public enum PackageType {
        MONTH("包月"),
        QUARTER("包季"),
        YEAR("包年"),
        STREAM("按量");

        private String title;

        PackageType(String title) {
            this.title = title;
        }

        public String getTitle() {
            return title;
        }
    }
}
