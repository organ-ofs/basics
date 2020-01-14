package com.ofs.commons.plugin.quartz.dto;

import com.ofs.web.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * job任务表日志表
 * </p>
 *
 * @author gaoly
 * @since 2018-04-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class JobTaskLogDto extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * cron表达式
     */
    private String cronExpression;
    /**
     * 任务名称
     */
    private String jobName;
    /**
     * 任务分组
     */
    private String jobGroup;
    /**
     * 描述信息
     */
    private String remark;
    /**
     * 任务状态    1：成功    0：失败
     */
    private String execStatus;
    /**
     * 耗时(单位：毫秒)
     */
    private long execTimes;
    /**
     * 失败信息
     */
    private String error;
    /**
     * 任务id
     */
    private String jobId;
    /**
     * ip
     */
    private String ip;
}
