package com.chaoip.ipproxy.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.*;

/**
 * SmsDto
 *
 * @author youbl
 * @version 1.0
 * @date 2020/11/17 19:40
 */
@Data
@Builder
@AllArgsConstructor
public class SmsDto {
    public SmsDto() {
    }

    /**
     * 输入的手机号
     */
    @Pattern(regexp = "^1\\d{10}$", message = "无效的手机号")
    private String phone;
    /**
     * 输入的验证码
     */
    @NotEmpty(message = "验证码不能为空")
    private String code;
    /**
     * 对应的序号
     */
    @NotEmpty(message = "序号不能为空")
    private String sn;
}
