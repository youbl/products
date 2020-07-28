package com.beinet.carsmanagement.cars.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

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
    String address;
    /**
     * 车牌
     */
    String carNumber;
    /**
     * 手机
     */
    String phone;
    /**
     * 备注
     */
    String remark;
    /**
     * 是否户主
     */
    boolean owner;
    /**
     * 交费，单位分
     */
    int money;
}
