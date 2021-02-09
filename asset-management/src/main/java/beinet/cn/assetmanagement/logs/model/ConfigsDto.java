package beinet.cn.assetmanagement.logs.model;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Data
public class ConfigsDto {
    private int id;

    private String type;

    private String code;

    private String description;

    private java.time.LocalDateTime creationTime;

    public Configs mapTo() {
        Configs result = new Configs();
        result.setId(this.getId());
        result.setType(this.getType());
        result.setCode(this.getCode());
        result.setDescription(this.getDescription());
        result.setCreationTime(this.getCreationTime());
        return result;
    }
}