package com.chaoip.ipproxy.controller;

//import org.springframework.boot.autoconfigure.web.ErrorProperties;

import lombok.Builder;
import lombok.Data;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * GlobalErrorController
 *
 * @author youbl
 * @version 1.0
 * @date 2020/11/26 15:51
 */
@RestController
public class GlobalErrorController implements ErrorController {
    private final String ERROR_PATH = "/error";
    private final ErrorAttributes errorAttributes;

    //    private final ErrorProperties errorProperties;
    public GlobalErrorController(ErrorAttributes errorAttributes) {
        this.errorAttributes = errorAttributes;
    }

    @Override
    public String getErrorPath() {
        return ERROR_PATH;
    }


//    @RequestMapping(value = ERROR_PATH, produces = {"text/html"})
//    public ModelAndView errorHtml(HttpServletRequest request, HttpServletResponse response) {
//        int code = response.getStatus();
//        if (404 == code) {
//            return new ModelAndView("error/404");
//        } else if (403 == code) {
//            return new ModelAndView("error/403");
//        } else if (401 == code) {
//            return new ModelAndView("login");
//        } else {
//            return new ModelAndView("error/500");
//        }
//    }

    @RequestMapping(value = ERROR_PATH)
    public Result handleError(WebRequest request, HttpServletResponse response) {
        int code = response.getStatus();
        String msg;
        switch (code) {
            case 401:
                msg = "未登录";
                break;
            case 403:
                msg = "无权限";
                break;
            case 404:
                msg = "资源不存在";
                break;
            default:
                Throwable error = errorAttributes.getError(request);
                msg = error == null ? "服务器异常" : error.getMessage();
                break;
        }
        return Result.error(code, msg);
    }

    @Data
    @Builder
    public static class Result {
        private int code;
        private String msg;

        public static Result error(int code, String msg) {
            return Result.builder().code(code).msg(msg).build();
        }
    }
}
