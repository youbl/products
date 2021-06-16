package com.chaoip.ipproxy.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result handleException(Exception exp) {
        log.error("全局异常", exp);
        return Result.Error(500, exp.getMessage());
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Result {
        public static Result Error(int code, String msg) {
            return new Result(code, msg);
        }

        private int code;
        private String msg;
    }
}
