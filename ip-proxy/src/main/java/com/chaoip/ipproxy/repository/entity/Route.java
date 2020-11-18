package com.chaoip.ipproxy.repository.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

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
     * 代理协议，如 http https socks5
     */
    private String protocal;
    /**
     * 代理服务所在地域
     */
    private String area;
    /**
     * 过期的绝对时间
     */
    private LocalDateTime expireTime;
}
