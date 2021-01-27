package beinet.cn.assetmanagement.security;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * BeinetAuthenticationEntryPoint
 * 匿名用户访问需要权限的url会转到此入口
 *
 * @author youbl
 * @version 1.0
 * @date 2020/11/25 15:00
 */
public class BeinetAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException {
        if (isAjax(request)) {
            BeinetSecurityAutoConfiguration.outputDenyMsg(response, authException.getMessage());
        } else {
            response.sendRedirect(BeinetSecurityAutoConfiguration.LOGIN_PAGE + getReturnUrl(request));
        }
    }

    /**
     * 判断是否ajax请求
     *
     * @param request 当前请求上下文
     * @return 是否
     */
    private static boolean isAjax(HttpServletRequest request) {
        String header = request.getHeader("accept");
        if (header != null && header.contains("application/json"))
            return true;
        header = request.getHeader("x-requested-with");
        return header != null && header.equalsIgnoreCase("XMLHttpRequest");
    }

    /**
     * 获取当前请求的地址，作为登录页的返回url参数
     *
     * @param request 当前请求上下文
     * @return 当前请求地址
     * @throws UnsupportedEncodingException 可能的异常
     */
    private static String getReturnUrl(HttpServletRequest request) throws UnsupportedEncodingException {
        String query = request.getQueryString();
        String rUrl = request.getRequestURI();
        if (query != null && !query.isEmpty())
            rUrl += "?" + query;

        return "?returnUrl=" + URLEncoder.encode(rUrl, "UTF-8");
    }
}
