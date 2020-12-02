package com.chaoip.ipproxy.controller.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * RouteDto
 *
 * @author youbl
 * @version 1.0
 * @date 2020/11/17 19:40
 */
@Data
public class IdentifyDto {
    @NotBlank(message = "姓名不能为空")
    private String realName;
    @NotBlank(message = "身份证不能为空")
    private String identity;
    @NotBlank(message = "图形码SN不能为空")
    private String imgSn;
    @NotBlank(message = "图形验证码不能为空")
    private String imgCode;
}
