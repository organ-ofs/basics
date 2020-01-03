package com.ofs.sys.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ofs.sys.entity.SysLog;
import com.ofs.sys.mapper.SysLogMapper;
import com.ofs.sys.service.SysLogService;
import com.ofs.web.base.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author gaoly
 * @version 2019/4/28/9:57
 */
@Service
public class SysLogServiceImpl extends BaseServiceImpl<SysLogMapper, SysLog> implements SysLogService {

    @Override
    public Page<SysLog> list(Page<SysLog> page, SysLog sysLog) {

        return super.list(page, sysLog);
    }

}
