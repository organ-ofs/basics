package com.ofs.web.constant;

/**
 * 缓存用常量
 *
 * @author
 * @since 2018/04/18
 */
public class CacheConstant {

    private CacheConstant() {

    }

    /**
     * 10个小时
     */
    public static final Long EXPIRE_TIME_TEN_HOUR = 10 * 60 * 60L;
    /**
     * 2个小时
     */
    public static final Long EXPIRE_TIME_TWO_HOUR = 2 * 60 * 60L;
    /**
     * 10分钟
     */
    public static final Long EXPIRE_TIME_ONE_MINUTE = 10 * 60L;

    /**
     * 要进行kickOut处理的token列表,3600S
     */
    public static final String SHIRO_KICK_OUT_DEQUE = "kickOutDeque";

    /**
     * kickOut处理过的token,7200S要和超时时间相一致
     */
    public static final String SHIRO_KICKED_TOKEN = "kickedToken";
    /**
     * logout退出的token,7200S要和超时时间相一致
     */
    public static final String SHIRO_LOGOUT_TOKEN = "logoutToken";

    /**
     * ShiroRealm.authorization 授权缓存2 * 60 * 60
     */
    public static final String SHIRO_PASS_REALM_AUTHORIZATION = "ShiroRealm.authorization";
    /**
     * JwtRealm.authorization 授权缓存 2 * 60 * 60
     */
    public static final String SHIRO_JWT_REALM_AUTHORIZATION = "JwtRealm.authorization";

    /**
     * passwordRetry 密码重试限制缓存 10 * 60
     */
    public static final String SHIRO_PASS_RETRY = "passwordRetry";

}
