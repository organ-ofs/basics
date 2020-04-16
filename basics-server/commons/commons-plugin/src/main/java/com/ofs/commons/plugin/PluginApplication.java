package com.ofs.commons.plugin;

import com.ofs.web.filter.MyTypeFilter;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

/**
 * @author gaoly
 * @desc 服务启动类
 */
@SpringBootApplication
@MapperScan("com.ofs.commons.plugin.quartz.mapper")
@ComponentScan(basePackages = {"com.ofs"}, excludeFilters = @ComponentScan.Filter(type = FilterType.CUSTOM, classes = {MyTypeFilter.class}))
public class PluginApplication {
    public static void main(String[] args) {
        SpringApplication.run(PluginApplication.class);
    }
}
