package com.chaoip.ipproxy.controller;

import com.chaoip.ipproxy.controller.dto.UserDto;
import com.chaoip.ipproxy.repository.entity.BeinetUser;
import com.chaoip.ipproxy.security.AuthDetails;
import com.chaoip.ipproxy.security.BeinetUserService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * UserController
 *
 * @author youbl
 * @version 1.0
 * @date 2020/11/25 18:59
 */
@RestController
@RequestMapping("user")
public class UserController {
    private final BeinetUserService userService;

    public UserController(BeinetUserService userService) {
        this.userService = userService;
    }

    @GetMapping("")
    public String userName(AuthDetails details) {
        if (details == null)
            return "未登录";
        return details.getUserName();
    }

    /**
     * 新增用户
     *
     * @param dto 新用户所需字段
     * @return 入库记录
     */
    @PostMapping("")
    public BeinetUser addUser(@Valid @RequestBody UserDto dto) {
        if (!dto.getPassword().equals(dto.getPasswordConfirm())) {
            throw new IllegalArgumentException("两次密码输入不一致");
        }
        return userService.addUser(dto);
    }
}
