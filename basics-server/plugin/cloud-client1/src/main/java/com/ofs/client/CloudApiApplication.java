package com.ofs.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;

@EnableEurekaClient
//@EnableFeignClients
@SpringBootApplication
@ComponentScan(basePackages = {"com.ofs"})
public class CloudApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(CloudApiApplication.class, args);
    }

}
