package beinet.cn.assetmanagement;

import beinet.cn.assetmanagement.security.BeinetSecurityAutoConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 全局的RuntimeException异常处理类，
     * 返回统一的json格式
     *
     * @param response 响应
     * @param exp      异常
     * @throws IOException 可能的io异常
     */
    @ExceptionHandler(value = RuntimeException.class)
    public void RuntimeExceptionHandler(HttpServletResponse response, RuntimeException exp) throws IOException {
        BeinetSecurityAutoConfiguration.outputDenyMsg(response, exp.getMessage());
    }


}
