package com.chaoip.ipproxy.controller;

import com.chaoip.ipproxy.security.BeinetUserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("user")
    public int xx() {
        return 0;
    }
}
