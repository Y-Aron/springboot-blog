package com.aron.blog.config;

import com.alibaba.fastjson.support.spring.FastJsonRedisSerializer;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Y-Aron
 * @create 2018-10-10 0:48
 */
@Configuration
@EnableCaching
public class CacheConfig {

    // 缓存过期时间
    private static final long EXPIRE_TIME = 3600;
    // 缓存前缀
    private static final String REDIS_CACHE_KEY_PREFIX = "CACHE::%s::";


    @Bean
    @Primary //当有多个管理器的时候，必须使用该注解在一个管理器上注释：表示该管理器为默认的管理器
    public CacheManager cacheManager(LettuceConnectionFactory connectionFactory) {
        //序列化方式 JSONObject
        FastJsonRedisSerializer<Object> fastJsonRedisSerializer = new FastJsonRedisSerializer<>(Object.class);

        RedisSerializationContext.SerializationPair<Object> pair = RedisSerializationContext
                .SerializationPair.fromSerializer(fastJsonRedisSerializer);


        RedisCacheConfiguration cacheConfiguration = RedisCacheConfiguration
                .defaultCacheConfig().entryTtl(Duration.ofSeconds(EXPIRE_TIME))
                .disableCachingNullValues()
                .computePrefixWith(s -> String.format(REDIS_CACHE_KEY_PREFIX, s))
                .serializeValuesWith(pair);

        Set<String> cacheNames = new HashSet<>();
        cacheNames.add("user");
        cacheNames.add("test");

        Map<String, RedisCacheConfiguration> configMap = new HashMap<>();
        configMap.put("user", cacheConfiguration.entryTtl(Duration.ofHours(1)));
        configMap.put("test", cacheConfiguration.entryTtl(Duration.ofMinutes(3)));

        return RedisCacheManager.builder(connectionFactory)
                .initialCacheNames(cacheNames)
                .withInitialCacheConfigurations(configMap)
                .build();
    }

//    @Bean
//    @Primary //当有多个管理器的时候，必须使用该注解在一个管理器上注释：表示该管理器为默认的管理器
//    public CacheManager cacheManager(LettuceConnectionFactory connectionFactory) {
//        //初始化一个RedisCacheWriter
//        RedisCacheWriter redisCacheWriter = RedisCacheWriter.nonLockingRedisCacheWriter(connectionFactory);
//
//        //序列化方式 JSONObject
//        FastJsonRedisSerializer<Object> fastJsonRedisSerializer = new FastJsonRedisSerializer<>(Object.class);
//
//        RedisSerializationContext.SerializationPair<Object> pair = RedisSerializationContext
//                .SerializationPair.fromSerializer(fastJsonRedisSerializer);
//
//        RedisCacheConfiguration redisCacheConfig = RedisCacheConfiguration
//                // 这是序列化方式
//                .defaultCacheConfig().serializeValuesWith(pair)
//                // 设置过期时间
//                .entryTtl(Duration.ofSeconds(EXPIRE_TIME))
//                // 设置key前缀
//                .computePrefixWith(s -> String.format(REDIS_CACHE_KEY_PREFIX, s));
//
//        //初始化 RedisCacheManager
//        return new RedisCacheManager(redisCacheWriter, redisCacheConfig);
//    }
}