package com.chaoip.ipproxy.security;

import com.chaoip.ipproxy.controller.dto.PasswordDto;
import com.chaoip.ipproxy.controller.dto.UserDto;
import com.chaoip.ipproxy.repository.BeinetUserRepository;
import com.chaoip.ipproxy.repository.entity.BeinetUser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;

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
        // 使用登录的密码加密器进行加密
        user.setPassword(encoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public BeinetUser findByName(String name) {
        BeinetUser user = userRepository.findByName(name);

        if (user != null) {
            // 隐藏密码
            user.setPassword("");
            // 隐藏手机号
            if (!StringUtils.isEmpty(user.getPhone())) {
                user.setPhone(user.getPhone().replaceAll("^(.{3}).+(.{2})$", "$1****$2"));
            }
            // 隐藏银行卡
            if (!StringUtils.isEmpty(user.getBankNo())) {
                user.setBankNo(user.getBankNo().replaceAll("^(.{3}).+(.{3})$", "$1****$2"));
            }
            // 隐藏SecurityKey
            if (!StringUtils.isEmpty(user.getSecurity())) {
                user.setSecurity(user.getSecurity().replaceAll("^(.{4}).+(.{4})$", "$1****$2"));
            }
        }
        return user;
    }

    public boolean changePassword(PasswordDto dto, String username) {
        BeinetUser user = userRepository.findByName(username);
        if (user == null) {
            throw new RuntimeException("指定的用户未找到: " + username);
        }
        if (!encoder.matches(dto.getPasswordOld(), user.getPassword())) {
            throw new RuntimeException("输入的旧密码不匹配: " + username);
        }
        user.setPassword(encoder.encode(dto.getPassword()));
        userRepository.save(user);
        return true;
    }
}
