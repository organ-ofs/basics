package com.ofs.web.base.component;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author ly
 */
@Component
@Slf4j
public class ApplicationContextComponent implements ApplicationContextAware {
    private static ApplicationContext applicationContext;

    /**
     * 设置spring上下文
     *
     * @param applicationContext spring上下文
     * @throws BeansException
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (log.isDebugEnabled()) {
            log.debug("ApplicationContext registed:[{}]", applicationContext);
        }
        ApplicationContextComponent.applicationContext = applicationContext;
    }

    /**
     * 获取容器
     *
     * @return
     */
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * 根据类型获取容器对象
     *
     * @param type
     * @param <T>
     * @return
     */
    public static <T> T getBeanByType(Class<T> type) {
        return applicationContext.getBean(type);
    }


    /**
     * 根据类型获取容器对象
     *
     * @param type
     * @param <T>
     * @return
     */
    public static <T> Map<String, T> getBeansByType(Class<T> type) {
        return applicationContext.getBeansOfType(type);
    }


    /**
     * 如果BeanFactory包含一个与所给名称匹配的bean定义，则返回true
     *
     * @param name
     * @return boolean
     */
    public static boolean containsBean(String name) {
        return applicationContext.containsBean(name);
    }

    /**
     * 根据名称获取容器对象
     *
     * @param beanName
     * @return
     */
    public static Object getBeanByName(String beanName) {
        return applicationContext.getBean(beanName);

    }
}
