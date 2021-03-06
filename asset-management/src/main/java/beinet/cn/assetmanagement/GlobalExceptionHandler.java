package beinet.cn.assetmanagement;

import beinet.cn.assetmanagement.security.BeinetSecurityAutoConfiguration;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.HashMap;
import java.util.Map;

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
        String msg = exp == null ? "没有异常明细" : exp.getMessage();
        if (exp instanceof DataIntegrityViolationException &&
                exp.getCause() instanceof ConstraintViolationException &&
                exp.getCause().getCause() instanceof SQLIntegrityConstraintViolationException) {
            msg = exp.getCause().getCause().getMessage();
        }
        if (StringUtils.isEmpty(msg)) {
            msg = exp.getClass().getName();
        }
        log.error("RuntimeException: " + exp);
        BeinetSecurityAutoConfiguration.outputDenyMsg(response, msg);
    }

    @ExceptionHandler(value = Exception.class)
    public void ExceptionHandler(HttpServletResponse response, Exception exp) throws IOException {
        log.error("OtherException: " + exp);

        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());

        response.setContentType("application/json; charset=utf-8");
        response.setHeader("ts", String.valueOf(System.currentTimeMillis()));

        String msg = exp.getMessage();
        if (exp instanceof MethodArgumentNotValidException) {
            msg = "";
            for (ObjectError oErr : ((MethodArgumentNotValidException) exp).getBindingResult().getAllErrors()) {
                msg += oErr.getDefaultMessage() + "; ";
            }
        }

        Map<String, Object> data = new HashMap<>();
        data.put("msg", msg);
        response.getOutputStream().write(new ObjectMapper().writeValueAsString(data).getBytes(StandardCharsets.UTF_8));
    }
}
