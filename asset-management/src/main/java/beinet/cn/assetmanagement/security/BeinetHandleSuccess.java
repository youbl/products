package beinet.cn.assetmanagement.security;

import beinet.cn.assetmanagement.event.Publisher;
import beinet.cn.assetmanagement.event.user.LoginDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * BeinetHandleSuccess
 *
 * @author youbl
 * @version 1.0
 * @date 2020/11/25 14:58
 */
public class BeinetHandleSuccess implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException {
        publishEvent(request, authentication);

        // response.sendRedirect("/user/sub");
        response.setStatus(HttpStatus.OK.value());

        response.setHeader("ts", String.valueOf(System.currentTimeMillis()));
        response.setContentType("application/json; charset=utf-8");

        Map<String, Object> data = new HashMap<>();
        data.put("msg", authentication.getName() + " 登录成功");
        response.getOutputStream().write(new ObjectMapper().writeValueAsString(data).getBytes(StandardCharsets.UTF_8));
    }

    private void publishEvent(HttpServletRequest request, Authentication authentication) {
        String ip = request.getRemoteAddr();
        String realIp = request.getHeader("X-Real-IP");
        if (StringUtils.hasText(realIp)) {
            ip += ";X-Real-IP:" + realIp;
        }
        String forwardIp = request.getHeader("X-Forwarded-For");
        if (StringUtils.hasText(forwardIp)) {
            ip += ";X-Forwarded-For:" + forwardIp;
        }
        LoginDto dto = LoginDto.builder()
                .account(authentication.getName())
                .ip(ip)
                .build();
        Publisher.publishEvent(dto);
    }
}