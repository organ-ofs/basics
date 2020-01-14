package com.ofs.commons.plugin.quartz.entity;

import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
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
@TableName("ST_TBL_JOB_TASK_LOG")
@KeySequence(value = "ST_SEQ_JOB_TASK_LOG", clazz = String.class)
public class JobTaskLog extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * cron表达式
     */
    @TableField("CRON_EXPRESSION")
    private String cronExpression;
    /**
     * 任务名称
     */
    @TableField("JOB_NAME")
    private String jobName;
    /**
     * 任务分组
     */
    @TableField("JOB_GROUP")
    private String jobGroup;
    /**
     * 描述信息
     */
    @TableField("REMARK")
    private String remark;
    /**
     * 任务状态    1：成功    0：失败
     */
    @TableField("EXEC_STATUS")
    private String execStatus;
    /**
     * 耗时(单位：毫秒)
     */
    @TableField("EXEC_TIMES")
    private Long execTimes;
    /**
     * 失败信息
     */
    @TableField("ERROR")
    private String error;
    /**
     * 任务id
     */
    @TableField("JOB_ID")
    private String jobId;
    /**
     * ip
     */
    @TableField("IP")
    private String ip;


    /**
     * cron表达式
     */
    public static final String CRON_EXPRESSION = "CRON_EXPRESSION";

    /**
     * 任务名称
     */
    public static final String JOB_NAME = "JOB_NAME";

    /**
     * 任务分组
     */
    public static final String JOB_GROUP = "JOB_GROUP";

    /**
     * 描述信息
     */
    public static final String REMARK = "REMARK";

    /**
     * 任务状态    1：成功    0：失败
     */
    public static final String EXEC_STATUS = "EXEC_STATUS";

    /**
     * 耗时(单位：毫秒)
     */
    public static final String EXEC_TIMES = "EXEC_TIMES";

    /**
     * 失败信息
     */
    public static final String ERROR = "ERROR";

    /**
     * 任务id
     */
    public static final String JOB_ID = "JOB_ID";

    /**
     * IP
     */
    public static final String IP = "IP";


}
