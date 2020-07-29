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

/*

insert into cars(sn,address,car_number,phone,remark,money,owner)values('001','73-705','闽A-F3E20','18650368081','夜停',5000,0)
insert into cars(sn,address,car_number,phone,remark,money,owner)values('002','72-602','闽A-1V285','13959107687','极少',5000,0)
insert into cars(sn,address,car_number,phone,remark,money,owner)values('003','73-202','闽A-QB033','13489906250','大量',5000,0)
insert into cars(sn,address,car_number,phone,remark,money,owner)values('004','73-202','闽A-818PZ','15980703602','极少',5000,0)
insert into cars(sn,address,car_number,phone,remark,money,owner)values('005','72-705','闽A-5700G','15959026725','夜停',5000,0)
insert into cars(sn,address,car_number,phone,remark,money,owner)values('006','72-301','闽A-J1U06','13665058230','大量',5000,0)
insert into cars(sn,address,car_number,phone,remark,money,owner)values('007','73-706','闽A-E671W','13600889834','大量',5000,0)
insert into cars(sn,address,car_number,phone,remark,money,owner)values('008','73-706','闽A-2730A','13860611239','夜停',5000,0)
insert into cars(sn,address,car_number,phone,remark,money,owner)values('009','73-201','闽A-X0W09','18959197116','夜停',5000,0)
insert into cars(sn,address,car_number,phone,remark,money,owner)values('010','73-402','闽A-Y919J','15750896129','极少',5000,0)
insert into cars(sn,address,car_number,phone,remark,money,owner)values('011','72-506','闽A-181L1','18050330924','大量',5000,0)
insert into cars(sn,address,car_number,phone,remark,money,owner)values('012','72-401','闽A-G7B01','15280008668','极少',5000,0)
insert into cars(sn,address,car_number,phone,remark,money,owner)values('013','73-102','闽A-D8P82','13600860036','夜停',0,0)
insert into cars(sn,address,car_number,phone,remark,money,owner)values('015','72-606','闽A-7516N','13809543315','极少',5000,0)
insert into cars(sn,address,car_number,phone,remark,money,owner)values('016','73-505','闽A-257AG','15980661257','极少',5000,0)
insert into cars(sn,address,car_number,phone,remark,money,owner)values('017','73-602','鄂J-EM178','13409829720','大量',5000,0)
insert into cars(sn,address,car_number,phone,remark,money,owner)values('019','72-402','闽A-932FD','15959117561','极少',0,0)
insert into cars(sn,address,car_number,phone,remark,money,owner)values('020','73-103','闽K-61590','18106072527','极少',5000,0)
insert into cars(sn,address,car_number,phone,remark,money,owner)values('021','73-106','闽A-W616V','18559967711','大量',5000,0)
insert into cars(sn,address,car_number,phone,remark,money,owner)values('022','73-105','闽A-200AA','15980726586','大量',5000,0)
insert into cars(sn,address,car_number,phone,remark,money,owner)values('023','73-401','闽A-3169Z','15859003160','大量',5000,0)
insert into cars(sn,address,car_number,phone,remark,money,owner)values('024','73-303','闽A-S0N63','18305933415','大量',0,0)
insert into cars(sn,address,car_number,phone,remark,money,owner)values('031','73-303','闽A-M1S90','18305933415','大量',0,0)
insert into cars(sn,address,car_number,phone,remark,money,owner)values('025','72-605','闽A-J9X82','13696887820','大量',5000,0)
insert into cars(sn,address,car_number,phone,remark,money,owner)values('026','72-406','闽A-5563R','13665032711','夜停',5000,0)
insert into cars(sn,address,car_number,phone,remark,money,owner)values('027','72-703','闽A-JD992','18259191618','极少',5000,0)
insert into cars(sn,address,car_number,phone,remark,money,owner)values('028','73-503','闽A-MC889','13696865218','夜停',5000,0)
insert into cars(sn,address,car_number,phone,remark,money,owner)values('029','72-206','闽A-505G9','13799998656','夜停',5000,0)
insert into cars(sn,address,car_number,phone,remark,money,owner)values('030','73-404','闽A-MM986','13705950422','夜停',5000,0)
insert into cars(sn,address,car_number,phone,remark,money,owner)values('033','73-101','鄂J-KZ152','18120335022','大量',0,0)
insert into cars(sn,address,car_number,phone,remark,money,owner)values('034','73-203','闽A-7198L','18649867168','极少',5000,0)
insert into cars(sn,address,car_number,phone,remark,money,owner)values('035','73-403','闽A-M8R85','15392001320','夜停',5000,0)
insert into cars(sn,address,car_number,phone,remark,money,owner)values('036','72-505','闽A-712CC','13705952380','大量',5000,0)
insert into cars(sn,address,car_number,phone,remark,money,owner)values('037','72-701','闽A-9200P','18960845459','极少',5000,0)
insert into cars(sn,address,car_number,phone,remark,money,owner)values('038','72-306','闽A-Y772K','18959105523','极少',5000,0)
insert into cars(sn,address,car_number,phone,remark,money,owner)values('039','73-603','闽A-U372F','13763895616','大量',0,0)
insert into cars(sn,address,car_number,phone,remark,money,owner)values('040','73-703','闽A-D0B91','13763823972','极少',5000,0)
insert into cars(sn,address,car_number,phone,remark,money,owner)values('041','73-703','闽A-6FM59','13559197087','极少',5000,0)
* */