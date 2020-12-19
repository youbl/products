package com.chaoip.ipproxy.controller.dto;

import com.chaoip.ipproxy.repository.entity.Route;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
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
@NoArgsConstructor
@AllArgsConstructor
public class RouteDto {
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

    private String province;

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


    /**
     * 提取ip用的签名
     */
    private String sign;
    /**
     * 产品订单编号，用于提取ip
     */
    private String orderNo;
    /**
     * 提取输出格式，text json
     */
    private String outputType;

    /**
     * 第几页
     */
    private int pageNum;
    /**
     * 每页条数
     */
    private int pageSize;

    public Route mapTo() {
        if (getOperators() != null) {
            switch (getOperators()) {
                case "unicom":
                case "mobile":
                case "telecom":
                    break;
                default:
                    throw new IllegalArgumentException("无效的运营商: " + getOperators());
            }
        }
        String province = "";
        if (getArea() != null) {
            String[] cityNames = CityHelper.getArrByAreaCode(getArea());
            if (cityNames == null) {
                throw new IllegalArgumentException("无效的城市区号: " + getArea());
            }
            province = cityNames[1];
        }
        return Route.builder()
                .id(getId())
                .ip(getIp())
                .port(getPort())
                .protocal(getProtocal())
                .area(getArea())
                .province(province)
                .operators(getOperators())
                .expireTime(LocalDateTime.now().plusSeconds(getExpireTime()))
                .build();
    }
}
