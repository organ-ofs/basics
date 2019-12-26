package com.ofs.sys.controller;

import com.ofs.sys.entity.SysLog;
import com.ofs.sys.service.SysLogService;
import com.ofs.web.base.BaseController;
import com.ofs.web.base.BaseService;
import com.ofs.web.bean.ResponseResult;
import com.ofs.web.bean.SystemCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author gaoly
 * @version 2019/4/28/10:22
 */
@RestController
@RequestMapping(value = "/system/log")
@Api(tags = {"日志管理"})
public class LogController extends BaseController<SysLog> {

    @Autowired
    private SysLogService service;

    @Override
    public BaseService<SysLog> getService() {
        return service;
    }

    @PostMapping("/remove")
    @ApiOperation("批量删除")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "身份认证Token")
    public ResponseResult removeList(@RequestBody @ApiParam("ID集合") List<String> logList) {
        service.removes(logList);
        return ResponseResult.e(SystemCode.OK);
    }

}
