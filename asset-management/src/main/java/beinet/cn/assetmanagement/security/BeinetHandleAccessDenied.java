package beinet.cn.assetmanagement.security;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * BeinetHandleAccessDenied
 * 已登录用户访问无权限的url时，会跳到这里
 *
 * @author youbl
 * @version 1.0
 * @date 2020/11/25 15:02
 */
public class BeinetHandleAccessDenied implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException)
            throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        BeinetSecurityAutoConfiguration.outputDenyMsg(response, accessDeniedException.getMessage());
    }
}