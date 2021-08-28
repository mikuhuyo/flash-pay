package com.flash.gateway.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.AuthenticationEntryPoint;

/**
 * @author yuelimin
 */
@Configuration
public class ResourceServerConfig {

    public static final String RESOURCE_ID = "shanju-resource";

    private final AuthenticationEntryPoint point = new RestOAuth2AuthExceptionEntryPoint();
    private final RestAccessDeniedHandler handler = new RestAccessDeniedHandler();

    /**
     * 资源拦截
     * 统一认证中心
     */
    @Configuration
    @EnableResourceServer
    public static class UaaServerConfig extends ResourceServerConfigurerAdapter {
        @Autowired
        private TokenStore tokenStore;

        @Override
        public void configure(ResourceServerSecurityConfigurer resources) {
            resources.tokenStore(tokenStore).resourceId(RESOURCE_ID).stateless(true);
        }

        @Override
        public void configure(HttpSecurity http) throws Exception {
            http.sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                    .and()
                    .authorizeRequests()
                    .antMatchers("/uaa/druid/**").denyAll()
                    .antMatchers("/uaa/**").permitAll();
        }

    }

    /**
     * 运营平台
     */
    @Configuration
    @EnableResourceServer
    public class OperationServerConfig extends ResourceServerConfigurerAdapter {

        @Autowired
        private TokenStore tokenStore;

        @Override
        public void configure(ResourceServerSecurityConfigurer resources) {
            resources.tokenStore(tokenStore).resourceId(RESOURCE_ID).stateless(true);
            resources.authenticationEntryPoint(point).accessDeniedHandler(handler);
        }

        @Override
        public void configure(HttpSecurity http) throws Exception {

            http.sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                    .and()
                    .authorizeRequests()
                    .antMatchers("/operation/swagger-ui.html").denyAll()
                    .antMatchers("/operation/webjars/**").denyAll()
                    .antMatchers("/operation/druid/**").denyAll()
                    .antMatchers("/operation/m/**").access("#oauth2.hasScope('read') and #oauth2.clientHasRole('ROLE_OPERATION')")
                    .antMatchers("/operation/**").permitAll();
        }
    }

    /**
     * 商户平台
     */
    @Configuration
    @EnableResourceServer
    public class MerchantServerConfig extends ResourceServerConfigurerAdapter {

        @Autowired
        private TokenStore tokenStore;

        @Override
        public void configure(ResourceServerSecurityConfigurer resources) {
            resources.tokenStore(tokenStore).resourceId(RESOURCE_ID).stateless(true);
            resources.authenticationEntryPoint(point).accessDeniedHandler(handler);
        }

        @Override
        public void configure(HttpSecurity http) throws Exception {

            http.sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                    .and()
                    .authorizeRequests()
                    .antMatchers("/merchant/druid/**").denyAll()
                    .antMatchers("/merchant/my/**").access("#oauth2.hasScope('read') and #oauth2.clientHasRole('ROLE_MERCHANT')")
                    .antMatchers("/merchant/swagger-ui.html").permitAll()
                    .antMatchers("/merchant/webjars/**").permitAll()
                    .antMatchers("/merchant/**").permitAll();
        }
    }

    /**
     * 门户网站
     */
    @Configuration
    @EnableResourceServer
    public class PortalServerConfig extends ResourceServerConfigurerAdapter {

        @Autowired
        private TokenStore tokenStore;

        @Override
        public void configure(ResourceServerSecurityConfigurer resources) {
            resources.tokenStore(tokenStore).resourceId(RESOURCE_ID).stateless(true);
            resources.authenticationEntryPoint(point).accessDeniedHandler(handler);
        }

        @Override
        public void configure(HttpSecurity http) throws Exception {

            http.sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                    .and()
                    .authorizeRequests()
                    .antMatchers("/portal/swagger-ui.html").denyAll()
                    .antMatchers("/portal/webjars/**").denyAll()
                    .antMatchers("/portal/druid/**").denyAll()
                    .antMatchers("/portal/m/**").access("#oauth2.hasScope('read') and #oauth2.clientHasRole('ROLE_PORTAL')")
                    .antMatchers("/portal/**").permitAll();
        }
    }

    /**
     * 开放API
     */
    @Configuration
    @EnableResourceServer
    public class ApiServerConfig extends ResourceServerConfigurerAdapter {

        @Autowired
        private TokenStore tokenStore;

        @Override
        public void configure(ResourceServerSecurityConfigurer resources) {
            resources.tokenStore(tokenStore).resourceId(RESOURCE_ID).stateless(true);
            resources.authenticationEntryPoint(point).accessDeniedHandler(handler);
        }

        @Override
        public void configure(HttpSecurity http) throws Exception {

            http.sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                    .and()
                    .authorizeRequests()
                    .antMatchers("/api/swagger-ui.html").denyAll()
                    .antMatchers("/api/webjars/**").denyAll()
                    .antMatchers("/api/druid/**").denyAll()
                    .antMatchers("/api/**").access("#oauth2.hasScope('read') and #oauth2.clientHasRole('ROLE_API')");
        }
    }

    /**
     * 第三方支付代理服务
     */
    @Configuration
    @EnableResourceServer
    public class PaymentAgentReceiverServerConfig extends ResourceServerConfigurerAdapter {

        @Autowired
        private TokenStore tokenStore;

        @Override
        public void configure(ResourceServerSecurityConfigurer resources) {
            resources.tokenStore(tokenStore).resourceId(RESOURCE_ID).stateless(true);
            resources.authenticationEntryPoint(point).accessDeniedHandler(handler);
        }

        @Override
        public void configure(HttpSecurity http) throws Exception {

            http.sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                    .and()
                    .authorizeRequests()
                    .antMatchers("/api/swagger-ui.html").denyAll()
                    .antMatchers("/api/webjars/**").denyAll()
                    .antMatchers("/api/druid/**").denyAll()
                    .antMatchers("/payment-receiver/**").permitAll();
        }
    }
}
