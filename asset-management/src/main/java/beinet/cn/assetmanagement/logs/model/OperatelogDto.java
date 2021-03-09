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

    private java.time.LocalDateTime creationTime;

    public Operatelog mapTo() {
        Operatelog result = new Operatelog();
        result.setId(this.getId());
        result.setCode(this.getCode());
        result.setAccount(this.getAccount());
        result.setType(this.getType());
        result.setSubType(this.getSubType());
        result.setDescription(this.getDescription());
        result.setOperator(this.getOperator());
        result.setCreationTime(this.getCreationTime());
        return result;
    }
}