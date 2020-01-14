package com.ofs.commons.plugin.redis.test;

import com.ofs.commons.plugin.redis.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class Test {

    @RequestMapping(value = "/add")
    public String add() {
        RedisUtils.setString("add1", "val333");
        return RedisUtils.getString("add1");
    }
}
