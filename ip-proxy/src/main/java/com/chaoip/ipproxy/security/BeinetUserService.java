package com.chaoip.ipproxy.security;

import com.chaoip.ipproxy.controller.dto.UserDto;
import com.chaoip.ipproxy.repository.BeinetUserRepository;
import com.chaoip.ipproxy.repository.entity.BeinetUser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * BeinetUserService
 *
 * @author youbl
 * @version 1.0
 * @date 2020/11/25 18:36
 */
public class BeinetUserService implements UserDetailsService {
    private final PasswordEncoder encoder;
    private final BeinetUserRepository userRepository;

    @Value("${beinet.security.user:sdk}")
    private String sdkUser;

    @Value("${beinet.security.password:beinet.123}")
    private String sdkPassword;

    /**
     * 带密码编码器的构造函数
     *
     * @param encoder 编码器
     */
    public BeinetUserService(PasswordEncoder encoder, BeinetUserRepository userRepository) {
        this.encoder = encoder;
        this.userRepository = userRepository;
    }

    /**
     * 根据用户名，查找用户信息返回。
     * 可以从数据库、内存或api远程查找
     *
     * @param username 用户名
     * @return 找到的用户信息
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (username == null || username.isEmpty())
            throw new IllegalArgumentException("username can't be empty.");

        // 管理员登录判断
        if (username.equalsIgnoreCase(sdkUser)) {
            BeinetUser ret = BeinetUser.builder()
                    .name(sdkUser)
                    .password(encoder.encode(sdkPassword))
                    .roles("USER,ADMIN")
                    .build();
            return ret;
        }

        BeinetUser ret = userRepository.findByName(username);
        if (ret == null)
            throw new UsernameNotFoundException(username + " not found.");
        return ret;
    }

    public boolean existsByPhone(String phone) {
        return userRepository.existsByPhone(phone);
    }

    public BeinetUser addUser(UserDto userDto) {
        if (userRepository.findByName(userDto.getName()) != null) {
            throw new RuntimeException("该名称已存在:" + userDto.getName());
        }
        BeinetUser user = userDto.mapTo();
        return userRepository.save(user);
    }
}
