package com.ofs.client.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CustomerService {

    @Autowired
    RestTemplate restTemplate;


    public String hiService(String name) {
        return restTemplate.getForObject("http://DOWNLOAD/hi?name=" + name, String.class);
    }
}
