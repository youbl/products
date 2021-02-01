package beinet.cn.assetmanagement.user.model;

import lombok.*;

import javax.persistence.*;
// <groupId>org.hibernate.validator</groupId><artifactId>hibernate-validator</artifactId>
import javax.validation.constraints.*;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "Validcode", catalog = "assets")
public class Validcode {
    @Id
    @Column(columnDefinition = "int(11) COMMENT 'Id'")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Size(max = 20)
    @Column(columnDefinition = "varchar(20) COMMENT '验证码'")
    private String code;

    @Size(max = 20)
    @Column(columnDefinition = "varchar(20) COMMENT '验证码序号'")
    private String sn;

    @Column(columnDefinition = "tinyint(4) COMMENT '类型，0图形，1短信'")
    private int type;

    @Column(columnDefinition = "tinyint(4) COMMENT '允许错误次数'")
    private int enableErrNum;

    @Column(columnDefinition = "datetime COMMENT '创建时间'", insertable = false, updatable = false)
    private java.time.LocalDateTime creationTime;

    @Column(columnDefinition = "datetime COMMENT '最后修改时间'", insertable = false, updatable = false)
    private java.time.LocalDateTime lastModificationTime;

    public ValidcodeDto mapTo() {
        ValidcodeDto result = new ValidcodeDto();
        result.setId(this.getId());
        result.setCode(this.getCode());
        result.setSn(this.getSn());
        result.setType(this.getType());
        result.setEnableErrNum(this.getEnableErrNum());
        result.setCreationTime(this.getCreationTime());
        result.setLastModificationTime(this.getLastModificationTime());
        return result;
    }
/*
INSERT INTO assets.Validcode (
  code, sn, type, enableErrNum
)VALUES(
  :code, :sn, :type, :enableErrNum
);

UPDATE assets.Validcode SET
  code = :code, sn = :sn, type = :type, enableErrNum = :enableErrNum
WHERE ;
*/
}