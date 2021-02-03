package beinet.cn.assetmanagement.assets.model;

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
@Table(name = "Assetclass", catalog = "assets")
public class Assetclass {
    @Id
    @Column(columnDefinition = "int(11) COMMENT 'Id'")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Size(max = 20)
    @Column(columnDefinition = "varchar(20) COMMENT '分类名'")
    private String className;

    @Size(max = 200)
    @Column(columnDefinition = "varchar(200) COMMENT '说明'")
    private String description;

    @Column(columnDefinition = "int(11) COMMENT '资产数量，用于资产code生成'")
    private int totalAmount;

    @Column(columnDefinition = "datetime COMMENT '创建时间'", insertable = false, updatable = false)
    private java.time.LocalDateTime creationTime;

    @Column(columnDefinition = "datetime COMMENT '最后修改时间'", insertable = false, updatable = false)
    private java.time.LocalDateTime lastModificationTime;


    public AssetclassDto mapTo() {
        AssetclassDto result = new AssetclassDto();
        result.setId(this.getId());
        result.setClassName(this.getClassName());
        result.setCreationTime(this.getCreationTime());
        result.setLastModificationTime(this.getLastModificationTime());
        result.setDescription(this.getDescription());
        return result;
    }
/*
INSERT INTO assets.Assetclass (
  className, description
)VALUES(
  :className, :description
);

UPDATE assets.Assetclass SET
  className = :className, description = :description
WHERE ;
*/
}