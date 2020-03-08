package com.ofs.web.shiro.matcher;

import com.ofs.web.constant.CacheConstant;
import com.ofs.web.jwt.JwtToken;
import com.ofs.web.jwt.JwtUtil;
import com.ofs.web.utils.Tools;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author ly
 */
public class RetryLimitCredentialsMatcher extends HashedCredentialsMatcher {

    private Cache<String, AtomicInteger> passwordRetryCache;

    private int maxRetryCount = 5;

    public RetryLimitCredentialsMatcher(CacheManager cacheManager, int maxRetryCount) {
        passwordRetryCache = cacheManager.getCache(CacheConstant.SHIRO_PASS_RETRY);
        this.maxRetryCount = maxRetryCount;

    }

    /**
     * 自定义密码验证逻辑
     *
     * @param token
     * @param info
     * @return
     */
    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        JwtToken jwtToken = (JwtToken) token;
        if (jwtToken.getPassword() == null) {
            // token 验证
            boolean verify = JwtUtil.verify(jwtToken.getToken(), jwtToken.getAccount());
            if (!verify) {
                throw new DisabledAccountException(Tools.VERIFYFAIL);
            }
            return true;
        } else {
            // 密码验证
            String account = (String) token.getPrincipal();
            String keyUserName = "retry_" + account;
            // retry count + 1
            AtomicInteger retryCount = passwordRetryCache.get(keyUserName);
            if (retryCount == null) {
                retryCount = new AtomicInteger(0);
                passwordRetryCache.put(keyUserName, retryCount);
            }
            if (retryCount.incrementAndGet() > this.maxRetryCount) {
                // if retry count > 5 throw
                throw new ExcessiveAttemptsException();
            }
            boolean matches = super.doCredentialsMatch(token, info);
            if (matches) {
                // clear retry count
                passwordRetryCache.remove(keyUserName);
            } else {
                //TODO 验证不成功是否要同步到缓存?
                passwordRetryCache.put(keyUserName, retryCount);
            }
            return matches;
        }
    }


}
