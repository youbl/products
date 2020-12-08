package com.chaoip.ipproxy.controller.dto;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

/**
 * 充值Dto
 *
 * @author youbl
 * @version 1.0
 * @date 2020/11/17 19:40
 */
@Data
public class ChargeDto {

    @Min(value = 100, message = "金额最少1元")
    @Max(value = 100000000, message = "金额最多100万元")
    private int money;
    @NotBlank(message = "说明不能为空")
    private String title;
    @NotBlank(message = "备注不能为空")
    private String description;
}
