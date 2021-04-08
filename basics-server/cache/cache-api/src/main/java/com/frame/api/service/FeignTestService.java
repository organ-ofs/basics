package com.frame.api.service;

import com.frame.api.dto.FeignTestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 测试用接口
 */
@FeignClient(value = "download-api")
public interface FeignTestService {

    @RequestMapping("/feign")
    FeignTestDto getFeign();
}
