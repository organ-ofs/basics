package com.ofs.commons.plugin.quartz.config;

import lombok.extern.slf4j.Slf4j;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.ee.servlet.QuartzInitializerListener;
import org.quartz.spi.JobFactory;
import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by  on 2018/6/4 11:02
 * Quartz的核心配置类
 */
@Configuration
@ConditionalOnProperty(
        name = {"frame.quartz.enable"},
        havingValue = "true",
        matchIfMissing = true
)
@Slf4j
public class ConfigQuartz {

    public static final String QUARTZ_PROPERTIES_PATH = "/config/quartz.properties";


    //配置自定义JobFactory
    @Bean
    public JobFactory jobFactory(ApplicationContext applicationContext) {
        AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();
        jobFactory.setApplicationContext(applicationContext);
        return jobFactory;
    }

    /**
     * SchedulerFactoryBean这个类的真正作用提供了对org.quartz.Scheduler的创建与配置，
     * 并且会管理它的生命周期与Spring同步。
     * org.quartz.Scheduler: 调度器。所有的调度都是由它控制。
     *
     * @param dataSource 为SchedulerFactory配置数据源
     * @param jobFactory 为SchedulerFactory配置JobFactory
     *                   <p>
     *                   dataSource：当需要使用数据库来持久化任务调度数据时，你可以在Quartz中配置数据源，也可以直接在Spring中通过dataSource指定一个Spring管理的数据源。如果指定了该属性，即使quartz.properties中已经定义了数据源，也会被此dataSource覆盖；
     *                   transactionManager：可以通过该属性设置一个Spring事务管理器。在设置dataSource时，Spring强烈推荐你使用一个事务管理器，否则数据表锁定可能不能正常工作；
     *                   nonTransactionalDataSource：在全局事务的情况下，如果你不希望Scheduler执行化数据操作参与到全局事务中，则可以通过该属性指定数据源。在Spring本地事务的情况下，使用dataSource属性就足够了；
     *                   quartzProperties：类型为Properties，允许你在Spring中定义Quartz的属性。其值将覆盖quartz.properties配置文件中的设置，这些属性必须是Quartz能够识别的合法属性，在配置时，你可以需要查看Quartz的相关文档。
     */
    @Bean
    public SchedulerFactoryBean schedulerFactoryBean(DataSource dataSource, JobFactory jobFactory, PlatformTransactionManager transactionManager) throws IOException {
        SchedulerFactoryBean factory = new SchedulerFactoryBean();
        //可选,QuartzScheduler启动时更新己存在的Job,
        // 这样就不用每次修改targetObject后删除qrtz_job_details表对应记录
        factory.setOverwriteExistingJobs(true);
        //QuartzScheduler 延时启动，应用启动完100秒后 QuartzScheduler 再启动
        factory.setStartupDelay(100);
        factory.setAutoStartup(true); //设置自行启动
        factory.setDataSource(dataSource);
        factory.setJobFactory(jobFactory);
        factory.setQuartzProperties(quartzProperties());
        factory.setTransactionManager(transactionManager);
        return factory;
    }

    //从quartz.properties文件中读取Quartz配置属性
    @Bean
    public Properties quartzProperties() throws IOException {
        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        propertiesFactoryBean.setLocation(new ClassPathResource(QUARTZ_PROPERTIES_PATH));
        propertiesFactoryBean.afterPropertiesSet();
        return propertiesFactoryBean.getObject();
    }


    /**
     * quartz初始化监听器
     */
    @Bean
    public QuartzInitializerListener executorListener() {
        return new QuartzInitializerListener();
    }


    /**
     * quartz监听器
     */
    @Bean
    public FrameJobListener getFrameJobListener() {
        return new FrameJobListener();
    }

    /**
     * quartz监听器
     */
    @Bean
    public FrameTriggerListener getFrameTriggerListener() {
        return new FrameTriggerListener();
    }

    /**
     * 通过SchedulerFactoryBean获取Scheduler的实例
     */
    @Bean(name = "scheduler")
    public Scheduler scheduler(SchedulerFactoryBean schedulerFactoryBean) throws IOException, SchedulerException {

        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        scheduler.getListenerManager().addJobListener(this.getFrameJobListener());
        scheduler.getListenerManager().addTriggerListener(this.getFrameTriggerListener());

        scheduler.start();
        return scheduler;
    }

    /*
    配置JobFactory,为quartz作业添加自动连接支持
    AutowiringSpringBeanJobFactory 类是为了可以在scheduler中使用spring注解，
     如果不使用注解，可以不适用该类，而直接使用SpringBeanJobFactory*/
    public final class AutowiringSpringBeanJobFactory extends SpringBeanJobFactory implements
            ApplicationContextAware {
        private transient AutowireCapableBeanFactory beanFactory;

        @Override
        public void setApplicationContext(final ApplicationContext context) {
            beanFactory = context.getAutowireCapableBeanFactory();
        }

        @Override
        protected Object createJobInstance(final TriggerFiredBundle bundle) throws Exception {
            final Object job = super.createJobInstance(bundle);
            try {
                beanFactory.autowireBean(job);
            } catch (Exception e) {
                log.error("quartz autowireBean error:", e);
            }
            return job;
        }
    }

}
