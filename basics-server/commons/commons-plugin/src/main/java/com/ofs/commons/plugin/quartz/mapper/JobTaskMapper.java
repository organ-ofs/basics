package com.ofs.commons.plugin.quartz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ofs.commons.plugin.quartz.dto.JobTaskDto;
import com.ofs.commons.plugin.quartz.entity.JobTask;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by EalenXie on 2018/6/4 14:27
 */
public interface JobTaskMapper extends BaseMapper<JobTask> {
    /**
     * @param page
     * @return
     */
    List<JobTaskDto> selectJobTask(Page<JobTaskDto> page, @Param("condition") JobTaskDto jobTaskDto);

}
