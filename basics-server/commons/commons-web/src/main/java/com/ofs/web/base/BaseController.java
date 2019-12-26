package com.ofs.web.base;


import com.baomidou.mybatisplus.plugins.Page;
import com.ofs.web.annotation.SysLogs;
import com.ofs.web.bean.ResponseResult;
import com.ofs.web.utils.BeanConverterUtil;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author gaoly
 * @version 2019/5/25/11:36
 */
public abstract class BaseController<T extends BaseEntity> {
    public abstract BaseService getService();


    @PostMapping("/list")
    @ApiOperation(value = "分页")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "身份认证Token", required = true)
    ResponseResult<Page<T>> list(@RequestBody T dto) {
        return ResponseResult.success(getService().list(new Page(), BeanConverterUtil.convert(dto, BaseEntity.class)));
    }


    @PostMapping("/update")
    @ApiOperation(value = "更新根据ID")
    @SysLogs("更新根据ID")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "身份认证Token", required = true)
    ResponseResult update(@RequestBody @Validated T dto) {
        getService().update(dto);
        return ResponseResult.success();
    }

    @PostMapping("/remove/{id}")
    @ApiOperation(value = "删除根据ID")
    @SysLogs("删除根据ID")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "身份认证Token", required = true)
    ResponseResult remove(@PathVariable("id") String id) {
        getService().remove(id);
        return ResponseResult.success();
    }

    @PostMapping("/get/{id}")
    @ApiOperation(value = "查询根据ID")
    @SysLogs("查询根据ID")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "身份认证Token", required = true)
    ResponseResult get(@PathVariable("id") @ApiParam(value = "ID") String id) {
        getService().selectById(id);
        return ResponseResult.success();
    }

    @PostMapping("/add")
    @ApiOperation(value = "新增")
    @SysLogs("新增")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "身份认证Token", required = true)
    ResponseResult add(@RequestBody @Validated T dto) {
        getService().insert(dto);
        return ResponseResult.success();
    }
}
