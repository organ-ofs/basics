package com.ofs.sys.web.controller;

import com.ofs.sys.web.dto.ResetPasswordDto;
import com.ofs.sys.web.entity.SysUser;
import com.ofs.sys.web.service.SysUserService;
import com.ofs.web.annotation.SysLogs;
import com.ofs.web.base.BaseController;
import com.ofs.web.base.BaseService;
import com.ofs.web.base.bean.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author gaoly
 * @version 2019/4/18/14:15
 */
@RestController
@RequestMapping(value = {"/system/user"})
@Api(tags = {"用户管理"})
public class UserController extends BaseController<SysUser> {

    @Autowired
    private SysUserService service;

    @Override
    public BaseService getService() {
        return service;
    }


    @PostMapping("/reset/password")
    @ApiOperation(value = "重置密码")
    @SysLogs("重置密码")
    public Result resetPassword(@RequestBody @Validated ResetPasswordDto dto) {
        service.resetPassword(dto);
        return Result.result();
    }
}
