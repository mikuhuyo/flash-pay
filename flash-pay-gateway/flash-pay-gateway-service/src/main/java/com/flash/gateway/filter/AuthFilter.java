package com.flash.gateway.filter;

import com.alibaba.fastjson.JSON;
import com.flash.common.util.EncryptUtil;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yuelimin
 * @version 1.0.0
 * @since 11
 */
@Component
public class AuthFilter extends ZuulFilter {

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext ctx = RequestContext.getCurrentContext();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 无token访问网关内资源的情况, 目前仅有uaa服务直接暴露
        if (!(authentication instanceof OAuth2Authentication)) {
            return null;
        }

        OAuth2Authentication oauth2Authentication = (OAuth2Authentication) authentication;
        Authentication userAuthentication = oauth2Authentication.getUserAuthentication();

        Map<String, String> jsonToken = new HashMap<>(oauth2Authentication.getOAuth2Request().getRequestParameters());

        if (userAuthentication != null) {
            jsonToken.put("user_name", userAuthentication.getName());
        }

        ctx.addZuulRequestHeader("jsonToken", EncryptUtil.encodeUTF8StringBase64(JSON.toJSONString(jsonToken)));
        return null;
    }

}
