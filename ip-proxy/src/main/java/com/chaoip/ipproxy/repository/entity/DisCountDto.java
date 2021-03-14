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
 * 优惠项表
 *
 * @author youbl
 * @version 1.0
 * @date 2021/03/14 09:19
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DisCountDto {

    private long id;

    /**
     * 优惠名称
     */
    private String name;

    /**
     * 打折明细配置，数组
     */
    private DisCount.OffConfig[] offConfigs;

    /**
     * 状态：0有效，1禁用
     */
    private int status;

    /**
     * 第几页
     */
    private int pageNum;

    /**
     * 每页条数
     */
    private int pageSize;

    public DisCount mapTo() {
        return DisCount.builder()
                .id(getId())
                .name(getName())
                .offConfigs(getOffConfigs())
                .status(getStatus())
                .build();
    }
}
