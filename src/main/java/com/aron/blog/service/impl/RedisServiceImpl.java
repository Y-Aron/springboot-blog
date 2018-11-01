package com.aron.blog.service.impl;

import com.aron.blog.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * @author: Y-Aron
 * @create: 2018-10-27 00:01
 **/
@Service
public class RedisServiceImpl implements RedisService {

    private final RedisTemplate<Object, Object> redis;

    private ValueOperations<Object, Object> operations;

    @Autowired
    public RedisServiceImpl(RedisTemplate<Object, Object> redis) {
        this.redis = redis;
        this.operations = this.redis.opsForValue();
    }

    public boolean clearByPattern(String pattern){
        return redis.delete(redis.keys(pattern)) > 0;
    }

    public boolean delete(String... key){
        if (key != null && key.length > 0){
            if (key.length == 1){
                return redis.delete(key);
            } else {
                return redis.delete(Arrays.asList(key)) > 0;
            }
        }
        return false;
    }

    public boolean hasKey(String key){
        try {
            return redis.hasKey(key);
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean expire(String key, long time){
        try {
            if (time > 0){
                redis.expire(key, time, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e){
            return false;
        }
    }

    public boolean set(Object key, Object value){
        try {
            operations.set(key, value);
            return true;
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean set(Object key, Object value, long expireTime){
        try {
            operations.set(key, value, expireTime, TimeUnit.SECONDS);
            return true;
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean set(Object key, Object value, long expireTime, TimeUnit timeUnit){
        try {
            operations.set(key, value, expireTime, timeUnit);
            return true;
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public Object get(String key){
        try {
            return operations.get(key);
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public long incr(String key, long number){
        if (number < 0){
            throw new RuntimeException("number must be greater than zero ...");
        }
        return operations.increment(key, number);
    }

    public long decr(String key, long number){
        if (number > 0){
            throw new RuntimeException("number must be less than zero ...");
        }
        return operations.increment(key, -number);
    }
}
