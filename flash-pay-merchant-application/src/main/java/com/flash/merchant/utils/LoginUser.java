package com.flash.merchant.utils;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class LoginUser {
    private String mobile;
    private Map<String, Object> payload = new HashMap<>();
    private String clientId;
    private String username;
    private Long tenantId;
}
