package com.chaoip.ipproxy.controller.dto;

import com.chaoip.ipproxy.repository.entity.Route;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

/**
 * RouteDto
 *
 * @author youbl
 * @version 1.0
 * @date 2020/11/17 19:40
 */
@Data
@Builder
@AllArgsConstructor
public class RouteDto {
    public RouteDto() {

    }

    private int id;

    /**
     * 代理服务IP
     */
    @Pattern(regexp = "^\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}$", message = "IP格式有误")
    private String ip;
    /**
     * 代理端口
     */
    @Min(value = 10, message = "端口不能小于10")
    @Max(value = 65535, message = "端口不能大于65535")
    private int port;
    /**
     * 代理协议，如 http https socks5
     */
    @Pattern(regexp = "^(https?|socks5)$", message = "协议只支持 http|https|socks5")
    private String protocal;
    /**
     * 代理服务所在地域
     */
    @NotNull(message = "地域不能为空")
    private String area;
    /**
     * 服务运营商
     */
    @NotNull(message = "运营商不能为空")
    private String operators;
    /**
     * 过期的绝对时间
     */
    @NotNull(message = "过期时间不能为空")
    private LocalDateTime expireTime;

    public Route mapTo() {
        return Route.builder()
                .id(getId())
                .ip(getIp())
                .port(getPort())
                .protocal(getProtocal())
                .area(getArea())
                .operators(getOperators())
                .expireTime(getExpireTime())
                .build();
    }
}
