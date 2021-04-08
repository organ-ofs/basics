package com.frame;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class DownloadApplication {

	public static void main(String[] args) {
		SpringApplication.run(DownloadApplication.class, args);
	}

}
