package com.chaoip.ipproxy.controller.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Result {
    private int code;
    private String msg;

    public static Result error(int code, String msg) {
        return Result.builder().code(code).msg(msg).build();
    }
}
