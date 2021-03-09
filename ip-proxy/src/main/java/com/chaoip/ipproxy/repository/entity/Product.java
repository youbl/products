package com.chaoip.ipproxy.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
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
@AllArgsConstructor
@NoArgsConstructor
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
     * 支付类型，包年包月、按量
     */
    private PackageType type;

    /**
     * ip有效时长和价格，数组
     */
    private IpTime[] ipValidTime;

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
    @NoArgsConstructor
    @Builder
    public static class IpTime {
        /**
         * ip有效时长最小值
         */
        private int minuteMin;
        /**
         * ip有效时长最大值
         */
        private int minuteMax;

        /**
         * 每千个IP单价，单位分（不是元)
         */
        private int price;
    }

    /**
     * 产品类型，是包月，包年等
     */
    public enum PackageType {
        MONTH("包月"),
        STREAM("按量");

        private final String title;

        PackageType(String title) {
            this.title = title;
        }

        public String getTitle() {
            return title;
        }
    }
}
