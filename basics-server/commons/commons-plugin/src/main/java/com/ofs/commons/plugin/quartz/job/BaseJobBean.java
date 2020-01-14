package com.ofs.commons.plugin.quartz.job;

import com.ofs.commons.plugin.quartz.entity.JobTaskLog;
import com.ofs.commons.plugin.quartz.service.IJobTaskLogService;
import com.ofs.web.knowledge.DataDictKnowledge;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.InetAddress;

/**
 * @author gaoly
 */
@Slf4j
public abstract class BaseJobBean implements Job {

    @Autowired
    BeanFactory beanFactory;

    /**
     * JOB 的执行方法
     *
     * @param context
     * @throws JobExecutionException
     */
    @Override
    public final void execute(JobExecutionContext context) throws JobExecutionException {
        JobDetail jobDetail = context.getJobDetail();
        JobTaskLog jobTaskLog = new JobTaskLog();
        jobTaskLog.setJobName(jobDetail.getKey().getName());
        jobTaskLog.setJobGroup(jobDetail.getKey().getGroup());

        try {
            InetAddress addr = InetAddress.getLocalHost();
            //获取本机ip
            jobTaskLog.setIp(StringUtils.trimToEmpty(addr.getHostAddress()));
            // 开始时间
            long startTimeMillis = System.currentTimeMillis();
            this.executeInternal(context);

            // 结束时间
            long endTimeMillis = System.currentTimeMillis();
            jobTaskLog.setExecTimes(endTimeMillis - startTimeMillis);
            jobTaskLog.setExecStatus(DataDictKnowledge.YesNoEnum.YES.getCode());

        } catch (Exception e) {
            String errMsg = e.getMessage();
            jobTaskLog.setExecStatus(DataDictKnowledge.YesNoEnum.NO.getCode());

            jobTaskLog.setError(errMsg);
            throw new JobExecutionException(e);
        } finally {
            try {
                IJobTaskLogService jobTaskLogService = beanFactory.getBean(IJobTaskLogService.class);
                jobTaskLogService.save(jobTaskLog);
            } catch (Exception e) {
            } catch (Throwable e) {
            }
        }

    }


    /**
     * 各具体业务去实现
     *
     * @param var1
     * @throws JobExecutionException
     */
    protected abstract void executeInternal(JobExecutionContext var1) throws JobExecutionException;


}
