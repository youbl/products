package com.beinet.carsmanagement.cars.model;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.*;

@Data
@Entity
public class Cars {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    /**
     * 序号，用于停车证
     */
    int sn;
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
     * 备注
     */
    @Length(max = 50, message = "备注最多50位")
    String remark;
    /**
     * 是否户主
     */
    boolean owner;
    /**
     * 交费，单位分
     */
    @Max(value = 1000000, message = "金额不能超过1万元")
    @Min(value = 0, message = "金额不能小于0")
    int money;

    @Column(updatable = false)
    private String ip;

    @Column(updatable = false)
    private String addTime;
}
