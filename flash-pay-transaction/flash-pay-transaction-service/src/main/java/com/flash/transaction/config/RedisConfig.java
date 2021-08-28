package com.flash.transaction.config;

import com.flash.common.cache.Cache;
import com.flash.transaction.common.utils.RedisCache;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @author yuelimin
 * @version 1.0.0
 * @since 11
 */
@Configuration
public class RedisConfig {
    @Bean
    public Cache cache(StringRedisTemplate redisTemplate){
        return new RedisCache(redisTemplate);
    }
}
