package com.ofs.sys;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author gaoly
 * @desc SYS服务启动类
 */
@SpringBootApplication
@EnableSwagger2
@MapperScan("com.ofs.sys.serv.mapper")
@ComponentScan(basePackages = {"com.ofs"})
public class SystemApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(SystemApiApplication.class);
    }
}
