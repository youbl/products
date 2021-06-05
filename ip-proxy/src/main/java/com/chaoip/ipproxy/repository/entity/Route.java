package com.chaoip.ipproxy.repository.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

import com.chaoip.ipproxy.util.CityHelper;

/**
 * Route
 *
 * @author youbl
 * @version 1.0
 * @date 2020/11/17 19:53
 */
@Data
@Builder
@Document(collection = "routes")
public class Route {
    /**
     * 主键
     */
    @Id
    private long id;

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
     * 代理所在省
     */
    private String province;
    /**
     * 代理服务所在地域
     */
    private String area;

    public String getAreaName() {
        return CityHelper.getByAreaCode(getArea());
    }

    /**
     * 运营商
     */
    private String operators;
    /**
     * 过期的绝对时间，并添加索引，
     * Mongo里，会每分钟扫描并自动删除数据，因此不是马上过期
     */
    @Indexed(expireAfterSeconds = 0)
    private LocalDateTime expireTime;

    /**
     * 入库时间
     */
    @CreatedDate
    private LocalDateTime creationTime;
    /**
     * 更新时间
     */
    @LastModifiedDate
    private LocalDateTime modifyTime;

    /**
     * 随机数，用于取IP时排序，避免结果不随机
     */
    @Indexed
    private Long rndNum;
}
