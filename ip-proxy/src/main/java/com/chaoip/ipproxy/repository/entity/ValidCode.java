package com.chaoip.ipproxy.repository.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/**
 * 用于校验的验证码
 *
 * @author youbl
 * @version 1.0
 * @date 2020/11/18 11:46
 */
@Document
@Builder
@Data
public class ValidCode {
    @Id
    private int id;
    /**
     * 验证码
     */
    private String code;
    /**
     * 序号
     */
    private String sn;
    /**
     * 状态，0为未验证，1为成功，2为失败
     */
    private int status;
    /**
     * 过期的绝对时间，并添加索引，
     * Mongo里，会每分钟扫描并自动删除数据，因此不是马上过期
     */
    @Indexed(expireAfterSeconds = 0)
    private LocalDateTime expireTime;
}
