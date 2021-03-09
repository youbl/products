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
@Table(name = "Operatelog", catalog = "assets")
public class Operatelog {
    @Id
    @Column(columnDefinition = "int(11) COMMENT 'Id'")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Size(max = 20)
    @Column(columnDefinition = "varchar(20) COMMENT '关联资产'")
    private String code;

    @Column(columnDefinition = "varchar(50) COMMENT '关联用户'")
    private String account;

    @Column(columnDefinition = "varchar(20) COMMENT '日志类型'")
    private OperateEnum type;

    @Size(max = 20)
    @Column(columnDefinition = "varchar(20) COMMENT '子类型'")
    private String subType;

    @Size(max = 500)
    @Column(columnDefinition = "varchar(500) COMMENT '说明'")
    private String description;

    @Column(columnDefinition = "varchar(50) COMMENT '操作人'")
    private String operator;

    @Column(columnDefinition = "datetime COMMENT '创建时间'", insertable = false, updatable = false)
    private java.time.LocalDateTime creationTime;

    public OperatelogDto mapTo() {
        OperatelogDto result = new OperatelogDto();
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
/*
INSERT INTO assets.Operatelog (
  code, userId, type, description, operator
)VALUES(
  :code, :userId, :type, :description, :operator
);

UPDATE assets.Operatelog SET
  code = :code, userId = :userId, type = :type, description = :description, operator = :operator
WHERE ;
*/
}