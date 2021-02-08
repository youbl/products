package beinet.cn.assetmanagement.security;

import beinet.cn.assetmanagement.user.model.Users;
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
public class BeinetUserDetail implements UserDetails {
    private final Users user;
    private List<GrantedAuthority> roles;

    public BeinetUserDetail(Users user) {
        this.user = user;
        setRoles();
    }

    private void setRoles() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        if (!StringUtils.isEmpty(user.getRoles())) {
            for (String role : user.getRoles().split(",")) {
                if (role.length() <= 0)
                    continue;
                // Spring比较是按 ROLE_USER 形式进行比较
                authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
            }
        }
        roles = authorities;
    }

    public Users getUser() {
        return user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getAccount();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return user.getState() != 1;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
