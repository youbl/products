package beinet.cn.assetmanagement.user.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsersDto {
    private int id;

    @NotBlank(message = "工号不能为空")
    private String code;

    @NotBlank(message = "账号不能为空")
    private String account;

    @NotBlank(message = "姓名不能为空")
    private String userName;

    @Min(value = 1, message = "部门必须选择")
    private int department;

    @NotBlank(message = "密码不能为空")
    private String password;

    private String roles;

    private String phone;

    private int sex;

    private int state;

    private java.time.LocalDateTime creationTime;

    private java.time.LocalDateTime lastModificationTime;

    public Users mapTo() {
        Users result = new Users();
        result.setId(this.getId());
        result.setCode(this.getCode());
        result.setAccount(this.getAccount());
        result.setUserName(this.getUserName());
        result.setDepartment(this.getDepartment());
        result.setPassword(this.getPassword());
        result.setRoles(this.getRoles());
        result.setPhone(this.getPhone());
        result.setSex(this.getSex());
        result.setState(this.getState());
        result.setCreationTime(this.getCreationTime());
        result.setLastModificationTime(this.getLastModificationTime());
        return result;
    }
}