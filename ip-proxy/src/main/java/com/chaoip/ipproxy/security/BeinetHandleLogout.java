package com.chaoip.ipproxy.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * BeinetHandleLogout
 *
 * @author youbl
 * @version 1.0
 * @date 2020/11/25 14:59
 */
public class BeinetHandleLogout implements LogoutSuccessHandler {

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        response.setStatus(HttpStatus.OK.value());

        response.setHeader("ts", String.valueOf(System.currentTimeMillis()));
        response.setContentType("application/json; charset=utf-8");

        Map<String, Object> data = new HashMap<>();
        if (authentication == null || StringUtils.isEmpty(authentication.getName())) {
            data.put("msg", " 当前没有登录");
        } else {
            data.put("msg", authentication.getName() + " 成功退出登录");
        }
        response.getOutputStream().write(new ObjectMapper().writeValueAsString(data).getBytes("UTF-8"));
    }
}