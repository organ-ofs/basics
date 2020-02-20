package com.ofs.web.base;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ofs.utils.IdentifierUtils;
import com.ofs.web.annotation.SysLogs;
import com.ofs.web.base.bean.ResponseResult;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author gaoly
 * @version 2019/5/25/11:36
 */
public abstract class BaseController<T extends BaseEntity> {

    @InitBinder
    public void initBinder(ServletRequestDataBinder binder) {

        //自动转换日期类型的字段格式
        binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"), true));

    }

    /**
     * getService
     *
     * @return
     */
    public abstract BaseService getService();


    @PostMapping("/list")
    @ApiOperation(value = "分页")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "身份认证Token", required = false)
    ResponseResult<IPage<T>> listPage(@RequestBody T dto, @RequestParam long size, @RequestParam long current) {
        return ResponseResult.success(getService().listPage(new Page(current, size), dto));
    }


    @PostMapping("/edit")
    @ApiOperation(value = "更新根据ID")
    @SysLogs("更新根据ID")
    ResponseResult edit(@RequestBody @Validated T dto) throws Exception {
        getService().update(dto);
        return ResponseResult.success();
    }

    @PostMapping("/remove")
    @ApiOperation(value = "删除根据ID")
    @SysLogs("删除根据ID")
    ResponseResult remove(@RequestParam("id") String id) throws Exception {
        getService().remove(id);
        return ResponseResult.success();
    }

    @PostMapping("/get")
    @ApiOperation(value = "查询根据ID")
    @SysLogs("查询根据ID")
    ResponseResult get(@RequestParam("id") @ApiParam(value = "ID") String id) {
        T data = (T) getService().getById(id);
        return ResponseResult.success(data);
    }

    @PostMapping("/add")
    @ApiOperation(value = "新增")
    @SysLogs("新增")
    ResponseResult add(@RequestBody @Validated T dto) throws Exception {
        dto.setId(IdentifierUtils.nextUuid());
        getService().add(dto);
        return ResponseResult.success();
    }
}
