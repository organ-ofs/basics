package com.ofs.commons.plugin.quartz.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.ofs.commons.plugin.quartz.config.ConfigQuartz;
import com.ofs.commons.plugin.quartz.dto.JobTaskDto;
import com.ofs.commons.plugin.quartz.dto.JobTaskLogDto;
import com.ofs.commons.plugin.quartz.entity.JobTaskLog;
import com.ofs.commons.plugin.quartz.mapper.JobTaskLogMapper;
import com.ofs.commons.plugin.quartz.service.IJobTaskLogService;
import com.ofs.web.base.BaseServiceImpl;
import com.ofs.web.base.bean.ResultTable;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * job任务表日志表 服务实现类
 * </p>
 *
 * @author gaoly
 * @since 2018-04-23
 */
@Service
@ConditionalOnBean(ConfigQuartz.class)
public class JobTaskLogServiceImpl extends BaseServiceImpl<JobTaskLogMapper, JobTaskLog> implements IJobTaskLogService {
    /**
     * 分页查询
     *
     * @return 包含分面信息的查询结果
     */
    public ResultTable<JobTaskLogDto> listPage(Page<JobTaskLogDto> page, JobTaskLogDto log) {
        List<JobTaskLogDto> list = super.baseMapper.listPage(page, log);
        page.setRecords(list);
        return new ResultTable(page);
    }

    /**
     * Job日志删除
     *
     * @param jobTaskDto
     * @return
     */
    @Override
    public Boolean removeLog(JobTaskDto jobTaskDto) {
        return SqlHelper.retBool(super.baseMapper.deleteLog(jobTaskDto.getName(), jobTaskDto.getJobGroup()));
    }


}
