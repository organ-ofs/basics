package com.ofs.commons.plugin.quartz.service;

import com.ofs.commons.plugin.quartz.entity.JobTask;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Trigger;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author ly
 * @since 2019-05-08
 */
@Service
public interface IJobService {

    /**
     * @param id
     * @return
     */
    JobTask getById(String id);

    /**
     * 从数据库中加载获取到所有Job
     *
     * @return
     */
    List<JobTask> loadJobs();


    /**
     * 获取JobDetail,JobDetail里会引用一个Job Class来定义
     *
     * @param jobKey
     * @param description
     * @param map
     * @return
     */
    JobDetail getJobDetail(JobKey jobKey, String description, JobDataMap map);

    /**
     * 获取Trigger (Job的触发器,执行规则)
     *
     * @param job
     * @return
     */
    Trigger getTrigger(JobTask job);

    /**
     * 获取JobKey,包含Name和Group
     *
     * @param job
     * @return
     */
    JobKey getJobKey(JobTask job);

    /**
     * @param job
     * @return
     */
    int save(JobTask job);
}
