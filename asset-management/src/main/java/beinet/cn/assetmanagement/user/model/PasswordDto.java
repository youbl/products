package beinet.cn.assetmanagement.user.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * PasswordDto
 *
 * @author youbl
 * @version 1.0
 * @date 2021/01/31 19:40
 */
@Data
public class PasswordDto {

    @NotBlank(message = "账号不能为空")
    private String account;
    @NotBlank(message = "旧密码不能为空")
    private String passwordOld;
    @NotBlank(message = "新密码不能为空")
    private String password;
    @NotBlank(message = "确认密码不能为空")
    private String passwordConfirm;
}
