package com.flash.uaa.integration;

import com.alibaba.fastjson.JSON;
import com.flash.common.domain.BusinessException;
import com.flash.common.util.StringUtil;
import com.flash.uaa.domain.AuthPrincipal;
import com.flash.uaa.domain.UnifiedUserDetails;
import com.flash.user.api.TenantService;
import com.flash.user.api.dto.authorization.AuthorizationInfoDTO;
import com.flash.user.api.dto.resource.ApplicationDTO;
import com.flash.user.api.dto.resource.ResourceDTO;
import com.flash.user.api.dto.tenant.LoginInfoDTO;
import com.flash.user.api.dto.tenant.LoginRequestDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class IntegrationUserDetailsAuthenticationHandler {

    private TenantService tenantService;


    public void setTenantService(TenantService tenantService) {
        this.tenantService = tenantService;
    }


    /**
     * 认证处理 简易判断, 后期优化结构
     *
     * @param authPrincipal 认证用户的身份信息
     * @param credentials   认证用户的登录凭证
     * @return
     */
    public UnifiedUserDetails authentication(AuthPrincipal authPrincipal, String credentials) {
        /**
         * 1.调用TenantService.login(LoginRequestDTO loginRequest)获取应用, 权限, 资源, 设置为UnifiedUserDetails.payload
         * 2.根据client_id获取当前接入应用所属租户ID(ResourceService.queryApplication(String applicationCode)), 设置为UnifiedUserDetails.tenantId
         */
        LoginRequestDTO loginRequestDTO = new LoginRequestDTO();
        loginRequestDTO.setPrincipal(authPrincipal.getUsername());
        loginRequestDTO.setCertificate(credentials);
        loginRequestDTO.setAuthenticationType(authPrincipal.getAuthenticationType());
        if ("sms".equals(authPrincipal.getAuthenticationType())) {
            loginRequestDTO.setSmsKey(authPrincipal.getPayload().get("smsKey").toString());
        }
        LoginInfoDTO loginInfoDTO = null;
        try {
            loginInfoDTO = tenantService.login(loginRequestDTO);
            log.info("loginInfoDTO:{}", JSON.toJSONString(loginInfoDTO));
        } catch (Exception ex) {
            ex.printStackTrace();
            if (ex instanceof BusinessException) {
                BusinessException be = (BusinessException) ex;
                log.info(JSON.toJSONString(be));
                throw new BadCredentialsException("login error-" + be.getErrorCode().getDesc());
            } else {
                throw new BadCredentialsException("login error " + ex.getMessage());
            }
        }
        if (loginInfoDTO == null) {
            throw new BadCredentialsException("User not found");
        }

        UnifiedUserDetails userDetails = new UnifiedUserDetails(authPrincipal.getUsername(), credentials);
        userDetails.setMobile(loginInfoDTO.getMobile());
        // 拼最后的payload结构
        Map<Long, Object> payload = new HashMap<>();
        // 得到权限信息
        Map<Long, AuthorizationInfoDTO> tenantAuthorizationInfoMap = loginInfoDTO.getTenantAuthorizationInfoMap();
        // 得到资源信息
        Map<Long, List<ResourceDTO>> resources = loginInfoDTO.getResources();

        for (Map.Entry<Long, AuthorizationInfoDTO> entry : tenantAuthorizationInfoMap.entrySet()) {
            Long tenantId = entry.getKey();
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("user_authorities", entry.getValue().getRolePrivilegeMap());
            map.put("resources", resources.get(tenantId));

            payload.put(tenantId, map);
        }
        userDetails.setPayload(payload);

        Map<String, Object> detailsMap = authPrincipal.getPayload();
        String client_id = (String) detailsMap.get("client_id");

        // 授权码模式 简化模式没有client_id  密码模式会传client_id
        if (StringUtil.isNotBlank(client_id)) {
            ApplicationDTO applicationDTO = tenantService.getApplicationDTOByClientId(client_id);
            Map<String, Object> tenantIdMap = new HashMap<String, Object>();
            tenantIdMap.put("tenantId", applicationDTO.getTenantId());
            userDetails.setTenant(tenantIdMap);
        }
        log.info("@@@@@@@@@@@: {}", JSON.toJSONString(userDetails));
        return userDetails;
    }

}

