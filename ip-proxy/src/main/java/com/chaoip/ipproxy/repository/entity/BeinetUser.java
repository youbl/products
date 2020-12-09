package com.chaoip.ipproxy.repository.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * BeinetUser
 * UserDetails接口用于SpringSecurity
 *
 * @author youbl
 * @version 1.0
 * @date 2020/11/25 18:37
 */
@Data
@Builder
@Document
public class BeinetUser implements UserDetails {
    @Id
    private long id;
    @Indexed(unique = true)
    private String name;
    @Indexed(unique = true)
    private String phone;
    private String password;
    private String realName;
    private String identity;
    private String bankNo;
    private String security;
    private String roles = "USER";
    private LocalDateTime creationTime;
    private int status;
    /**
     * 用户余额，单位分，新字段，因此要用 Integer
     */
    private Integer money;

    /**
     * 给用户计算一个密钥，后续作为api使用
     *
     * @param name
     * @param password
     * @param time
     * @return
     */
    public static String countSecurity(String name, String password, LocalDateTime time) {
        String sign = String.format("%s-%s-%s", name, password, time);
        return DigestUtils.md5DigestAsHex(sign.getBytes());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        if (!StringUtils.isEmpty(roles)) {
            for (String role : roles.split(",")) {
                if (role.length() <= 0)
                    continue;
                // Spring比较是按 ROLE_USER 形式进行比较
                authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
            }
        }
        return authorities;
    }

    @Override
    public String getUsername() {
        return name;
    }

    /**
     * 账号是否有效
     *
     * @return 是否
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * 账号是否可用
     *
     * @return 是否
     */
    @Override
    public boolean isAccountNonLocked() {
        return status == 0;
    }

    /**
     * 账号是否在 有效期内
     *
     * @return 是否
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 账号是否启用
     *
     * @return 是否
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}
