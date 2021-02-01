package beinet.cn.assetmanagement.security;

import beinet.cn.assetmanagement.user.service.UsersService;
import beinet.cn.assetmanagement.user.service.ValidcodeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
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

    private final ValidcodeService validcodeService;
    private final AuthenticationFailureHandler failureHandler;

    public BeinetSecurityAutoConfiguration(ValidcodeService validcodeService) {
        this.validcodeService = validcodeService;
        this.failureHandler = new BeinetHandleFail();
    }

    @Bean
    public PasswordEncoder createPasswordEncoder() {
        return new BeinetPasswordEncoder();
    }

    @Bean
    public BeinetUserService createUserDetailService(UsersService usersService) {
        return new BeinetUserService(usersService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // super.configure(http); 即默认配置是下面这一句
        //((HttpSecurity)((HttpSecurity)((AuthorizedUrl)http.authorizeRequests().anyRequest()).authenticated().and()).formLogin().and()).httpBasic();

        // 先关闭csrf，影响登录和退出
        http.csrf().disable();
        // 默认是 X-Frame-Options: DENY 不允许在frame内
        http.headers().frameOptions().sameOrigin();

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
                .failureHandler(failureHandler)    // 登录验证失败后的处理器
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
                .antMatchers("/static/**", "/res/**", "/error/**", "/img/**").permitAll()     // res根路径及子目录请求，不限制访问
                .antMatchers("/favicon.ico").permitAll()        // ico不限制访问
                .antMatchers("/login/**").permitAll()           // login相关页面请求，不限制访问
                .antMatchers("/departments").permitAll()        // 注册页面需要获取部门列表
                .antMatchers("/validcode/img/**").permitAll()   // 验证码请求，不限制访问
                .antMatchers("/**/*.html/**").permitAll()       // html，不限制访问
                //.antMatchers("/admin/**").hasRole("ADMIN") // 管理页面请求，要求ADMIN角色才能访问
                //.antMatchers("/manage/**").hasRole("ADMIN")// 管理页面请求，要求ADMIN角色才能访问
                .anyRequest().authenticated();      // 其它所有请求都要求登录后访问，但是不限制角色

        // 在用户名密码校验之前，检查图形验证码是否正确
        ValidCodeFilter filter = new ValidCodeFilter(validcodeService, failureHandler);
        http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
    }


    @Configuration
    static class WebMvcConfig implements WebMvcConfigurer {
        /**
         * 为Controller增加一个参数解析器
         *
         * @param resolvers 参数解析器
         */
        @Override
        public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
            resolvers.add(new AuthDetailArgumentResolver());
        }
    }

    public static void outputDenyMsg(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());

        response.setContentType("application/json; charset=utf-8");
        response.setHeader("ts", String.valueOf(System.currentTimeMillis()));

        Map<String, Object> data = new HashMap<>();
        data.put("msg", message);
        response.getOutputStream().write(new ObjectMapper().writeValueAsString(data).getBytes(StandardCharsets.UTF_8));
    }
}
