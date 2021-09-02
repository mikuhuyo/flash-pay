package com.flash.merchant.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.flash.common.util.EncryptUtil;
import com.flash.common.util.StringUtil;
import com.flash.merchant.utils.LoginUser;
import com.flash.merchant.utils.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yuelimin
 */
@Slf4j
@Component
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {

        //  放行静态资源
        String requestUrl = httpServletRequest.getRequestURL().toString();
        log.info("requestUrl: {}", requestUrl);
        // 不需要过滤的资源
        String[] urls = {".js", ".css", ".ico", ".jpg", ".png", ".gif"};
        if (StringUtil.isContains(urls, requestUrl)
                || requestUrl.indexOf("/actuator") != -1
                || requestUrl.indexOf("/v2/api-docs") != -1
                || requestUrl.indexOf("/swagger-resources") != -1
                || requestUrl.indexOf("/swagger-ui.html") != -1
                || requestUrl.indexOf("/merchants/register") != -1
                || requestUrl.indexOf("/sms") != -1
                || requestUrl.indexOf("/my/tenants-merchants") != -1) {

            log.info("该资源放行: {}", requestUrl);
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }

        // 校验jsontoken
        // 网关给后端微服务传的token令牌
        String jsonToken = httpServletRequest.getHeader("jsonToken");
        log.info("jsonToken: {}", jsonToken);

        if (StringUtils.isBlank(jsonToken)) {
            responseMessage(999911, "调用微服务-没有jsontoken令牌", httpServletRequest, httpServletResponse);
            return;
        }

        // base64解码 后判断有没有当前租户的权限信息
        String json = EncryptUtil.decodeUTF8StringBase64(jsonToken);
        log.info("json令牌: {}", json);
        Map<String, Object> tokenMap = JSONObject.parseObject(json, new TypeReference<Map<String, Object>>() {
        });
        log.info("tokenMap: {}", JSON.toJSONString(tokenMap));

        // 需注意payload的值是字符串需要再次转map
        String payload = (String) tokenMap.get("payload");
        log.info("paload: {}", payload);
        Map<String, Object> payloadMap = JSONObject.parseObject(payload, new TypeReference<Map<String, Object>>() {
        });
        log.info("payloadMap: {}", JSON.toJSONString(payloadMap));

        // 增加将当前登入用户信息放入requestAttribute
        LoginUser loginUser = SecurityUtil.convertTokenToLoginUser(jsonToken);
        String tenantId = httpServletRequest.getHeader("tenantId");
        if (StringUtils.isNotBlank(tenantId)) {
            loginUser.setTenantId(Long.parseLong(tenantId));
        }
        httpServletRequest.setAttribute("jsonToken", loginUser);

        // 获取当前租户的权限信息
        Map tenantmap = (Map) payloadMap.get(tenantId);
        log.info("tenantmap:{}", JSON.toJSONString(tenantmap));
        if (tenantmap == null) {
            log.error("json-token中没有当前租户 {} 的信息", tenantId);
            //throw new BusinessException(CommonErrorCode.E_999913);
            responseMessage(999913, "调用微服务-json-token令牌有误-没有当前租户信息", httpServletRequest, httpServletResponse);
            return;
        }

        // 取对应的 user_authorities信息
        // 将权限放到几个arraylist集合中
        // 不需要角色
        Map<String, JSONArray> userAuthMap = (Map<String, JSONArray>) tenantmap.get("user_authorities");
        log.info("userAuthMap: {}", JSON.toJSONString(userAuthMap));
        if (userAuthMap == null) {
            log.error("json-token中该租户 {} 下没有权限信息", tenantId);
            responseMessage(999914, "调用微服务-json-token令牌有误-该租户下没有权限信息", httpServletRequest, httpServletResponse);
            return;
        }

        List<String> rolePrivileges = new ArrayList<>();
        for (Map.Entry<String, JSONArray> entry : userAuthMap.entrySet()) {
            // String roleName = entry.getKey();
            List<String> privileges = JSONObject.parseArray(JSON.toJSONString(entry.getValue()), String.class);
            // rolePrivileges.add(roleName);
            rolePrivileges.addAll(privileges);
        }
        log.info("该租户 {} 拥有的权限最终个数{},分别是: {}", tenantId, rolePrivileges.size(), rolePrivileges);
        filterChain.doFilter(httpServletRequest, httpServletResponse);

    }

    public void responseMessage(int code, String desc, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
        // 由于全局异常处理器无法捕获filter中的异常信息
        // 利用此map将异常信息直接响应到前端
        Map<String, Object> exceptionMap = new HashMap<String, Object>();
        exceptionMap.put("code", code);
        exceptionMap.put("desc", desc);
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType("application/json;charset=UTF-8");
        httpServletResponse.getWriter().print(JSONObject.toJSON(exceptionMap));
    }
}
