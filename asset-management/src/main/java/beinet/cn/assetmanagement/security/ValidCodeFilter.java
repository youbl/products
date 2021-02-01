package beinet.cn.assetmanagement.security;

import beinet.cn.assetmanagement.user.service.ValidcodeService;
import beinet.cn.assetmanagement.utils.CachingStreamRequestWrapper;
import org.apache.commons.io.IOUtils;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidCodeFilter extends OncePerRequestFilter {
    private static final String VALID_CODE = "imgCode";
    private static final String VALID_CODE_SN = "imgSn";

    private final List<RequestMatcher> pathMatcher;
    private final ValidcodeService validcodeService;
    private final AuthenticationFailureHandler failureHandler;

    protected ValidCodeFilter(ValidcodeService validcodeService, AuthenticationFailureHandler failureHandler) {
        this.validcodeService = validcodeService;
        this.failureHandler = failureHandler;

        this.pathMatcher = new ArrayList<>();
        // 只校验POST的 /login登录请求 和 /login/users 注册请求
        this.pathMatcher.add(new AntPathRequestMatcher("/login", "POST"));
        // 只校验POST的 /login登录请求 和 /login/users 注册请求
        this.pathMatcher.add(new AntPathRequestMatcher("/login/users", "POST"));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // 已登录，不校验验证码
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            filterChain.doFilter(request, response);
            return;
        }

        boolean needCheck = false;
        for (RequestMatcher matcher : this.pathMatcher) {
            if (matcher.matches(request)) {
                needCheck = true;
                break;
            }
        }
        if (!needCheck) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            request = attemptValidCode(request, response);
            filterChain.doFilter(request, response);
        } catch (AuthenticationServiceException exp) {
            failureHandler.onAuthenticationFailure(request, response, exp);
        }
    }

    protected HttpServletRequest attemptValidCode(HttpServletRequest request, HttpServletResponse httpServletResponse)
            throws AuthenticationException, IOException {
        if (!request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("ValidCode method not supported: " + request.getMethod());
        }

        String imgCode = request.getParameter(VALID_CODE);
        String imgSn = request.getParameter(VALID_CODE_SN);
        if (StringUtils.isEmpty(imgCode)) {
            // 包装一下并返回，避免在后续的Controller里报错： HttpMessageNotReadableException: Required request body is missing
            request = new CachingStreamRequestWrapper(request);
            String[] codeAndSn = parseValidCodeFromPost(request);
            imgCode = codeAndSn[0];
            imgSn = codeAndSn[1];
        }

        if (StringUtils.isEmpty(imgCode) || StringUtils.isEmpty(imgSn)) {
            throw new AuthenticationServiceException("ValidCode or sn not provided.");
        }
        if (!validcodeService.validByCodeAndSn(imgCode, imgSn)) {
            throw new AuthenticationServiceException("验证码错误");
        }
        return request;
    }

    static String[] parseValidCodeFromPost(HttpServletRequest request) throws IOException {
        String[] ret = new String[2];
        // String str = getRequestPostStr(request);
        String str = IOUtils.toString(((CachingStreamRequestWrapper) request).getBody(), request.getCharacterEncoding());

        if (StringUtils.isEmpty(str)) {
            return ret;
        }
        Pattern pattern = Pattern.compile("\"([^\"]+)\"\\s*:\\s*\"([^\"]+)\"[,\\}]");
        Matcher matcher = pattern.matcher(str);
        while (matcher.find()) {
            switch (matcher.group(1)) {
                case VALID_CODE:
                    ret[0] = matcher.group(2);
                    break;
                case VALID_CODE_SN:
                    ret[1] = matcher.group(2);
                    break;
            }
        }
        return ret;
    }

    static String getRequestPostStr(HttpServletRequest request)
            throws IOException {
        byte[] buffer = getRequestPostBytes(request);
        if (buffer.length <= 0) {
            return "";
        }
        String charEncoding = request.getCharacterEncoding();
        if (charEncoding == null) {
            charEncoding = "UTF-8";
        }
        return new String(buffer, charEncoding);
    }

    static byte[] getRequestPostBytes(HttpServletRequest request)
            throws IOException {
        int contentLength = request.getContentLength();
        if (contentLength < 0) {
            return new byte[0];
        }

        byte buffer[] = new byte[contentLength];
        for (int i = 0; i < contentLength; ) {
            int readlen = request.getInputStream().read(buffer, i,
                    contentLength - i);
            if (readlen == -1) {
                break;
            }
            i += readlen;
        }
        return buffer;
    }

}
