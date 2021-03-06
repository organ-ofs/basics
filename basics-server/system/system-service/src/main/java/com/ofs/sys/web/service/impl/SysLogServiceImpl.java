package com.ofs.sys.web.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ofs.sys.web.entity.SysLog;
import com.ofs.sys.web.mapper.SysLogMapper;
import com.ofs.sys.web.service.SysLogService;
import com.ofs.web.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author gaoly
 * @version 2019/4/28/9:57
 */
@Service
public class SysLogServiceImpl extends BaseServiceImpl<SysLogMapper, SysLog> implements SysLogService {

    @Override
    public IPage<SysLog> listPage(Page<SysLog> page, SysLog sysLog) {
        return super.listPage(page, sysLog);
    }

}
