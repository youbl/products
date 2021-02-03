package beinet.cn.assetmanagement.logs.model;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Data
public class OperatelogDto {
    private int id;

    private String code;

    private int userId;

    private String type;

    private String description;

    private int operator;

    private java.time.LocalDateTime creationTime;

    public Operatelog mapTo() {
        Operatelog result = new Operatelog();
        result.setId(this.getId());
        result.setCode(this.getCode());
        result.setUserId(this.getUserId());
        result.setType(this.getType());
        result.setDescription(this.getDescription());
        result.setOperator(this.getOperator());
        result.setCreationTime(this.getCreationTime());
        return result;
    }
}