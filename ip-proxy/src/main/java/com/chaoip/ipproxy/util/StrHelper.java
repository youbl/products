package com.chaoip.ipproxy.util;

import java.util.Random;

public class StrHelper {
    // 图形验证码数组
    private static final String CODES = "23456789abcdefghjkmnopqrstuvwxyzABCDEFGHJKMNPQRSTUVWXYZ";
    // 手机验证码数组
    private static final String NUMS = "0123456789";
    private static final Random rnd = new Random();    //获取随机数对象

    /**
     * 获取随机字符串
     *
     * @param len  字符串长度
     * @param type 类型，0为图形，任意字符；1为手机，只限数字
     * @return 字符串
     */
    public static String getRndStr(int len, int type) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            sb.append(randomChar(type));
        }
        return sb.toString();
    }

    /**
     * 获取随机字符
     *
     * @return 字符
     */
    private static char randomChar(int type) {
        String arrStr;
        if (type == 1) {
            arrStr = NUMS;
        } else {
            arrStr = CODES;
        }
        int index = rnd.nextInt(arrStr.length());
        return arrStr.charAt(index);
    }
}
