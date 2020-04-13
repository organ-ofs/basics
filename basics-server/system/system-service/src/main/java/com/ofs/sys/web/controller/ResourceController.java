package com.ofs.sys.web.controller;

import com.ofs.sys.web.entity.SysResource;
import com.ofs.sys.web.service.SysResourceService;
import com.ofs.web.base.BaseController;
import com.ofs.web.base.BaseService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author gaoly
 * 菜单功能页面交互类
 */
@RestController
@Api(value = "资源", tags = {"资源服务接口"})
@RequestMapping("/system/resource")
public class ResourceController extends BaseController<SysResource> {

    @Autowired
    private SysResourceService service;

    @Override
    public BaseService<SysResource> getService() {
        return service;
    }

}

