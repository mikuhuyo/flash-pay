package com.flash.uaa.common.utils;

import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;

public class WebUtils {
    public static final String UTF_8 = "UTF-8";

    public static final String VERSION = "2.0.1";

    private static ThreadLocal<String> ipThreadLocal = new ThreadLocal<>();

    private WebUtils() {
    }

    public static String getIp() {
        return ipThreadLocal.get();
    }

    public static void setIp(String ip) {
        ipThreadLocal.set(ip);
    }

    /**
     * Retrieve client ip address
     *
     * @param request HttpServletRequest
     * @return IP
     */
    public static String retrieveClientIp(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (isUnAvailableIp(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (isUnAvailableIp(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (isUnAvailableIp(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    private static boolean isUnAvailableIp(String ip) {
        return StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip);
    }

}
