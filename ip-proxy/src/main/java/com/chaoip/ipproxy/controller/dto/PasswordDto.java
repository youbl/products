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
public class PasswordDto {

    @NotBlank(message = "旧密码不能为空")
    private String passwordOld;
    @NotBlank(message = "新密码不能为空")
    private String password;
    @NotBlank(message = "确认密码不能为空")
    private String passwordConfirm;
}
