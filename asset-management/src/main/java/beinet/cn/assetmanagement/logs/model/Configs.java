package beinet.cn.assetmanagement.logs.model;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
// <groupId>org.hibernate.validator</groupId><artifactId>hibernate-validator</artifactId>
import javax.validation.constraints.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@DynamicInsert
@DynamicUpdate
@Entity
@Table(name = "Configs", catalog = "assets")
public class Configs {
    @Id
    @Column(columnDefinition = "int(11) COMMENT 'Id'")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Size(max = 20)
    @Column(columnDefinition = "varchar(20) COMMENT '配置类型'")
    private String type;

    @Size(max = 20)
    @Column(columnDefinition = "varchar(20) COMMENT '配置键值'")
    private String code;

    @Size(max = 100)
    @Column(columnDefinition = "varchar(100) COMMENT '配置说明'")
    private String description;

    @Column(columnDefinition = "datetime COMMENT '创建时间'", insertable = false, updatable = false)
    private java.time.LocalDateTime creationTime;

    public ConfigsDto mapTo() {
        ConfigsDto result = new ConfigsDto();
        result.setId(this.getId());
        result.setType(this.getType());
        result.setCode(this.getCode());
        result.setDescription(this.getDescription());
        result.setCreationTime(this.getCreationTime());
        return result;
    }
/*
INSERT INTO assets.Configs (
  type, code, description
)VALUES(
  :type, :code, :description
);

UPDATE assets.Configs SET
  type = :type, code = :code, description = :description
WHERE ;
*/
}