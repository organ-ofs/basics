package com.ofs.web.shiro;

import com.ofs.web.constant.CacheConstant;
import com.ofs.web.constant.FrameProperties;
import com.ofs.web.jwt.JwtFilter;
import com.ofs.web.shiro.matcher.RetryLimitCredentialsMatcher;
import com.ofs.web.shiro.util.PasswordHash;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authc.pam.FirstSuccessfulStrategy;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.MethodInvokingFactoryBean;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 权限检查类
 *
 * @author ly
 * @version 2019年5月20日
 */
@Slf4j
@Configuration
public class ShiroConfig {

    /**
     * 系统参数设置
     */
    @Autowired
    private FrameProperties frameProperties;

    /**
     * 缓存管理器 根据插件自动注入
     */
    @Autowired
    @Qualifier("shiroCache")
    private CacheManager shiroCache;

    /**
     * 定义多个realm的认证策略配置,使用FirstSuccessfulStrategy
     *
     * @return
     */
    @Bean
    public ModularRealmAuthenticator modularRealmAuthenticator() {
        ModularRealmAuthenticator modularRealmAuthenticator = new ModularRealmAuthenticator();
        modularRealmAuthenticator.setAuthenticationStrategy(new FirstSuccessfulStrategy());
        return modularRealmAuthenticator;
    }

    /**
     * 安全管理器
     */
    @Bean
    public DefaultWebSecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();

        securityManager.setAuthenticator(modularRealmAuthenticator());
        Collection<Realm> realms = new ArrayList<>();
        realms.add(myRealm());
        // 其他realm realms.add(myRealm());
        securityManager.setRealms(realms);
        //自定义缓存管理
        securityManager.setCacheManager(shiroCache);
        /*
         * 关闭shiro自带的session，详情见文档
         * http://shiro.apache.org/session-management.html#SessionManagement-StatelessApplications%28Sessionless%29
         */
        ((DefaultSessionStorageEvaluator) ((DefaultSubjectDAO) securityManager.getSubjectDAO())
                .getSessionStorageEvaluator()).setSessionStorageEnabled(false);
        securityManager.setSubjectFactory(new AgileSubjectFactory());

        return securityManager;
    }


    /**
     * 項目自定义的Realm -
     *
     * @return
     */
    @Bean(initMethod = "onInit")
    public MyRealm myRealm() {
        MyRealm myRealm = new MyRealm();

        //Authorization
        myRealm.setAuthorizationCachingEnabled(true);
        //缓存AuthenticationInfo信息的缓存名称
        myRealm.setAuthorizationCacheName(CacheConstant.SHIRO_JWT_REALM_AUTHORIZATION);
        //启用身份验证缓存，即缓存AuthenticationInfo信息，默认false
        myRealm.setAuthenticationCachingEnabled(false);
        myRealm.setCacheManager(shiroCache);
        myRealm.setCredentialsMatcher(hashedCredentialsMatcher(passwordHash()));

        return myRealm;
    }

    /**
     * shiro密码加密配置
     *
     * @return
     */
    @Bean
    public PasswordHash passwordHash() {
        //密码加密 1次md5,增强密码可修改此处
        PasswordHash passwordHash = new PasswordHash();
        passwordHash.setAlgorithmName("MD5");
        passwordHash.setHashIterations(1);
        return passwordHash;
    }


    @Bean
    public KickOutFilter kickOutFilter() {
        KickOutFilter kickOutFilter = new KickOutFilter();
        //使用cacheManager获取相应的cache来缓存用户登录的会话；用于保存用户—会话之间的关系的；
        //也可以重新另写一个，重新配置缓存时间之类的自定义缓存属性
        kickOutFilter.setCacheManager(shiroCache);
        //是否踢出后来登录的，默认是false；即后者登录的用户踢出前者登录的用户；踢出顺序。
        kickOutFilter.setKickOutAfter(this.frameProperties.getAuth().isKickOutAfter());
        //同一个用户最大的会话数，默认1；比如2的意思是同一个用户允许最多同时两个人登录；
        kickOutFilter.setMaxSession(this.frameProperties.getAuth().getKickOutMaxSession());
        return kickOutFilter;
    }

    @Bean
    public FilterRegistrationBean shiroKickOutFilter(KickOutFilter filter) {
        FilterRegistrationBean registration = new FilterRegistrationBean(filter);
        registration.setEnabled(false);
        return registration;
    }

    @Bean
    public JwtFilter jwtFilter() {
        JwtFilter jwtFilter = new JwtFilter();
        jwtFilter.setCacheManager(shiroCache);
        return jwtFilter;
    }

    @Bean
    public FilterRegistrationBean shiroJwtFilter(JwtFilter filter) {
        FilterRegistrationBean registration = new FilterRegistrationBean(filter);
        registration.setEnabled(false);
        return registration;
    }

    @Bean
    public HeaderFilter headerFilter() {
        HeaderFilter headerFilter = new HeaderFilter();
        return headerFilter;
    }

    @Bean
    public FilterRegistrationBean shiroHeaderFilter(HeaderFilter filter) {
        FilterRegistrationBean registration = new FilterRegistrationBean(filter);
        registration.setEnabled(false);
        return registration;
    }

    @Bean("shiroFilter")
    public ShiroFilterFactoryBean shiroFilter(DefaultWebSecurityManager securityManager) {
        log.info("Shiro Configuration initialized");
        ShiroFilterFactoryBean shiroFilter = new ShiroFilterFactoryBean();
        //配置securityManager
        shiroFilter.setSecurityManager(securityManager);

        // 添加自己的过滤器并且取名为jwt
        Map<String, Filter> filterMap = new LinkedHashMap<>();
        filterMap.put("header", headerFilter());
        filterMap.put("jwt", jwtFilter());
        if (this.frameProperties.getAuth().isKickOutValid()) {
            filterMap.put("kickOut", kickOutFilter());
        }
        shiroFilter.setFilters(filterMap);
        shiroFilter.setLoginUrl("/login");

        // 自定义url规则
        //<!-- 过滤链定义，从上向下顺序执行，一般将 /**放在最为下边 -->:这是一个坑呢，一不小心代码就不好使了;
        //<!-- authc:所有url都必须认证通过才可以访问; anon:所有url都都可以匿名访问-->
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();

        String[] ignoreUrl = this.frameProperties.getAuth().getIgnoreUrl();
        if (ArrayUtils.isNotEmpty(ignoreUrl)) {
            for (int i = 0; i < ignoreUrl.length; i++) {
                filterChainDefinitionMap.put(ignoreUrl[i], "header,anon");
            }
        }
        //开放的静态资源 网站图标
        filterChainDefinitionMap.put("/favicon.ico", "anon");
        filterChainDefinitionMap.put("/css/**", "anon");
        filterChainDefinitionMap.put("/js/**", "anon");
        filterChainDefinitionMap.put("/img/**", "anon");
        filterChainDefinitionMap.put("/fonts/**", "anon");
        filterChainDefinitionMap.put("/swagger/**", "anon");

        filterChainDefinitionMap.put("/**/*.css", "anon");
        filterChainDefinitionMap.put("/**/*.js", "anon");
        filterChainDefinitionMap.put("/**/*.html", "anon");
        // 所有请求通过我们自己的JWT Filter

        filterChainDefinitionMap.put("/login", "header,anon");
        filterChainDefinitionMap.put("/logout", "header,jwt");
        filterChainDefinitionMap.put("/test/**", "header,anon");
        if (this.frameProperties.getAuth().isKickOutValid()) {
            filterChainDefinitionMap.put("/**", "header,jwt,kickOut");
        } else {
            filterChainDefinitionMap.put("/**", "header,jwt");
        }

        shiroFilter.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilter;
    }

    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher(PasswordHash passwordHash) {
        HashedCredentialsMatcher hashedCredentialsMatcher = new RetryLimitCredentialsMatcher(shiroCache, this.frameProperties.getAuth().getPasswordRetryLimit());
        //散列算法
        hashedCredentialsMatcher.setHashAlgorithmName(passwordHash.getAlgorithmName());
        //散列次数
        hashedCredentialsMatcher.setHashIterations(passwordHash.getHashIterations());

        return hashedCredentialsMatcher;
    }


    /**
     * 开启shiro aop注解支持
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
        DefaultAdvisorAutoProxyCreator defaultAAP = new DefaultAdvisorAutoProxyCreator();
        defaultAAP.setProxyTargetClass(true);
        return defaultAAP;
    }

    @Bean
    public MethodInvokingFactoryBean methodInvokingFactoryBean(SecurityManager securityManager) {
        MethodInvokingFactoryBean bean = new MethodInvokingFactoryBean();
        bean.setStaticMethod("org.apache.shiro.SecurityUtils.setSecurityManager");
        bean.setArguments(securityManager);
        return bean;
    }
}
