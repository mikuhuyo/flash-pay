package com.flash.gateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

/**
 * @author yuelimin
 */
@Configuration
public class JWTConfig {

    @Value("${siging-key}")
    private String SIGNING_KEY;

    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(accessTokenConverter());
    }

    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        // 对称秘钥, 资源服务器使用该秘钥来解密
        converter.setSigningKey(SIGNING_KEY);
        converter.setAccessTokenConverter(new ClientDefaultAccessTokenConverter());
        return converter;
    }

}
