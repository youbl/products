package beinet.cn.assetmanagement.security;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * BeinetHandleFail
 *
 * @author youbl
 * @version 1.0
 * @date 2020/11/25 14:59
 */
public class BeinetHandleFail implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
            throws IOException {
        BeinetSecurityAutoConfiguration.outputDenyMsg(response, exception.getMessage());
    }
}
