package com.beinet.carsmanagement.utils;

import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

public final class IpHelper {
    private IpHelper() {

    }

    public static String getIpAddr(HttpServletRequest request) {
        System.out.println(request);
        String ipAddress;
        try {
            ipAddress = request.getHeader("x-forwarded-for");
            if (StringUtils.isEmpty(ipAddress)) {
                ipAddress = request.getHeader("x-real-IP");
            }
            if (StringUtils.isEmpty(ipAddress)) {
                ipAddress = request.getRemoteAddr();
            }
            return ipAddress;
        } catch (Exception e) {
            return "";
        }
    }
}
