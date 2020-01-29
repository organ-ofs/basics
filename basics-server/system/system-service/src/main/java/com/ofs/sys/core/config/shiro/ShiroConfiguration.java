package com.ofs.sys.core.config.shiro;

import com.ofs.web.auth.service.ShiroService;
import com.ofs.web.auth.shiro.AgileSubjectFactory;
import com.ofs.web.auth.shiro.CredentialsMatcher;
import com.ofs.web.auth.shiro.MyRealm;
import com.ofs.web.jwt.JwtFilter;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.MethodInvokingFactoryBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.Map;

/**
 * @author gaoly
 * @version 2017/9/22
 */
@Configuration
@Slf4j
public class ShiroConfiguration {

    @Autowired
    private ShiroService shiroService;

    @Bean
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
        log.info("Shiro Configuration initialized");
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();

        //设置SecurityManager
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        //拦截器
        //<!-- 过滤链定义，从上向下顺序执行，一般将 /**放在最为下边 -->:这是一个坑呢，一不小心代码就不好使了;
        //<!-- authc:所有url都必须认证通过才可以访问; anon:所有url都都可以匿名访问-->
        Map<String, String> filterChainDefinitionMap = shiroService.getFilterChainDefinitionMap();

        //过滤器
        Map<String, Filter> filters = new HashMap<>(16);
        filters.put("perms", new JwtFilter());
        shiroFilterFactoryBean.setFilters(filters);
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }

    @Bean
    public MyRealm myRealm() {
        MyRealm myRealm = new MyRealm();
        myRealm.setCredentialsMatcher(new CredentialsMatcher());
        myRealm.setAuthorizationCacheName(MyRealm.class.getName() + ".authorizationCache");
        return myRealm;
    }

    @Bean
    public SecurityManager securityManager(RedisCacheManager redisCacheManager) {
        DefaultWebSecurityManager manager = new DefaultWebSecurityManager();
        manager.setRealm(myRealm());
        manager.setCacheManager(redisCacheManager);
        /*
         * 关闭session存储，禁用Session作为存储策略的实现，
         * 但它没有完全地禁用Session所以需要配合SubjectFactory中的context.setSessionCreationEnabled(false)
         * manager.setSessionManager(sessionManager());
         */
        ((DefaultSessionStorageEvaluator) ((DefaultSubjectDAO) manager.getSubjectDAO())
                .getSessionStorageEvaluator()).setSessionStorageEnabled(false);
        manager.setSubjectFactory(new AgileSubjectFactory());
        return manager;
    }

    /**
     * 开启shiro aop注解支持
     *
     * @param securityManager
     * @return
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

    /**
     * DefaultAdvisorAutoProxyCreator，Spring的一个bean，由Advisor决定对哪些类的方法进行AOP代理。
     */
    @Bean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator defaultAap = new DefaultAdvisorAutoProxyCreator();
        defaultAap.setProxyTargetClass(true);
        return defaultAap;
    }

    @Bean
    public MethodInvokingFactoryBean methodInvokingFactoryBean(SecurityManager securityManager) {
        MethodInvokingFactoryBean bean = new MethodInvokingFactoryBean();
        bean.setStaticMethod("org.apache.shiro.SecurityUtils.setSecurityManager");
        bean.setArguments(securityManager);
        return bean;
    }

    @ConfigurationProperties(prefix = "spring.redis")
    @Bean("shiroRedisManager")
    public RedisManager redisManager() {
        RedisManager redisManager = new RedisManager();
        redisManager.setHost("127.0.0.1:6379");
        redisManager.setPassword("root");
        redisManager.setExpire(1800);
        // 配置缓存过期时间
        redisManager.setTimeout(0);
        return redisManager;
    }

    @Bean("shiroRedisCacheManager")
    public RedisCacheManager cacheManager(RedisManager manager) {
        RedisCacheManager redisCacheManager = new RedisCacheManager();
        redisCacheManager.setRedisManager(manager);
        return redisCacheManager;
    }
    /*
    禁用SESSION，所以此配置无效
    @Bean
    public RedisSessionDAO redisSessionDAO(){
        RedisSessionDAO dao = new RedisSessionDAO();
        dao.setRedisManager(redisManager());
        return dao;
    }

    @Bean
    public DefaultWebSessionManager sessionManager() {
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        sessionManager.setSessionValidationSchedulerEnabled(false);
        //sessionManager.setSessionDAO(redisSessionDAO());
        return sessionManager;
    }*/
}
