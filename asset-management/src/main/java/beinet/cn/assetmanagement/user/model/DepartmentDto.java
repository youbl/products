package beinet.cn.assetmanagement.user.model;

import lombok.Data;

@Data
public class DepartmentDto {
    private int id;

    private String departmentName;

    private String description;

    private java.time.LocalDateTime creationTime;

    private java.time.LocalDateTime lastModificationTime;

    public Department mapTo() {
        Department result = new Department();
        result.setId(this.getId());
        result.setDepartmentName(this.getDepartmentName());
        result.setDescription(this.getDescription());
        result.setCreationTime(this.getCreationTime());
        result.setLastModificationTime(this.getLastModificationTime());
        return result;
    }
}