package com.ofs.commons.plugin.quartz.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ofs.commons.plugin.quartz.dto.JobTaskLogDto;
import com.ofs.commons.plugin.quartz.entity.JobTaskLog;
import com.ofs.web.base.IBaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * job任务表日志表 Mapper 接口
 * </p>
 *
 * @author gaoly
 * @since 2018-04-23
 */
public interface JobTaskLogMapper extends IBaseMapper<JobTaskLog> {

    /**
     * 分布查询系统日志信息
     *
     * @param page 分面信息
     * @return 查询结果
     */
    List<JobTaskLogDto> listPage(Page<JobTaskLogDto> page, @Param("condition") JobTaskLogDto jobTaskDto);


    /**
     * 删除job执行日志信息
     *
     * @param name
     * @param group
     * @return
     */
    @Delete("delete from ST_TBL_JOB_TASK_LOG   WHERE  JOB_NAME =  #{name} and  JOB_GROUP =  #{group}")
    int deleteLog(@Param("name") String name, @Param("group") String group);
}

