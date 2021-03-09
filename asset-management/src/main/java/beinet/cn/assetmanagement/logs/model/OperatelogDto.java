package beinet.cn.assetmanagement.logs.model;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Data
public class OperatelogDto {
    private int id;

    private String code;

    private String account;

    private OperateEnum type;

    private String subType;

    private String description;

    private String operator;

    private String ip;

    private java.time.LocalDateTime creationTime;

    private int pageNum;
    private int pageSize;

}