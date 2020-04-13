package com.ofs.sys.web.controller;

import com.ofs.sys.web.entity.SysMenus;
import com.ofs.sys.web.service.SysMenusService;
import com.ofs.web.base.BaseController;
import com.ofs.web.base.BaseService;
import com.ofs.web.base.bean.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;


/**
 * @author gaoly
 * @version 2019/4/22
 */
@RestController
@RequestMapping(value = "/system/menu")
@Api(value = "菜单管理", tags = {"菜单服务接口"})
public class MenuController extends BaseController<SysMenus> {

    @Autowired
    private SysMenusService service;

    @Override
    public BaseService<SysMenus> getService() {
        return service;
    }


    @ApiOperation(value = "获取所有资源树 - 权限配置可用", httpMethod = "POST", produces = "application/json")
    @RequestMapping(value = "/getTree", method = RequestMethod.POST)
    public Result getTree(SysMenus menu, HttpServletResponse response) {
        List<SysMenus> data = service.getTree(menu);
        return Result.result(data);
    }

    @ApiOperation(value = "根据角色获取资源树-菜单可用", httpMethod = "POST", produces = "application/json")
    @RequestMapping(value = "/getTreeByRole", method = RequestMethod.POST)
    public Result getTreeByRole(String roleId, HttpServletResponse response) {
        List<SysMenus> data = service.getTreeByRole(roleId);
        return Result.result(data);
    }


}
