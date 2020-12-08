package com.chaoip.ipproxy.repository.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/**
 * 站点配置表
 *
 * @author youbl
 * @version 1.0
 * @date 2020/11/17 19:53
 */
@Data
@Document
public class SiteConfig {
    /**
     * 主键
     */
    @Id
    private long id;

    /**
     * 配置的key
     */
    @Indexed(unique = true)
    private String key;

    /**
     * 配置的value
     */
    private String value;
}
