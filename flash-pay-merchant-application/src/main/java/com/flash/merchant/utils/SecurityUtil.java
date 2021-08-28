package com.flash.merchant.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.flash.common.util.EncryptUtil;
import com.flash.merchant.api.IMerchantService;
import com.flash.merchant.api.dto.MerchantDto;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/***
 * 获取当前登录用户信息
 * 前端配置token, 后续每次请求并通过Header方式发送至后端
 */
public class SecurityUtil {
    public static LoginUser getUser() {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (servletRequestAttributes != null) {
            HttpServletRequest request = servletRequestAttributes.getRequest();

            Object jwt = request.getAttribute("jsonToken");
            if (jwt instanceof LoginUser) {
                return (LoginUser) jwt;
            }
        }
        return new LoginUser();
    }

    public static Long getMerchantId() {
        IMerchantService merchantService = ApplicationContextHelper.getBean(IMerchantService.class);
        assert merchantService != null;
        MerchantDto merchant = merchantService.queryMerchantByTenantId(getUser().getTenantId());
        Long merchantId = null;
        if (merchant != null) {
            merchantId = merchant.getId();
        }
        return merchantId;
    }

    /**
     * 转换明文jsonToken为用户对象
     *
     * @param token 加密字符串
     * @return LoginUser
     */
    public static LoginUser convertTokenToLoginUser(String token) {
        token = EncryptUtil.decodeUTF8StringBase64(token);
        LoginUser user = new LoginUser();
        JSONObject jsonObject = JSON.parseObject(token);
        String payload = jsonObject.getString("payload");
        Map<String, Object> payloadMap = JSON.parseObject(payload, Map.class);
        user.setPayload(payloadMap);
        user.setClientId(jsonObject.getString("client_id"));
        user.setMobile(jsonObject.getString("mobile"));
        user.setUsername(jsonObject.getString("user_name"));
        return user;
    }
}
