package beinet.cn.assetmanagement.security;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.security.Principal;

/**
 * AuthDetailArgumentResolver
 *
 * @author youbl
 * @version 1.0
 * @date 2020/11/25 14:54
 */

public class AuthDetailArgumentResolver implements HandlerMethodArgumentResolver {
    /**
     * 判断参数类型，如果是 AuthDetails，则调用 resolveArgument方法生成该参数，并注入
     *
     * @param parameter 参数信息
     * @return true false
     */
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().isAssignableFrom(AuthDetails.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        AuthDetails ret = new AuthDetails();

        // 调用者的信息
        ret.setUserAgent(webRequest.getHeader("user-agent"));

        Principal principal = webRequest.getUserPrincipal();
        if (principal == null) {
            return ret;
        }
        ret.setAccount(principal.getName());
//        if (principal instanceof DirectLoginFilter.DirectAuth) {
//            BeinetUserDetail detail = (BeinetUserDetail) ((DirectLoginFilter.DirectAuth) principal).getPrincipal();
//            ret.setUser(detail.getUser());
//        }

        return ret;
    }
}
