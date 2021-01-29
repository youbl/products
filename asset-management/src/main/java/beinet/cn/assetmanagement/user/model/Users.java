package beinet.cn.assetmanagement.user.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
// <groupId>org.hibernate.validator</groupId><artifactId>hibernate-validator</artifactId>
import javax.validation.constraints.*;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@DynamicInsert
@DynamicUpdate
@Table(name = "Users", catalog = "assets")
public class Users {
    @Id
    @Column(columnDefinition = "int(11) COMMENT 'Id'")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Size(max = 20)
    @Column(columnDefinition = "varchar(20) COMMENT '工号'")
    private String code;

    @Size(max = 20)
    @Column(columnDefinition = "varchar(20) COMMENT '登录名'")
    private String account;

    @Size(max = 20)
    @Column(columnDefinition = "varchar(20) COMMENT '姓名'")
    private String userName;

    @Column(columnDefinition = "int(11) COMMENT '所属部门'")
    private int department;

    @Size(max = 50)
    @Column(columnDefinition = "varchar(50) COMMENT '加密后的密码'")
    private String password;

    @Size(max = 2)
    @Column(columnDefinition = "varchar(2) COMMENT '联系电话'")
    private String phone;

    @Column(columnDefinition = "tinyint(4) COMMENT '性别 0男 1女'")
    private int sex;

    @Column(columnDefinition = "tinyint(4) COMMENT '状态 0待审核 1禁用 8可用'")
    private int state;

    @Column(columnDefinition = "datetime COMMENT '创建时间'", insertable = false, updatable = false)
    private java.time.LocalDateTime creationTime;

    @Column(columnDefinition = "datetime COMMENT '最后修改时间'", insertable = false, updatable = false)
    private java.time.LocalDateTime lastModificationTime;

    public UsersDto mapTo() {
        UsersDto result = new UsersDto();
        result.setId(this.getId());
        result.setCode(this.getCode());
        result.setAccount(this.getAccount());
        result.setUserName(this.getUserName());
        result.setDepartment(this.getDepartment());
        result.setPassword(this.getPassword());
        result.setPhone(this.getPhone());
        result.setSex(this.getSex());
        result.setCreationTime(this.getCreationTime());
        result.setLastModificationTime(this.getLastModificationTime());
        return result;
    }
/*
INSERT INTO assets.Users (
  code, account, userName, department, password, phone, sex
)VALUES(
  :code, :account, :userName, :department, :password, :phone, :sex
);

UPDATE assets.Users SET
  code = :code, account = :account, userName = :userName, department = :department, password = :password, phone = :phone, sex = :sex
WHERE ;
*/
}