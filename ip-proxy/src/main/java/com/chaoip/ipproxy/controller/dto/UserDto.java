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

    }

    @NotBlank(message = "用户名不能为空")
    private String name;
    @NotBlank(message = "密码不能为空")
    private String password;
    @NotBlank(message = "确认密码不能为空")
    private String passwordConfirm;

    public BeinetUser mapTo() {
        LocalDateTime now = LocalDateTime.now();
        return BeinetUser.builder()
                .name(getName())
                .password(getPassword())
                .creationTime(now)
                .security(BeinetUser.countSecurity(getName(), getPassword(), now))
                .build();
    }
}
