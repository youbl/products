package beinet.cn.assetmanagement.user.model;

import lombok.Data;

import javax.persistence.*;
// <groupId>org.hibernate.validator</groupId><artifactId>hibernate-validator</artifactId>
import javax.validation.constraints.*;

@Data
@Entity
@Table(name = "Department", catalog = "assets")
public class Department {
    @Id
    @Column(columnDefinition = "int(11) COMMENT 'Id'")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Size(max = 20)
    @Column(columnDefinition = "varchar(20) COMMENT '部门名称'")
    private String departmentName;

    @Size(max = 200)
    @Column(columnDefinition = "varchar(200) COMMENT '说明'")
    private String description;

    @Column(columnDefinition = "datetime COMMENT '创建时间'", insertable = false, updatable = false)
    private java.time.LocalDateTime creationTime;

    @Column(columnDefinition = "datetime COMMENT '最后修改时间'", insertable = false, updatable = false)
    private java.time.LocalDateTime lastModificationTime;

    public DepartmentDto mapTo() {
        DepartmentDto result = new DepartmentDto();
        result.setId(this.getId());
        result.setDepartmentName(this.getDepartmentName());
        result.setDescription(this.getDescription());
        result.setCreationTime(this.getCreationTime());
        result.setLastModificationTime(this.getLastModificationTime());
        return result;
    }
/*
INSERT INTO assets.Department (
  departmentName, description
)VALUES(
  :departmentName, :description
);

UPDATE assets.Department SET
  departmentName = :departmentName, description = :description
WHERE ;
*/
}