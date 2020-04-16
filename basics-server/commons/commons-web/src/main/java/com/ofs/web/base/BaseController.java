package com.ofs.web.base;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ofs.utils.DateUtils;
import com.ofs.utils.IdentifierUtils;
import com.ofs.web.annotation.SysLogs;
import com.ofs.web.base.bean.RequestTable;
import com.ofs.web.base.bean.Result;
import com.ofs.web.base.bean.ResultTable;
import com.ofs.web.jwt.JwtToken;
import com.ofs.web.utils.WebTools;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author gaoly
 * @version 2019/5/25/11:36
 */
public abstract class BaseController<T extends BaseEntity> {

    /**
     * //相关参数注解 @RequestParam("id") @ApiParam(value = "ID") @RequestBody
     *
     * @param binder
     */

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
    Result<IPage<T>> listPage(@RequestBody RequestTable<T> request) {
        return ResultTable.result(getService().listPage(new Page(request.getCurrent(), request.getSize()), request.getData()));
    }


    @PostMapping("/edit")
    @ApiOperation(value = "更新根据ID")
    @SysLogs("更新根据ID")
    Result edit(@RequestBody @Validated T dto) throws Exception {
        dto.setUpdateDate(DateUtils.getCurrentTime());
        getService().update(dto);
        return Result.result();
    }

    @PostMapping("/remove")
    @ApiOperation(value = "删除根据ID")
    @SysLogs("删除根据ID")
    Result remove(String id) throws Exception {
        getService().remove(id);
        return Result.result();
    }

    @PostMapping("/get")
    @ApiOperation(value = "查询根据ID")
    @SysLogs("查询根据ID")
    Result get(String id) {
        T data = (T) getService().getById(id);
        return Result.result(data);
    }

    @PostMapping("/add")
    @ApiOperation(value = "新增")
    @SysLogs("新增")
    Result add(@RequestBody @Validated T dto) throws Exception {
        dto.setId(IdentifierUtils.nextUuid());
        dto.setCreateDate(DateUtils.getCurrentTime());
        getService().add(dto);
        return Result.result();
    }

    public String getAccount() {
        JwtToken token = WebTools.getJwtToken();
        return token.getAccount();
    }
}
