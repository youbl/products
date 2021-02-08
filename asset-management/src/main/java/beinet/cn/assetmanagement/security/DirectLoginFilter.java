package beinet.cn.assetmanagement.security;

import beinet.cn.assetmanagement.AutoInitData;
import beinet.cn.assetmanagement.user.service.UsersService;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

public class DirectLoginFilter extends OncePerRequestFilter {
    private static final String USER_PARA = "beinet";
    private final UsersService usersService;

    public DirectLoginFilter(UsersService usersService) {
        this.usersService = usersService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // 已登录 直接跳过
        if (authentication != null && authentication.isAuthenticated()) {
            filterChain.doFilter(request, response);
            return;
        }

        String account = request.getParameter(USER_PARA);
        // 没有提供url参数 直接跳过
        if (StringUtils.isEmpty(account) || !AutoInitData.ADMIN.equals(account)) {
            filterChain.doFilter(request, response);
            return;
        }

        BeinetUserDetail authResult = new BeinetUserDetail(usersService.findByAccount(account, true));
        SecurityContextHolder.getContext().setAuthentication(new DirectAuth(authResult));

        filterChain.doFilter(request, response);
    }


    public static class DirectAuth extends AbstractAuthenticationToken {
        private BeinetUserDetail details;

        public DirectAuth(BeinetUserDetail details) {
            super(null);
            this.details = details;
        }

        @Override
        public Object getCredentials() {
            return details;
        }

        @Override
        public Object getPrincipal() {
            return details;
        }

        @Override
        public Collection<GrantedAuthority> getAuthorities() {
            return (Collection<GrantedAuthority>) details.getAuthorities();
        }
    }

    /**
     * 用于支持 DirectAuth 类型鉴权的服务类
     */
    public static class DirectAuthenticationProvider implements AuthenticationProvider {

        @Override
        public Authentication authenticate(Authentication authentication) throws AuthenticationException {
            Assert.isInstanceOf(DirectAuth.class, authentication, "不支持该类型登录:" + authentication.getClass().getName());
            authentication.setAuthenticated(true);
            return authentication;
        }

        @Override
        public boolean supports(Class<?> authentication) {
            return DirectAuth.class.isAssignableFrom(authentication);
        }
    }
}
