package com.ofs.commons.plugin.quartz.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;
import org.quartz.Trigger;

import java.text.MessageFormat;

/**
 * JobListener监听器
 *
 * @author: gaoly
 * @date:
 */
@Slf4j
@Data
public class FrameJobListener implements JobListener {


    private String jobToBeFiredMessage = "Job {1}.{0} fired (by trigger {4}.{3}) at: {2, date, MM/dd/yyyy HH:mm:ss }";

    private String jobSuccessMessage = "Job {1}.{0} execution complete at {2, date, MM/dd/yyyy HH:mm:ss } and reports: {8}";

    private String jobFailedMessage = "Job {1}.{0} execution failed at {2, date, MM/dd/yyyy HH:mm:ss } and reports: {8}";

    /**
     * <p>
     * 返回当前监听器的名字，这个方法必须被写他的返回值；
     * 因为listener需要通过其getName()方法广播它的名称
     * </p>
     */
    @Override
    public String getName() {
        return "frameJobListener";
    }

    /**
     * Scheduler 在 JobDetail 将要被执行时调用这个方法。
     * 任务被触发前触发
     *
     * @param context
     * @see #jobExecutionVetoed(JobExecutionContext)
     */
    @Override
    public void jobToBeExecuted(JobExecutionContext context) {
        log.debug("[{}]:[{}]:开始执行", getName(), context.getJobDetail().getJobClass());
        if (!log.isInfoEnabled()) {
            return;
        }

        Trigger trigger = context.getTrigger();

        Object[] args = {
                context.getJobDetail().getKey().getName(),
                context.getJobDetail().getKey().getGroup(), new java.util.Date(),
                trigger.getKey().getName(), trigger.getKey().getGroup(),
                trigger.getPreviousFireTime(), trigger.getNextFireTime(),
                Integer.valueOf(context.getRefireCount())
        };

        log.info(MessageFormat.format(getJobToBeFiredMessage(), args));

    }

    /**
     * </p>
     * Scheduler 在 JobDetail 即将被执行，但又被 TriggerListener否决了时调用这个方法
     * 但是如果当TriggerListener中的vetoJobExecution方法返回true时,那么执行这个方法.
     *
     * @param context
     * @see #jobToBeExecuted(JobExecutionContext)
     */
    @Override
    public void jobExecutionVetoed(JobExecutionContext context) {
        log.debug("[{}]:[{}]:被否决", getName(), context.getJobDetail().getJobClass());

    }

    /**
     * 任务调度完成后触发
     * Scheduler 在 JobDetail 被执行之后调用这个方法
     *
     * @param context
     * @param jobException
     */
    @Override
    public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {
        log.debug("[{}]:[{}]:执行结束", getName(), context.getJobDetail().getJobClass());


        Trigger trigger = context.getTrigger();

        Object[] args = null;

        if (jobException != null) {
            if (!log.isWarnEnabled()) {
                return;
            }

            String errMsg = jobException.getMessage();
            args =
                    new Object[]{
                            context.getJobDetail().getKey().getName(),
                            context.getJobDetail().getKey().getGroup(), new java.util.Date(),
                            trigger.getKey().getName(), trigger.getKey().getGroup(),
                            trigger.getPreviousFireTime(), trigger.getNextFireTime(),
                            Integer.valueOf(context.getRefireCount()), errMsg
                    };

            log.warn(MessageFormat.format(getJobFailedMessage(), args), jobException);
        } else {
            if (!log.isInfoEnabled()) {
                return;
            }
            String result = String.valueOf(context.getResult());
            args =
                    new Object[]{
                            context.getJobDetail().getKey().getName(),
                            context.getJobDetail().getKey().getGroup(), new java.util.Date(),
                            trigger.getKey().getName(), trigger.getKey().getGroup(),
                            trigger.getPreviousFireTime(), trigger.getNextFireTime(),
                            Integer.valueOf(context.getRefireCount()), result
                    };

            log.info(MessageFormat.format(getJobSuccessMessage(), args));
        }
    }
}
