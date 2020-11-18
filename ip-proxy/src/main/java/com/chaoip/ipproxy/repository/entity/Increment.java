package com.chaoip.ipproxy.repository.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 用于保存每个表的自增id的集合
 *
 * @author youbl
 * @version 1.0
 * @date 2020/11/18 11:46
 */
@Document
@Data
public class Increment {
    @Id
    private String id;
    private String collection;
    private int currentId;
}
