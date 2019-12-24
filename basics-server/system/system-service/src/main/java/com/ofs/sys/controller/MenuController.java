package com.ofs.sys.controller;

import com.ofs.sys.entity.SysMenus;
import com.ofs.sys.service.SysMenusService;
import com.ofs.web.base.BaseController;
import com.ofs.web.base.BaseService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author gaoly.cn
 * @version 2019/4/22
 */
@RestController
@RequestMapping(value = "/system/resource")
@Api(tags = {"资源管理"})
public class MenuController extends BaseController<SysMenus> {

    @Autowired
    private SysMenusService menusService;

    @Override
    public BaseService<SysMenus> getService() {
        return menusService;
    }


}
