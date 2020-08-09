package com.beinet.carsmanagement.carsTemp.cars.model;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
@Entity
public class CarsTemp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    /**
     * 住址
     */
    @NotBlank(message = "住址不能为空")
    @Length(max = 20, message = "地址最长20")
    String address;
    /**
     * 车牌
     */
    @NotBlank(message = "车牌不能为空")
    @Length(min = 7, max = 8, message = "车牌长度有误，只能7~8位")
    String carNumber;
    /**
     * 手机
     */
    @NotBlank(message = "手机不能为空")
    @Length(min = 11, max = 11, message = "手机必须11位")
    String phone;
    /**
     * 临时停放时长
     */
    @Max(value = 24, message = "最长不能超过24小时")
    @Min(value = 1, message = "最小不能低于1小时")
    int hour;

    @Column(updatable = false)
    private String ip;

    @Column(updatable = false)
    private String addTime;
}
