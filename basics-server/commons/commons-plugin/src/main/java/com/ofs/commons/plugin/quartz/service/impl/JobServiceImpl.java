package com.ofs.commons.plugin.quartz.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ofs.commons.plugin.quartz.entity.JobTask;
import com.ofs.commons.plugin.quartz.job.DynamicJobTest;
import com.ofs.commons.plugin.quartz.mapper.JobTaskMapper;
import com.ofs.commons.plugin.quartz.service.IJobService;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by EalenXie on 2018/6/4 14:25
 */
@Service
public class JobServiceImpl implements IJobService {

    @Autowired
    private JobTaskMapper jobTaskMapper;

    //通过Id获取Job
    @Override
    public JobTask getById(String id) {
        return jobTaskMapper.selectById(id);
    }

    @Override
    public List<JobTask> loadJobs() {
        List<JobTask> list = new ArrayList<>();
        QueryWrapper<JobTask> queryWrapper = new QueryWrapper<>();
        jobTaskMapper.selectList(queryWrapper).forEach(list::add);
        return list;
    }

    //获取JobDataMap.(Job参数对象)
    public JobDataMap getJobDataMap(JobTask job) {
        JobDataMap map = new JobDataMap();
        map.put("name", job.getName());
        map.put("jobGroup", job.getJobGroup());
        map.put("cronExpression", job.getCron());
        map.put("parameter", job.getParameter());
        map.put("jobDescription", job.getDescription());
        map.put("vmParam", job.getVmParam());
        map.put("jarPath", job.getJarPath());
        map.put("status", job.getStatus());
        return map;
    }

    @Override
    public JobDetail getJobDetail(JobKey jobKey, String description, JobDataMap map) {
        return JobBuilder.newJob(DynamicJobTest.class)
                .withIdentity(jobKey)
                .withDescription(description)
                .setJobData(map)
                .storeDurably()
                .build();
    }

    @Override
    public Trigger getTrigger(JobTask job) {
        return TriggerBuilder.newTrigger()
                .withIdentity(job.getName(), job.getJobGroup())
                .withSchedule(CronScheduleBuilder.cronSchedule(job.getCron()))
                .build();
    }

    @Override
    public JobKey getJobKey(JobTask job) {
        return JobKey.jobKey(job.getName(), job.getJobGroup());
    }

    @Override
    public int save(JobTask job) {
        return jobTaskMapper.insert(job);
    }
}
