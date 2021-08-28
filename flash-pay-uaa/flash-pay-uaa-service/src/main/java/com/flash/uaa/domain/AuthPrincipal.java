package com.flash.uaa.domain;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class AuthPrincipal {
    /**
     * 用户名
     */
    private String username;
    /**
     * 域  用于扩展
     */
    private String domain;
    /**
     * 认证的类型
     * password: 用户名密码模式类型
     * sms: 短信模式类型
     */
    private String authenticationType;
    /**
     * 附加数据, 不同认证类型可拥有不同的附加数据.
     * 如认证类型为短信时包含smsKey -> sms:3d21042d054548b08477142bbca95cfa;
     * 所有情况下都包含clientId
     */
    private Map<String, Object> payload = new HashMap<>();

}
