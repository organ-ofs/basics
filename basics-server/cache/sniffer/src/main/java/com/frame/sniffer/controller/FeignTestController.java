package com.frame.sniffer.controller;

import com.frame.api.dto.FeignTestDto;
import com.frame.api.service.FeignTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("")
public class FeignTestController {

    @Autowired
    private FeignTestService feignTestService;

    @RequestMapping("/feign")
    public FeignTestDto getFeign() {

        return feignTestService.getFeign();
    }
}
