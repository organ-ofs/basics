package com.ofs.web.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author gaoly
 * @version 2019/4/13/14:02
 */
@RestController
@RequestMapping(value = {"/swagger"})
@Api(tags = {"账户相关"})
public class AccountController {


    @PostMapping("/test")
    @ApiOperation(value = "登录")
    public String signIn() {

        return "test";
    }


}
