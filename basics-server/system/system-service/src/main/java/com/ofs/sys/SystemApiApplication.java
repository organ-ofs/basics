package com.ofs.sys;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author gaoly
 * @desc SYS服务启动类
 */
@SpringBootApplication
@EnableSwagger2
@EnableCaching
@MapperScan("com.ofs.sys.web.mapper")
@ComponentScan(basePackages = {"com.ofs"})
public class SystemApiApplication {
    public static void main(String[] args) {
        try {
            SpringApplication.run(SystemApiApplication.class, args);
            System.out.println("Server startup done.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
