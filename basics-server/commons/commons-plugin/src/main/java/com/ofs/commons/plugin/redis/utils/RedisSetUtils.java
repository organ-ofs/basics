package com.ofs.commons.plugin.redis.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * @author gaoly
 */
@Component
public class RedisSetUtils {


    private static RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public void setRedisTemplate(RedisTemplate redisTemplate) {
        RedisSetUtils.redisTemplate = redisTemplate;
    }


    /**
     * @param key 键
     * @return
     * @description 根据key获取Set中的所有值
     */

    public Set<Object> sGetAll(String key) {
        try {
            return redisTemplate.opsForSet().members(key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @param key   键
     * @param value 值
     * @return true 存在 false不存在
     * @description 根据value从一个set中查询, 是否存在
     */

    public boolean sHasKey(String key, Object value) {
        try {
            return redisTemplate.opsForSet().isMember(key, value);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * @param key    键
     * @param values 值 可以是多个
     * @return 成功个数
     * @description 将数据放入set缓存
     */

    public long sSet(String key, Object... values) {
        try {
            return redisTemplate.opsForSet().add(key, values);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * @param key 键
     * @return `
     * @description 获取set缓存的长度
     */

    public long sGetSetSize(String key) {
        try {
            return redisTemplate.opsForSet().size(key);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * @param key    键
     * @param values 值 可以是多个
     * @return 移除的个数
     * @description 移除值为value的
     */

    public long sRemove(String key, Object... values) {
        try {
            Long count = redisTemplate.opsForSet().remove(key, values);
            return count;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

}
