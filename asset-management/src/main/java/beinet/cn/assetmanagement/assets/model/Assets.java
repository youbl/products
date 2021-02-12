package beinet.cn.assetmanagement.assets.model;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.util.StringUtils;

import javax.persistence.*;
// <groupId>org.hibernate.validator</groupId><artifactId>hibernate-validator</artifactId>
import javax.validation.constraints.*;
import java.time.format.DateTimeFormatter;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@DynamicInsert
@DynamicUpdate
@Entity
@Table(name = "Assets", catalog = "assets")
public class Assets {
    @Id
    @Column(columnDefinition = "int(11) COMMENT 'Id'")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Size(max = 20)
    @Column(columnDefinition = "varchar(20) COMMENT '资产编号'")
    private String code;

    @Size(max = 100)
    @Column(columnDefinition = "varchar(100) COMMENT '资产名称'")
    private String assetName;

    @Column(columnDefinition = "int(11) COMMENT '资产分类id'")
    private int classId;

    @Size(max = 500)
    @Column(columnDefinition = "varchar(500) COMMENT '说明'")
    private String description;

    @Column(columnDefinition = "int(11) COMMENT '购买价格，单位分'")
    private int price;

    @Column(columnDefinition = "datetime COMMENT '购买时间'")
    private java.time.LocalDateTime buyTime;

    @Column(columnDefinition = "int(11) COMMENT '状态 0可用，1借出，2故障，3报废'")
    private int state;

    @Size(max = 50)
    @Column(columnDefinition = "varchar(50) COMMENT '库存位置编号'")
    private String place;

    @Size(max = 50)
    @Column(columnDefinition = "varchar(50) COMMENT '保管人'")
    private String account;

    @Column(columnDefinition = "datetime COMMENT '借用时间'")
    private java.time.LocalDateTime accountTime;

    @Column(columnDefinition = "datetime COMMENT '创建时间'", insertable = false, updatable = false)
    private java.time.LocalDateTime creationTime;

    @Column(columnDefinition = "datetime COMMENT '最后修改时间'", insertable = false, updatable = false)
    private java.time.LocalDateTime lastModificationTime;

    public AssetsDto mapTo() {
        AssetsDto result = new AssetsDto();
        result.setId(this.getId());
        result.setCode(this.getCode());
        result.setAssetName(this.getAssetName());
        result.setClassId(this.getClassId());
        result.setDescription(this.getDescription());
        result.setBuyTime(this.getBuyTime());
        result.setPrice(this.getPrice());
        result.setState(this.getState());
        result.setPlace(this.getPlace());
        result.setAccount(this.getAccount());
        result.setAccountTime(this.getAccountTime());
        result.setCreationTime(this.getCreationTime());
        result.setLastModificationTime(this.getLastModificationTime());
        return result;
    }
/*
INSERT INTO assets.Assets (
  code, assetName, classId, description, buyTime, price, state, place
)VALUES(
  :code, :assetName, :classId, :description, :buyTime, :price, :state, :place
);

UPDATE assets.Assets SET
  code = :code, assetName = :assetName, classId = :classId, description = :description, buyTime = :buyTime, price = :price, state = :state, place = :place
WHERE ;
*/
}