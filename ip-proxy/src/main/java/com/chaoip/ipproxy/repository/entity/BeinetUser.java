package com.chaoip.ipproxy.repository.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;

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
    private String password;
    private String identity;
    private String bankNo;
    private String security;
    private String roles;

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
        return true;
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
