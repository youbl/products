package com.chaoip.ipproxy.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/**
 * 优惠项表
 *
 * @author youbl
 * @version 1.0
 * @date 2021/03/14 00:19
 */
@Data
@Builder
@Document
@AllArgsConstructor
@NoArgsConstructor
public class DisCount {
    /**
     * 主键
     */
    @Id
    private long id;

    /**
     * 优惠名称
     */
    @Indexed(unique = true)
    private String name;

    /**
     * 打折明细配置，数组
     */
    private OffConfig[] offConfigs;

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
    public static class OffConfig {
        /**
         * 优惠类型，立减、打折
         */
        private DisCountType type;
        /**
         * 优惠计算依据
         */
        private DisCountSource disCountSource;
        /**
         * 数量，如IP数，月数，总价(分)，由disCountSource决定
         */
        private int num;
        /**
         * 优惠内容，折扣百分比（93表示93折）或立减数额（分）
         */
        private int off;
    }

    /**
     * 优惠类型
     */
    public enum DisCountType {
        Reduce("立减"),
        Percent("打折");

        private final String title;

        DisCountType(String title) {
            this.title = title;
        }

        public String getTitle() {
            return title;
        }
    }

    /**
     * 优惠依据
     */
    public enum DisCountSource {
        /**
         * 例如超过1万个IP，总价打9折；超过2万个，总价打8折
         */
        Num("IP数优惠"),
        /**
         * 例如购买月数超过半年，总价打9折；超过1年，总价打8折
         */
        Month("月份优惠"),
        /**
         * 例如购买总价格超过1万，总价打9折；超过2万，总价打8折
         */
        Price("总价优惠"),
        ;
        private final String title;

        DisCountSource(String title) {
            this.title = title;
        }

        public String getTitle() {
            return title;
        }
    }
}
