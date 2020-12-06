package com.chaoip.ipproxy.repository.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/**
 * 二维码短码列表
 *
 * @author youbl
 * @version 1.0
 * @date 2020/11/17 19:53
 */
@Data
@Builder
@Document
public class QrCode {
    /**
     * 主键
     */
    @Id
    private long id;

    /**
     * 用户账号
     */
    @Indexed
    private String name;

    private String realName;

    private String orderNo;

    private String identity;

    private String certId;
    /**
     * 支付宝认证地址
     */
    private String aliUrl;

    /**
     * 支付宝认证回调结果
     */
    private String result;

    /**
     * 入库时间
     */
    @CreatedDate
    private LocalDateTime creationTime;
}
