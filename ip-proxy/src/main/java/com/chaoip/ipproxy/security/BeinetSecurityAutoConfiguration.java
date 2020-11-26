package com.chaoip.ipproxy.security;

import com.chaoip.ipproxy.repository.BeinetUserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * BeinetAuthConfiguration
 *
 * @author youbl
 * @version 1.0
 * @date 2020/11/25 14:53
 */
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Configuration
public class BeinetSecurityAutoConfiguration extends WebSecurityConfigurerAdapter {
    static final String LOGIN_PAGE = "/login/index.html";

    @Value("${beinet.security.user:sdk}")
    private String sdkUser;

    @Value("${beinet.security.password:beinet.123}")
    private String sdkPassword;

    @Bean
    public PasswordEncoder createPasswordEncoder() {
        return new BeinetPasswordEncoder();
    }

    @Bean
    public BeinetUserService createUserDetailService(PasswordEncoder encoder, BeinetUserRepository userRepository) {
        return new BeinetUserService(encoder, userRepository);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // 支持basic认证，用于admin请求 在 org.springframework.security.web.authentication.www.BasicAuthenticationFilter 里实现的
        // 在header里添加  Authorization: Basic base64(用户名:密码) 访问
        auth.inMemoryAuthentication()
                .passwordEncoder(createPasswordEncoder())
                .withUser(sdkUser).password(sdkPassword).authorities("ROLE_ADMIN", "ROLE_USER");// 用 authorities 必须显式添加ROLE_前缀
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // super.configure(http); 即默认配置是下面这一句
        //((HttpSecurity)((HttpSecurity)((AuthorizedUrl)http.authorizeRequests().anyRequest()).authenticated().and()).formLogin().and()).httpBasic();

        // 先关闭csrf，影响登录和退出
        http.csrf().disable();

        // antMatchers 规则：
        // ? 匹配1个字符
        // * 匹配0或多个字符
        // ** 匹配0或多个目录
        // {varName:[a-z]+} 匹配正则，并把正则匹配到的内容赋值给变量
        // 参考文档 https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/util/AntPathMatcher.html
        http
                .formLogin()                        // 开启form表单登录
                .usernameParameter("beinetUser")    // 修改登录用户名参数
                .passwordParameter("beinetPwd")     // 修改登录密码参数
                .loginPage(LOGIN_PAGE)         // 使用自定义的登录表单
                .loginProcessingUrl("/login")       // 接收POST登录请求的处理地址
                .successHandler(new BeinetHandleSuccess()) // 登录验证通过后的处理器
                .failureHandler(new BeinetHandleFail())    // 登录验证失败后的处理器
                .permitAll()                        // 允许上述请求匿名访问, 注：不加这句，会导致302死循环
                .and()                              // 把前面的返回结果，转换回HttpSecurity，以便后续的流式操作
                .logout()                           // 开启退出登录接口
                //.logoutUrl("/logout")             // 指定退出登录的url，默认就是/logout
                .logoutSuccessHandler(new BeinetHandleLogout()) // 退出成功后的处理器
                .permitAll()                        // 退出的url要允许匿名访问
                .and()
                .exceptionHandling()                // 开始异常处理配置
                .authenticationEntryPoint(new BeinetAuthenticationEntryPoint()) // 指定匿名访问需登录的url的异常处理器
                .accessDeniedHandler(new BeinetHandleAccessDenied())            // 指定登录用户访问无权限url的异常处理器
                .and()
                .authorizeRequests()                // 开始指定请求授权
                .antMatchers(LOGIN_PAGE + "/**").permitAll() // 上面的loginPage("/myLogin.html").permitAll() 居然不支持带参数: myLogin.html?xxx
                .antMatchers("/res/**").permitAll()     // res根路径及子目录请求，不限制访问
                .antMatchers("/favicon.ico").permitAll()     // ico不限制访问
                .antMatchers("/user/**").permitAll()    // user控制器请求，不限制访问
                .antMatchers("/admin/**").hasRole("ADMIN")// news根路径及子目录请求，要求ADMIN角色才能访问
                //.antMatchers("/**").permitAll()
                //.antMatchers("/role/**").permitAll()    // 忽略配置里的权限，改用 EnableGlobalMethodSecurity 和 PreAuthorize 注解
                .anyRequest().authenticated();      // 其它所有请求都要求登录后访问，但是不限制角色

        BeinetAuthenticationFilter filter = new BeinetAuthenticationFilter();
        //http.addFilterBefore(filter, RequestCacheAwareFilter.class);
        http.addFilterAfter(filter, BasicAuthenticationFilter.class);
    }


    @Configuration
    static class WebMvcConfig implements WebMvcConfigurer {
        @Override
        public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
            resolvers.add(new AuthDetailArgumentResolver());
        }
    }

    static void outputDenyMsg(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());

        response.setContentType("application/json; charset=utf-8");
        response.setHeader("ts", String.valueOf(System.currentTimeMillis()));

        Map<String, Object> data = new HashMap<>();
        data.put("msg", message);
        response.getOutputStream().write(new ObjectMapper().writeValueAsString(data).getBytes(StandardCharsets.UTF_8));
    }
}
