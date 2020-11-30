package com.chaoip.ipproxy.security;

import com.chaoip.ipproxy.repository.entity.BeinetUser;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

/**
 * BeinetPasswordEncoder
 *
 * @author youbl
 * @version 1.0
 * @date 2020/11/25 18:57
 */
public class BeinetPasswordEncoder implements PasswordEncoder {
    private final static LocalDateTime timeForPwd = LocalDateTime.of(2010, 3, 30, 19, 40);

    /**
     * 使用md5加密密码
     *
     * @param rawPassword 原始密码
     * @return 加密内容
     */
    @Override
    public String encode(CharSequence rawPassword) {
        return BeinetUser.countSecurity("beinet", rawPassword.toString(), timeForPwd);
        // return String.valueOf(rawPassword);
    }

    /**
     * 验证密码是否一致
     *
     * @param rawPassword     原始密码，用户输入的
     * @param encodedPassword 上面encode加密过的密码
     * @return 是否一致
     */
    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        if (StringUtils.isEmpty(rawPassword)) {
            return false;
        }
        return encode(rawPassword).equals(encodedPassword);
    }
}