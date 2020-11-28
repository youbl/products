package com.chaoip.ipproxy.controller.dto;

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
public class SmsDto {
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
