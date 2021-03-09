package beinet.cn.assetmanagement.utils;

import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

public final class RequestHelper {
    /**
     * 返回完整的客户端IP，可能有多个IP
     *
     * @param request 请求上下文
     * @return ip
     */
    public static String getFullIP(HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        String realIp = request.getHeader("X-Real-IP");
        if (StringUtils.hasText(realIp)) {
            ip += ";X-Real-IP:" + realIp;
        }
        String forwardIp = request.getHeader("X-Forwarded-For");
        if (StringUtils.hasText(forwardIp)) {
            ip += ";X-Forwarded-For:" + forwardIp;
        }
        return ip;
    }
}
