package com.flash.common.domain;

/**
 * 浏览器类型
 *
 * @author yuelimin
 */
public enum BrowserType {
    /**
     * 支付宝
     */
    ALIPAY,
    /**
     * 微信
     */
    WECHAT,
    /**
     * pc端浏览器
     */
    PC_BROWSER,
    /**
     * 手机端浏览器
     */
    MOBILE_BROWSER;


    /**
     * 根据UA获取浏览器类型
     *
     * @param userAgent userAgent
     * @return 浏览器类型
     */
    public static BrowserType valueOfUserAgent(String userAgent) {
        if (userAgent != null && userAgent.contains("AlipayClient")) {
            return BrowserType.ALIPAY;
        } else if (userAgent != null && userAgent.contains("MicroMessenger")) {
            return BrowserType.WECHAT;
        } else {
            return BrowserType.MOBILE_BROWSER;
        }
    }
}
