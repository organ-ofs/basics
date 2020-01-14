package com.ofs.commons.plugin.quartz.job;

import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;

import java.util.Date;

import static com.ofs.commons.plugin.quartz.knowledge.JobKnowledge.JOB_PARAM_KEY;

/**
 * @author gaoly
 */
//@DisallowConcurrentExecution
@Slf4j
public class HelloJobBean extends BaseJobBean {


    public HelloJobBean() {
        log.error("HelloJobBean ");

    }

    @Override
    public void executeInternal(JobExecutionContext context) {
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            log.error("HelloJobBean e: ", e);
            Thread.currentThread().interrupt();

        }
        int w = 0;
        log.error("hello Job执行时间: " + new Date());
        log.debug("JOB_PARAM_KEY=[{}]", context.getMergedJobDataMap().get(JOB_PARAM_KEY));
        ;
    }
}
