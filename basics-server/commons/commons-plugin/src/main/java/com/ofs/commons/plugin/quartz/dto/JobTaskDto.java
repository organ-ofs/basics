package com.ofs.commons.plugin.quartz.dto;

import com.ofs.web.base.BaseEntity;
import com.ofs.web.base.validation.group.UpdateGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class JobTaskDto extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @NotNull(message = "the job id cannot be null", groups = {UpdateGroup.class})
    private String name;
    //job名称

    private String jobGroup;
    //job组名

    @NotNull(message = "the job id cannot be null", groups = {UpdateGroup.class})
    private String cron;

    private String parameter;

    private String description;

    private String vmParam;
    //vm参数

    private String jarPath;
    //job的jar路径

    private String status;
    //job的执行状态,这里我设置为OPEN/CLOSE且只有该值为OPEN才会执行该Job
}
