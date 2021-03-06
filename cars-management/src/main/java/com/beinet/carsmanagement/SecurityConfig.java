package com.beinet.carsmanagement;

import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/res/**").permitAll()         // 允许res目录下的所有访问
                .antMatchers("/**/*.html").permitAll()      // 允许所有html访问
                .antMatchers("/v1/carstemp**").permitAll()    // 允许临时车访问
                .antMatchers(HttpMethod.GET).permitAll()                 // 允许所有get请求
                .antMatchers(HttpMethod.POST, "/v1/owner**").permitAll()   // 允许所有POST+owner请求
                .anyRequest()
                .authenticated().and().formLogin();
    }

    /**
     * 返回当前登录用户名
     *
     * @return 用户名，未登录返回空
     */
    public static String getLoginUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // 未登录时，principal是一个字符串，内容为 anonymousUser
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        }
        return "";
    }

    /**
     * 是否登录
     *
     * @return true false
     */
    public static boolean isLogin() {
        return getLoginUser().length() > 0;
    }
}
