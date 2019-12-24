package com.ofs.sys.controller;

import com.ofs.sys.dto.SignInDto;
import com.ofs.sys.service.SysUserService;
import com.ofs.web.annotation.JwtClaim;
import com.ofs.web.annotation.SysLogs;
import com.ofs.web.bean.ResponseResult;
import com.ofs.web.bean.SystemCode;
import com.ofs.web.jwt.JwtToken;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    @PostMapping(value = {"/sign-in"})
    @ApiOperation(value = "登录")
    @SysLogs("登录")
    public ResponseResult signIn(@RequestBody @Validated @ApiParam(value = "登录数据", required = true) SignInDto sign) {
        redisTemplate.opsForValue().set("aaa", "aaa");
        System.out.println(redisTemplate.opsForValue().get("aaa"));
        userService.signIn(sign);
        return ResponseResult.e(SystemCode.SIGN_IN_OK, ((JwtToken) SecurityUtils.getSubject().getPrincipal()).getToken());
    }

    @PostMapping(value = "/current")
    @ApiOperation(value = "获取当前用户信息")
    @SysLogs("获取当前用户信息")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "身份认证Token")
    public ResponseResult current() {
        return ResponseResult.e(SystemCode.OK, userService.getCurrentUser());
    }

    @PostMapping(value = "/logout")
    @ApiOperation(value = "注销登录")
    @SysLogs("注销登录")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "身份认证Token")
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
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "身份认证Token")
    public ResponseResult<List<String>> getAllPermissionTag(@JwtClaim String t) {
        return ResponseResult.e(SystemCode.OK, userService.getAllPermissionTag());
    }

}
