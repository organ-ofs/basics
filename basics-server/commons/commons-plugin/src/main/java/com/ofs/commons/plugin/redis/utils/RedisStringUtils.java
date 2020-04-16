package com.ofs.commons.plugin.redis.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * @author gaoly
 */
@Component
public class RedisStringUtils {


    private static RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public void setRedisTemplate(RedisTemplate redisTemplate) {
        RedisStringUtils.redisTemplate = redisTemplate;
    }


    public static String getString(String key) {
        return key == null ? null : redisTemplate.opsForValue().get(key).toString();
    }

    public static Boolean setString(String key, String value) {
        try {
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;

        }
    }
}
