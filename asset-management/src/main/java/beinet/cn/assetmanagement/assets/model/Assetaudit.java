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
@Table(name = "Assetaudit", catalog = "assets")
public class Assetaudit {
    @Id
    @Column(columnDefinition = "int(11) COMMENT 'Id'")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Size(max = 20)
    @Column(columnDefinition = "varchar(20) COMMENT '审核类型'")
    private String type;

    @Size(max = 20)
    @Column(columnDefinition = "varchar(20) COMMENT '审核子类型'")
    private String subtype;

    @Size(max = 100)
    @Column(columnDefinition = "varchar(100) COMMENT '申请说明'")
    private String description;

    @Column(columnDefinition = "int(11) COMMENT '资产分类id'")
    private int classId;

    @Column(columnDefinition = "datetime COMMENT '退库时间'")
    private java.time.LocalDateTime returnTime;

    @Size(max = 50)
    @Column(columnDefinition = "varchar(50) COMMENT '申请人'")
    private String account;

    @Column(columnDefinition = "int(11) COMMENT '审核状态,0待审核,1拒绝,2作废,8通过'")
    private int state;

    @Size(max = 20)
    @Column(columnDefinition = "varchar(20) COMMENT '审核人'")
    private String auditUser;

    @Size(max = 100)
    @Column(columnDefinition = "varchar(100) COMMENT '审核意见'")
    private String auditReason;

    @Column(columnDefinition = "datetime COMMENT '创建时间'", insertable = false, updatable = false)
    private java.time.LocalDateTime creationTime;

    @Column(columnDefinition = "datetime COMMENT '最后修改时间'", insertable = false, updatable = false)
    private java.time.LocalDateTime lastModificationTime;

    public AssetauditDto mapTo() {
        AssetauditDto result = new AssetauditDto();
        result.setId(this.getId());
        result.setType(this.getType());
        result.setSubtype(this.getSubtype());
        result.setClassId(this.getClassId());
        result.setDescription(this.getDescription());
        result.setReturnTime(this.getReturnTime());
        result.setAuditUser(this.getAuditUser());
        result.setAccount(this.getAccount());
        result.setState(this.getState());
        result.setAuditReason(this.getAuditReason());
        result.setCreationTime(this.getCreationTime());
        result.setLastModificationTime(this.getLastModificationTime());
        return result;
    }
/*
INSERT INTO assets.Assetaudit (
  type, subtype, description, assetCode, account, state
)VALUES(
  :type, :subtype, :description, :assetCode, :account, :state
);

UPDATE assets.Assetaudit SET
  type = :type, subtype = :subtype, description = :description, assetCode = :assetCode, account = :account, state = :state
WHERE ;
*/
}