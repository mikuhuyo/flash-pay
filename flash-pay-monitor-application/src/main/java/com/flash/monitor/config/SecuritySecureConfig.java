package com.flash.monitor.config;

import de.codecentric.boot.admin.server.config.AdminServerProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

/**
 * @author yuelimin
 * @version 1.0.0
 * @since 11
 */
@Configuration
class SecuritySecureConfig extends WebSecurityConfigurerAdapter {

    private final String adminContextPath;

    public SecuritySecureConfig(AdminServerProperties adminServerProperties) {
        this.adminContextPath = adminServerProperties.getContextPath();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        SavedRequestAwareAuthenticationSuccessHandler successHandler = new SavedRequestAwareAuthenticationSuccessHandler();
        successHandler.setTargetUrlParameter("redirectTo");

        // 跨域设置, SpringBootAdmin客户端通过instances注册, 见InstancesController
        http.csrf()
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .ignoringAntMatchers(adminContextPath + "/instances");

        // 静态资源
        http.authorizeRequests().antMatchers(adminContextPath + "/assets/**").permitAll();
        // 所有请求必须通过认证
        http.authorizeRequests().anyRequest().authenticated();

        // 整合spring-boot-admin-server-ui
        http.formLogin().loginPage("/login").permitAll();
        http.logout().logoutUrl("/logout").logoutSuccessUrl("/login");

        // 启用basic认证 SpringBootAdmin客户端使用的是basic认证
        http.httpBasic();
    }
}


