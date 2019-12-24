package com.ofs.sys.controller;

import com.ofs.sys.dto.ResetPasswordDTO;
import com.ofs.sys.entity.SysUser;
import com.ofs.sys.service.SysUserService;
import com.ofs.web.annotation.SysLogs;
import com.ofs.web.base.BaseController;
import com.ofs.web.base.BaseService;
import com.ofs.web.bean.ResponseResult;
import com.ofs.web.bean.SystemCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


/**
 * @author gaoly
 * @version 2019/4/18/14:15
 */
@RestController
@RequestMapping(value = {"/system/user"})
@Api(tags = {"用户管理"})
public class UserController extends BaseController<SysUser> {

    @Autowired
    private SysUserService userService;

    @Override
    public BaseService getService() {
        return userService;
    }

    @PostMapping(value = {"/get/id/{id}"})
    @ApiOperation(value = "根据ID获取用户信息")
    @SysLogs("根据ID获取用户信息")
    public ResponseResult getById(@PathVariable("id") @ApiParam(value = "用户ID") String id) {
        return ResponseResult.e(SystemCode.OK, userService.findUserById(id, true));
    }

    @PostMapping(value = {"/reset-password"})
    @ApiOperation(value = "重置密码")
    @SysLogs("重置密码")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "身份认证Token")
    public ResponseResult resetPassword(@RequestBody
                                        @Validated @ApiParam(value = "用户及密码数据") ResetPasswordDTO dto) {
        userService.resetPassword(dto);
        return ResponseResult.e(SystemCode.OK);
    }
}
