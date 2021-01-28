package beinet.cn.assetmanagement.user.model;

import lombok.Data;

@Data
public class UsersDto {
    private int id;

    private String code;

    private String account;

    private String userName;

    private int department;

    private String password;

    private String phone;

    private int sex;

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
        result.setPhone(this.getPhone());
        result.setSex(this.getSex());
        result.setCreationTime(this.getCreationTime());
        result.setLastModificationTime(this.getLastModificationTime());
        return result;
    }
}