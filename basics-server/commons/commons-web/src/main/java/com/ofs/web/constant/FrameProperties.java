package com.ofs.web.constant;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author ly
 */
@ConfigurationProperties(prefix = "frame")
@Component
@Data
@NoArgsConstructor
public class FrameProperties {
    /**
     * Api账户信息
     */
    private FrameProperties.Config config;

    /**
     * 权限模块控制
     */
    private FrameProperties.Auth auth;


    /**
     * quartz配置
     */
    private Quartz quartz;


    /**
     * 系统模块控制
     */
    @Data
    @NoArgsConstructor
    public static class Config {
        /**
         * 是否启动加密 默认为False
         */
        private boolean encryptEnable = false;

        /**
         * 是否启动swagger 默认为False
         */
        private boolean swaggerEnable = false;

        /**
         * RSA 私有key
         */
        private String rsaPrivateKey = "";

        /**
         * 日志有效期
         */
        private int logValidity = 30;

        /**
         * redis默认超时时间
         */
        private long redisDefaultExpiration = 300L;

        /**
         * 测试账户
         */
        private String testAccount = "admin";

        /**
         * 测试平台
         */
        private String testTerminal = Knowledge.TerminalEnum.PC.getValue();

        /**
         * cors 的访问来源
         */
        private String[] apiAllowedOrigins = {"*"};


    }

    /**
     * 权限模块控制
     */
    @Data
    @NoArgsConstructor
    public static class Auth {
        /**
         * 超级管理员
         */
        private String adminAccount = "admin";

        /**
         * jwtRealm isPermitted全局属性
         */
        private boolean requiresPermissions = true;

        /**
         * 密码重试次数
         */
        private int passwordRetryLimit = 5;
        /**
         * 是否执行kickout
         */
        private boolean kickOutValid = false;
        /**
         * 踢出之前登录的或者 之后登录的用户 默认(false)踢出之前登录的用户
         */
        private boolean kickOutAfter = false;
        /**
         * 同一个帐号最大会话数 默认1
         */
        private int kickOutMaxSession = 1;

        /**
         * 不要权限限制的url
         */
        private String[] ignoreUrl;
        /**
         * jwt密码信息
         */
        private String jwtSecret;
        /**
         * 默认60分钟
         */
        private long jwtExpireTime = 60 * 60 * 1000L;


    }

    /**
     * quartz配置
     *
     * @author ly
     */
    @Data
    @NoArgsConstructor
    public static class Quartz {

        /**
         * 是否启动quartz
         */
        private boolean enable = false;
        /**
         * 包含的job group,多个数组 ，优先处理include ，include有值不处理exclude
         */
        private String[] includeJobGroup;
        /**
         * 不排除的job group,多个数组
         */
        private String[] excludeJobGroup;
    }


}
