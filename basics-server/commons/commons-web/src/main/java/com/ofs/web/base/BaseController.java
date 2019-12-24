package com.ofs.web.base;


import com.baomidou.mybatisplus.plugins.Page;
import com.ofs.web.annotation.SysLogs;
import com.ofs.web.bean.ResponseResult;
import com.ofs.web.bean.SystemCode;
import com.ofs.web.utils.BeanConverterUtil;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author gaoly
 * @version 2019/5/25/11:36
 */
public abstract class BaseController<T extends BaseEntity> {
    public abstract BaseService getService();


    @PostMapping("/list")
    @ApiOperation(value = "分页获取所有列表")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "身份认证Token", required = true)
    ResponseResult<Page<T>> list(@RequestBody T dto) {
        return ResponseResult.e(SystemCode.OK, getService().list(new Page(), BeanConverterUtil.convert(dto, BaseEntity.class)));
    }


    @PostMapping("/update/{id}")
    @ApiOperation(value = "更新指定ID对象的信息")
    @SysLogs("更新指定ID对象的信息")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "身份认证Token", required = true)
    ResponseResult update(@PathVariable("id") String id, @RequestBody @Validated T dto) {

//        getService().update(id, BeanConverterUtil.convert(dto, BaseEntity.class));
        return ResponseResult.e(SystemCode.OK);
    }

    @PostMapping("/remove/{id}")
    @ApiOperation(value = "删除指定ID的对象")
    @SysLogs("删除指定ID的对象")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "身份认证Token", required = true)
    ResponseResult remove(@PathVariable("id") List<String> ids) {
//        getService().remove(ids);
        return ResponseResult.e(SystemCode.OK);
    }
}
