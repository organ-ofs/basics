package com.ofs.commons.plugin.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.*;
import java.util.concurrent.TimeUnit;

@Service
public class RedisUtil {
    private static Logger logger = LoggerFactory.getLogger(RedisUtil.class);
    //保存字符串类型数据
    @Resource(name = "redisTemplate")
    private RedisTemplate<String, String> redisTemplate;

    //存储字符串
    public void setKey(String key, String value, int cacheSeconds) {
        redisTemplate.opsForValue().set(key, value, cacheSeconds, TimeUnit.SECONDS);
    }

    //获取字符串
    public String getValue(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    //序列化
    public byte[] serialize(Object source) {
        ByteArrayOutputStream byteOut = null;
        ObjectOutputStream ObjOut = null;
        try {
            byteOut = new ByteArrayOutputStream();
            ObjOut = new ObjectOutputStream(byteOut);
            ObjOut.writeObject(source);
            ObjOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != ObjOut) {
                    ObjOut.close();
                }
            } catch (IOException e) {
                ObjOut = null;
            }
        }
        return byteOut.toByteArray();
    }

    //反序列化
    public Object deserialize(byte[] source) {
        ObjectInputStream ObjIn = null;
        Object retVal = null;

        try {
            ByteArrayInputStream byteIn = new ByteArrayInputStream(source);
            ObjIn = new ObjectInputStream(byteIn);
            retVal = ObjIn.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != ObjIn) {
                    ObjIn.close();
                }
            } catch (IOException e) {
                ObjIn = null;
            }
        }
        return retVal;
    }
}
