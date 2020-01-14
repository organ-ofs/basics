package com.ofs.commons.plugin.quartz.service;

import com.ofs.commons.plugin.quartz.dto.JobTaskDto;
import com.ofs.commons.plugin.quartz.entity.JobTaskLog;
import com.ofs.web.base.BaseService;

/**
 * <p>
 * job任务表日志表 服务类
 * </p>
 *
 * @author gaoly
 * @since 2018-04-23
 */
public interface IJobTaskLogService extends BaseService<JobTaskLog> {

    /**
     * 删除操作日志
     *
     * @param jobTaskDto
     * @return
     */
    Boolean removeLog(JobTaskDto jobTaskDto);

}
