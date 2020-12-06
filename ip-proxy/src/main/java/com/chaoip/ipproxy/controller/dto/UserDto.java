package com.chaoip.ipproxy.controller.dto;

import com.chaoip.ipproxy.repository.entity.BeinetUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

/**
 * RouteDto
 *
 * @author youbl
 * @version 1.0
 * @date 2020/11/17 19:40
 */
@Data
@Builder
@AllArgsConstructor
public class UserDto {
    public UserDto() {
        // empty
    }

    @NotBlank(message = "用户名不能为空")
    private String name;
    @NotBlank(message = "密码不能为空")
    private String password;
    @NotBlank(message = "确认密码不能为空")
    private String passwordConfirm;
    /**
     * 输入的手机号
     */
    @Pattern(regexp = "^1\\d{10}$", message = "无效的手机号")
    private String phone;
    @NotBlank(message = "短信验证码序号不能为空")
    private String smsSn;
    @NotBlank(message = "短信验证码不能为空")
    private String smsCode;

    private String bankNo;
    private String realName;
    private String identity;
    private LocalDateTime creationTime;

    private int pageNum;
    private int pageSize;

    public BeinetUser mapTo() {
        LocalDateTime now = LocalDateTime.now();
        return BeinetUser.builder()
                .name(getName())
                .password(getPassword())
                .phone(getPhone())
                .bankNo("")
                .realName("")
                .identity("")
                .security(BeinetUser.countSecurity(getName(), getPassword(), now))
                .build();
    }
}
