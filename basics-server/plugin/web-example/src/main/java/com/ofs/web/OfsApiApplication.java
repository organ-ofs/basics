package com.ofs.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author gaoly
 * @desc SYS服务启动类
 */
@SpringBootApplication
@EnableSwagger2
@EnableZuulProxy
@EnableEurekaClient
@EnableEurekaServer
public class OfsApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(OfsApiApplication.class);
    }
}
