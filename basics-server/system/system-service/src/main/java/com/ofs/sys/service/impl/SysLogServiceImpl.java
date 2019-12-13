package com.ofs.sys.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.ofs.sys.entity.SysLog;
import com.ofs.sys.service.SysLogService;
import com.ofs.web.base.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author Licoy
 * @version 2018/4/28/9:57
 */
@Service
public class SysLogServiceImpl extends BaseServiceImpl<SysLog> implements SysLogService {

    @Override
    public Page<SysLog> list(Page<SysLog> page, SysLog sysLog) {
        EntityWrapper<SysLog> wrapper = new EntityWrapper<>();
        wrapper.orderBy(true, "create_date");
        return this.selectPage(page, wrapper);
    }

}
