package com.aron.blog.service;

import java.util.concurrent.TimeUnit;

/**
 * @author: Y-Aron
 * @create: 2018-10-27 00:00
 **/
public interface RedisService {

    /**
     * 根据表达式删除所有key
     * @param pattern 表达式
     * @return true/false
     */
    boolean clearByPattern(String pattern);

    /**
     * 删除一个或多个key
     * @param key Array
     * @return boolean
     */
    boolean delete(String... key);

    /**
     * 判断key是否存在
     * @param key String
     * @return boolean
     */
    boolean hasKey(String key);


    /**
     * 设置键的过期时间
     * @param key 键
     * @param time 过期时间
     * @return true / false
     */
    boolean expire(String key, long time);

    /**
     * 将数据写入redis
     * @param key 键
     * @param value 值
     * @return true / false
     */
    boolean set(Object key, Object value);

    boolean set(Object key, Object value, long expireTime);

    boolean set(Object key, Object value, long expireTime, TimeUnit timeUnit);

    /**
     * 获取key对应的value
     * @param key 键
     * @return 值 or null
     */
    Object get(String key);


    /**
     * 自增
     * @param key String
     * @param number long
     * @return long
     */
    long incr(String key, long number);

    /**
     * 递减
     * @param key String
     * @param number long
     * @return long
     */
    long decr(String key, long number);
}
