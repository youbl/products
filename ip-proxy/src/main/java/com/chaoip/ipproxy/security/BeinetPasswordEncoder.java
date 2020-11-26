package com.chaoip.ipproxy.security;

import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * BeinetPasswordEncoder
 *
 * @author youbl
 * @version 1.0
 * @date 2020/11/25 18:57
 */
public class BeinetPasswordEncoder implements PasswordEncoder {
    @Override
    public String encode(CharSequence rawPassword) {
        return String.valueOf(rawPassword);
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return String.valueOf(rawPassword).equals(encodedPassword);
    }
}