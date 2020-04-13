package com.ofs.sys.web.controller;

import com.ofs.sys.web.dto.SignInDto;
import com.ofs.sys.web.service.SysUserService;
import com.ofs.web.annotation.SysLogs;
import com.ofs.web.base.bean.Result;
import com.ofs.web.base.bean.SystemCode;
import com.ofs.web.jwt.JwtToken;
import com.ofs.web.knowledge.AuthMessageEnum;
import com.ofs.web.utils.WebTools;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author gaoly
 * @version 2019/4/13/14:02
 */
@Slf4j
@RestController
@RequestMapping("")
@Api(tags = {"账户相关"})
public class AccountController {

    @Autowired
    private SysUserService userService;

    @Autowired
    private RedisTemplate redisTemplate;

    @PostMapping("/login")
    @ApiOperation(value = "登录")
    @SysLogs("登录")
    public Result signIn(@Validated @ApiParam(value = "登录", required = true) SignInDto sign, HttpServletRequest request) {
        Subject currentUser = SecurityUtils.getSubject();
        String ip = WebTools.getClientIp(request);
        String terminal = "";
        JwtToken token = new JwtToken("", sign.getAccount(), sign.getPassword(), ip, terminal, null);
        try {
            currentUser.login(token);
        } catch (Exception e) {
            log.error("登陆失败", e);
        }
        //验证是否登录成功
        if (currentUser.isAuthenticated()) {
            log.info("用户[" + sign.getAccount() + "]登录认证通过");

            return Result.result(((JwtToken) currentUser.getPrincipal()).getToken());
        } else {
            return Result.error(AuthMessageEnum.FORBIDDEN_ACCOUNT_ERROR);
        }
    }

    @PostMapping(value = "/system/current")
    @ApiOperation(value = "获取当前用户信息")
    @SysLogs("获取当前用户信息")
    public Result current() {
        return Result.result(userService.getCurrentUser());
    }

    @PostMapping(value = "/logout")
    @ApiOperation(value = "注销登录")
    @SysLogs("注销登录")
    public Result logout() {
        try {
            Subject subject = SecurityUtils.getSubject();
            subject.logout();
        } catch (Exception e) {
            return Result.error(SystemCode.LOGOUT_FAIL);
        }
        return Result.result();
    }

    @PostMapping(value = "/system/permissions")
    @ApiOperation(value = "获取所有的权限标示")
    public Result<List<String>> getAllPermissionTag() {
        JwtToken token = WebTools.getJwtToken();
        return Result.result(userService.getAllPermissionTag(token.getAccount()));
    }

}
