package com.chaoip.ipproxy.controller.dto;

import com.chaoip.ipproxy.repository.entity.Route;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.util.StringUtils;
import com.chaoip.ipproxy.util.CityHelper;

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
        // empty
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
     * 代理协议，如 http socks
     */
    @Pattern(regexp = "^(http|socks)$", message = "协议只支持 http,socks")
    private String protocal;
    /**
     * 代理服务所在地域
     */
    @NotNull(message = "地域不能为空")
    private String area;
    /**
     * 服务运营商
     */
    @Pattern(regexp = "^(unicom|mobile|telecom)$", message = "运营商只支持 unicom,mobile,telecom")
    private String operators;
    /**
     * 过期时间，单位秒
     */
    @Min(value = 10, message = "过期时间不能小于10")
    private int expireTime;

    public Route mapTo() {
        String operatorName = "";

        if (getOperators() != null) {
            switch (getOperators()) {
                case "unicom":
                    operatorName = "中国联通";
                    break;
                case "mobile":
                    operatorName = "中国移动";
                    break;
                case "telecom":
                    operatorName = "中国电信";
                    break;
                default:
                    throw new IllegalArgumentException("无效的运营商: " + getOperators());
            }
        }
        String cityName = "";
        if (getArea() != null) {
            cityName = CityHelper.getByAreaCode(getArea());
            if (StringUtils.isEmpty(cityName)) {
                throw new IllegalArgumentException("无效的城市区号: " + getArea());
            }
        }
        return Route.builder()
                .id(getId())
                .ip(getIp())
                .port(getPort())
                .protocal(getProtocal())
                .area(cityName)
                .operators(operatorName)
                .expireTime(LocalDateTime.now().plusSeconds(getExpireTime()))
                .build();
    }
}
