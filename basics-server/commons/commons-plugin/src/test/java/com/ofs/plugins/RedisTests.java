package com.ofs.plugins;

import com.ofs.commons.plugin.PluginApplication;
import com.ofs.commons.plugin.redis.utils.RedisStringUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {PluginApplication.class})
@Slf4j
public class RedisTests {

    @Test
    public void redisTest() {
        RedisStringUtils.setString("add1", "val333");
        System.out.println(RedisStringUtils.getString("add1"));
        RedisStringUtils.getString("add1");
    }

}
