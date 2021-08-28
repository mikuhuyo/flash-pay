package com.flash.uaa.integration;

import com.alibaba.fastjson.JSON;
import com.flash.uaa.domain.AuthPrincipal;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Map;

/**
 * 统一用户认证处理, 集成了网页(简化模式, 授权码模式用户登录)认证  与  password模式认证
 *
 * @author yuelimin
 */
public class IntegrationUserDetailsAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

    private IntegrationUserDetailsAuthenticationHandler authenticationHandler = null;

    public IntegrationUserDetailsAuthenticationProvider(IntegrationUserDetailsAuthenticationHandler authenticationHandler) {
        this.authenticationHandler = authenticationHandler;
    }

    @Override
    @SuppressWarnings("deprecation")
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        // 仅在父类中验证用户的状态
    }


    @Override
    protected final UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        try {
            UserDetails loadedUser = authenticationUser(authentication);

            if (loadedUser == null) {
                throw new InternalAuthenticationServiceException("UserDetailsService returned null, which is an interface contract violation");
            }
            return loadedUser;
        } catch (UsernameNotFoundException | InternalAuthenticationServiceException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex);
        }
    }

    private UserDetails authenticationUser(UsernamePasswordAuthenticationToken authentication) {

        if (authentication.getPrincipal() == null) {
            throw new BadCredentialsException("username is blank");
        }
        String username = authentication.getName();

        if (authentication.getCredentials() == null) {
            throw new BadCredentialsException("Credentials is blank");
        }
        String credentials = authentication.getCredentials().toString();

        AuthPrincipal authPrincipal = null;

        try {
            authPrincipal = JSON.parseObject(username, AuthPrincipal.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BadCredentialsException("username parseObject error");
        }

        if (authentication.getDetails() instanceof Map) {
            Map detailsMap = (Map) authentication.getDetails();
            authPrincipal.getPayload().putAll(detailsMap);
        }

        return authenticationHandler.authentication(authPrincipal, credentials);
    }

}
