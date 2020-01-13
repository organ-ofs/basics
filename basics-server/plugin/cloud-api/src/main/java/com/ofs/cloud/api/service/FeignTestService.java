package com.ofs.cloud.api.service;

import com.ofs.cloud.api.dto.FeignTestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 测试用接口
 */
@FeignClient(value = "download")
public interface FeignTestService {

    @RequestMapping(value = "/feign")
    FeignTestDto getFeign();
}
