package com.ofs.commons.plugin.redis.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.concurrent.TimeUnit;

/**
 * @author gaoly
 */
@Component
public class RedisUtils {


    private static RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public void setRedisTemplate(RedisTemplate redisTemplate) {
        RedisUtils.redisTemplate = redisTemplate;
    }


    /**
     * @return
     * @description 事务开启
     */
    public static void multi() {
        redisTemplate.multi();
    }

    /**
     * @return
     * @description 事务提交
     */
    public static void exec() {
        redisTemplate.exec();
    }

    /**
     * @param key  键
     * @param time 时间(秒)
     * @return
     * @description 指定缓存失效时间
     */
    public static boolean expire(String key, long time) {
        try {
            if (time > 0) {
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    /**
     * @param key 键 不能为null
     * @return 时间(秒) 返回0代表为永久有效
     * @description 根据key 获取过期时间
     */
    public static long getExpire(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    /**
     * @param key 键
     * @return true 存在 false不存在
     * @description 判断key是否存在
     */
    public static boolean hasKey(String key) {
        try {
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * @param key 可以传一个值 或多个
     * @description 删除缓存
     */

    @SuppressWarnings("unchecked")
    public static void del(String... key) {
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                redisTemplate.delete(key[0]);
            } else {
                redisTemplate.delete(CollectionUtils.arrayToList(key));
            }
        }
    }


    /**
     * @param key   键
     * @param delta 要增加几(大于0)
     * @return
     * @description 递增
     */
    public long incr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递增因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(key, delta);

    }

    /**
     * @param key   键
     * @param delta 要减少几(小于0)
     * @return
     * @description 递减
     */
    public long decr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递减因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(key, -delta);
    }

}
