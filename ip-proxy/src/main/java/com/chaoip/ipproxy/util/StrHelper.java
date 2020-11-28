package com.chaoip.ipproxy.util;

import java.util.Random;

public class StrHelper {
    //验证码数组
    private static final String codes = "23456789abcdefghjkmnopqrstuvwxyzABCDEFGHJKMNPQRSTUVWXYZ";
    private static final Random rnd = new Random();    //获取随机数对象

    public static String getRndStr(int len) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            sb.append(randomChar());
        }
        return sb.toString();
    }

    /**
     * 获取随机字符
     *
     * @return
     */
    private static char randomChar() {
        int index = rnd.nextInt(codes.length());
        return codes.charAt(index);
    }
}
