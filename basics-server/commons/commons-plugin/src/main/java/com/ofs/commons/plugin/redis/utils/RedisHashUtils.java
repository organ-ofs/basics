package com.ofs.commons.plugin.redis.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author gaoly
 */
@Component
public class RedisHashUtils {


    private static RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public void setRedisTemplate(RedisTemplate redisTemplate) {
        RedisHashUtils.redisTemplate = redisTemplate;
    }


    /**
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return 值
     * @description HashGet
     */

    public Object hashGet(String key, String item) {
        // redis命令：mget key field
        return redisTemplate.opsForHash().get(key, item);

    }

    /**
     * @param key 键
     * @return 对应的多个键值
     * @description 获取hashKey对应的所有键值
     */

    public Map<Object, Object> hashGetAll(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * @param key 键
     * @param map 对应多个键值
     * @return true 成功 false 失败
     * @description HashSet
     */

    public boolean hashMSet(String key, Map<String, Object> map) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * @param key   键
     * @param item  项
     * @param value 值
     * @return true 成功 false失败
     * @description 向一张hash表中放入数据, 如果不存在将创建
     */

    public boolean hashSet(String key, String item, Object value) {
        try {
            redisTemplate.opsForHash().put(key, item, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * @param key  键 不能为null
     * @param item 项 可以使多个 不能为null
     * @description 删除hash表中的值
     */

    public void hashDel(String key, Object... item) {
        redisTemplate.opsForHash().delete(key, item);
    }

    /**
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return true 存在 false不存在
     * @description 判断hash表中是否有该项的值
     */

    public boolean hashHasKey(String key, String item) {
        return redisTemplate.opsForHash().hasKey(key, item);
    }
}
