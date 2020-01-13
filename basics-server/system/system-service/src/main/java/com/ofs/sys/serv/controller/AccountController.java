package com.ofs.sys.serv.controller;

import com.ofs.sys.serv.dto.SignInDto;
import com.ofs.sys.serv.service.SysUserService;
import com.ofs.web.annotation.JwtClaim;
import com.ofs.web.annotation.SysLogs;
import com.ofs.web.base.bean.ResponseResult;
import com.ofs.web.base.bean.SystemCode;
import com.ofs.web.jwt.JwtToken;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author gaoly
 * @version 2019/4/13/14:02
 */
@RestController
@RequestMapping(value = {"/account"})
@Api(tags = {"账户相关"})
public class AccountController {

    @Autowired
    private SysUserService userService;

    @Autowired
    private RedisTemplate redisTemplate;

    @PostMapping("/sign-in")
    @ApiOperation(value = "登录")
    @SysLogs("登录")
    public ResponseResult signIn(@Validated @ApiParam(value = "登录数据", required = true) SignInDto sign) {
        userService.signIn(sign);
        return ResponseResult.success(((JwtToken) SecurityUtils.getSubject().getPrincipal()).getToken());
    }

    @PostMapping(value = "/current")
    @ApiOperation(value = "获取当前用户信息")
    @SysLogs("获取当前用户信息")
    public ResponseResult current() {
        return ResponseResult.success(userService.getCurrentUser());
    }

    @PostMapping(value = "/logout")
    @ApiOperation(value = "注销登录")
    @SysLogs("注销登录")
    public ResponseResult logout() {
        try {
            Subject subject = SecurityUtils.getSubject();
            subject.logout();
        } catch (Exception e) {
            return ResponseResult.e(SystemCode.LOGOUT_FAIL);
        }
        return ResponseResult.e(SystemCode.LOGOUT_OK);
    }

    @PostMapping(value = "/all-permission-tag")
    @ApiOperation(value = "获取所有的权限标示")
    public ResponseResult<List<String>> getAllPermissionTag(@JwtClaim String t) {
        return ResponseResult.success(userService.getAllPermissionTag());
    }

}
