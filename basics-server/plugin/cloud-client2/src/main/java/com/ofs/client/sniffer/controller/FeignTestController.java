package com.ofs.client.sniffer.controller;

import com.ofs.cloud.api.dto.FeignTestDto;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/abc")
public class FeignTestController {
    //
//    @Autowired
//    private FeignTestService feignTestService;
//
    @RequestMapping("/feign")
    public FeignTestDto getFeign() {

//        return feignTestService.getFeign();
        return new FeignTestDto();
    }

}
