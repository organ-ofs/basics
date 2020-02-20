package com.ofs.web.shiro;

import com.ofs.web.auth.model.ShiroUser;
import com.ofs.web.constant.CacheConstant;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.Serializable;
import java.util.Deque;
import java.util.LinkedList;

/**
 * @author ly
 * 用户管理filter，单点登陆
 */
@Slf4j
public class KickOutFilter extends BasicHttpAuthenticationFilter {
    /**
     * 踢出之前登录的/之后登录的用户 默认踢出之前登录的用户
     */
    @Setter
    private boolean kickOutAfter = false;
    /**
     * 同一个帐号最大会话数 默认1
     */
    private int maxSession = 1;


    /**
     * 登陆用户列表
     */
    private Cache<String, Deque<Serializable>> cache;

    /**
     * 已删除用户列表
     */
    private Cache<Serializable, String> kickedCache;

    public void setMaxSession(int maxSession) {
        this.maxSession = maxSession;
    }


    /**
     * 设置Cache的key的前缀
     */

    public void setCacheManager(CacheManager cacheManager) {

        this.cache = cacheManager.getCache(CacheConstant.SHIRO_KICK_OUT_DEQUE);

        this.kickedCache = cacheManager.getCache(CacheConstant.SHIRO_KICKED_TOKEN);


    }

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {

        if (log.isDebugEnabled()) {
            log.debug("KickOutFilter isAccessAllowed:[{}]");
        }
        Subject subject = getSubject(request, response);

        if (!subject.isAuthenticated()) {
            //如果没有登录，直接进行之后的流程
            return true;
        }

        ShiroUser shiroUser = ShiroUser.getCurrentUser();
        String account = shiroUser.getAccount() + "_" + shiroUser.getTerminal();
        //jwtid
        Serializable sessionId = shiroUser.getJwtId();
        //读取缓存 没有就存入
        Deque<Serializable> deque = cache.get(account);

        if (deque == null) {
            deque = new LinkedList<>();
            cache.put(account, deque);
        }

        //如果队列里没有此sessionId，且用户没有被踢出；放入队列
        if (!deque.contains(sessionId)) {
            //将sessionId存入队列
            deque.push(sessionId);
            cache.put(account, deque);

        }

        //如果队列里的sessionId数超出最大会话数，开始踢人
        while (deque.size() > maxSession) {
            Serializable kickOutSessionId = null;
            if (kickOutAfter) {
                //如果踢出后者
                kickOutSessionId = deque.removeFirst();
            } else {
                //否则踢出前者
                kickOutSessionId = deque.removeLast();
            }
            cache.put(account, deque);

            //添加到已经删除的cache
            kickedCache.put(kickOutSessionId, "1");
        }

        //如果被踢出了，直接退出，重定向到踢出后的地址
        if (kickedCache.get(sessionId) != null) {
            //会话被踢出了
            try {
                //退出登录
                subject.logout();
            } catch (Exception e) {
                log.error("logout", e);
            }
            //会话被踢出了
            return false;
        }
        return true;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {

        if (log.isDebugEnabled()) {
            log.debug("KickOutFilter onAccessDenied:[{}]");
        }

        return true;
    }
}

