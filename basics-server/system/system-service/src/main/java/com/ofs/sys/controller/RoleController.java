package com.ofs.sys.controller;

import com.ofs.sys.entity.SysRole;
import com.ofs.sys.service.SysRoleService;
import com.ofs.web.base.BaseController;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author gaoly
 * @version 2019/4/19/9:41
 */
@RestController
@RequestMapping(value = {"/system/role"})
@Api(tags = {"角色管理"})
public class RoleController extends BaseController<SysRole> {

    @Autowired
    private SysRoleService service;

    @Override
    public SysRoleService getService() {
        return service;
    }
}
