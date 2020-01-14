package com.ofs.commons.plugin.quartz.config;

import com.google.common.collect.Lists;
import com.ofs.web.constant.FrameProperties;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.Trigger;
import org.quartz.TriggerListener;

import javax.annotation.Resource;

/**
 * @author: gaoly
 * @date:
 */
@Slf4j
public class FrameTriggerListener implements TriggerListener {


    @Resource
    FrameProperties frameProperties;

    /**
     * <p>
     * Get the name of the <code>TriggerListener</code>.
     * </p>
     */
    @Override
    public String getName() {
        return "frameTriggerListener";
    }

    /**
     * 被调度时触发，和它相关的org.quartz.jobdetail即将执行。
     * 该方法优先vetoJobExecution()执行
     *
     * @param trigger
     * @param context
     */
    @Override
    public void triggerFired(Trigger trigger, JobExecutionContext context) {
        log.debug("[{}]:[{}]:triggerFired", trigger.getJobKey().getGroup(), trigger.getJobKey().getName());


    }

    /**
     * 被调度时触发，和它相关的org.quartz.jobdetail即将执行。
     * TriggerListener 给了一个选择去否决 Job 的执行。假如这个方法返回 true，这个 Job 将不会为此次 Trigger 触发而得到执行。
     *
     * @param trigger The <code>Trigger</code> that has fired.
     * @param context The <code>JobExecutionContext</code> that will be passed to
     */
    @Override
    public boolean vetoJobExecution(Trigger trigger, JobExecutionContext context) {


        String jobGroup = trigger.getJobKey().getGroup();
        String[] includeJobGroup = frameProperties.getQuartz().getIncludeJobGroup();

        if (includeJobGroup != null && includeJobGroup.length > 0) {
            //符合才运行
            if (Lists.newArrayList(includeJobGroup).contains(jobGroup)) {
                return false;
            } else {
                log.debug("[{}]:[{}]:vetoJobExecution 否决", trigger.getJobKey().getGroup(), trigger.getJobKey().getName());
                return true;
            }
        } else {
            String[] excludeJobGroup = frameProperties.getQuartz().getExcludeJobGroup();
            if (excludeJobGroup != null && excludeJobGroup.length > 0) {
                //排除对应的job
                if (Lists.newArrayList(excludeJobGroup).contains(jobGroup)) {
                    log.debug("[{}]:[{}]:vetoJobExecution 否决", trigger.getJobKey().getGroup(), trigger.getJobKey().getName());

                    return true;
                } else {
                    return false;
                }
            }
        }
        return false;

    }


    /**
     * Scheduler 调用这个方法是在 Trigger 错过触发时。
     * 如这个方法的 JavaDoc 所指出的，你应该关注此方法中持续时间长的逻辑：在出现许多错过触发的 Trigger 时，
     * 长逻辑会导致骨牌效应。你应当保持这上方法尽量的小。
     *
     * @param trigger
     */
    @Override
    public void triggerMisfired(Trigger trigger) {
        log.debug("[{}]:[{}]:triggerMisfired", trigger.getJobKey().getGroup(), trigger.getJobKey().getName());

    }

    /**
     * </p>
     * 执行完毕时触发
     *
     * @param trigger
     * @param context
     * @param triggerInstructionCode
     */
    @Override
    public void triggerComplete(Trigger trigger, JobExecutionContext context, Trigger.CompletedExecutionInstruction triggerInstructionCode) {
        log.debug("[{}]:[{}]:triggerComplete", trigger.getJobKey().getGroup(), trigger.getJobKey().getName());

    }
}

