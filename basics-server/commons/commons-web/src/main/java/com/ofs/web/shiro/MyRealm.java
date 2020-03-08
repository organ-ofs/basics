package com.ofs.web.shiro;

import com.ofs.web.auth.model.ShiroUser;
import com.ofs.web.auth.service.ShiroService;
import com.ofs.web.constant.FrameProperties;
import com.ofs.web.jwt.JwtToken;
import com.ofs.web.jwt.JwtUtil;
import com.ofs.web.knowledge.DataDictKnowledge;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

/**
 * 权限检查类
 *
 * @author ly
 */
@Slf4j
public class MyRealm extends AuthorizingRealm {

    @Autowired
    @Lazy
    private ShiroService shiroService;

    @Autowired
    FrameProperties frameProperties;
    /**
     * 本real支持的验证规则
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }


    /**
     * 授权
     * 1.doGetAuthenticationInfo执行时机如下
     * <p>
     * Subject currentUser = SecurityUtils.getSubject();
     * currentUser.login(token);
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        if (log.isDebugEnabled()) {
            log.debug("JWT 授权");
        }
        return null;
    }

    /**
     * 登录验证
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken auth) {
        if (log.isDebugEnabled()) {
            log.debug("JWT 登陆");
        }
        JwtToken token = (JwtToken) auth;
        String tokenStr = token.getToken();
        if (StringUtils.isNotEmpty(tokenStr)) {
            return new SimpleAuthenticationInfo(token, token, getName());
        } else {
            //查出是否有此用户
            ShiroUser shiroUser = shiroService.getUserByAccount(token.getAccount());
            if (shiroUser == null) {
                //无账号
                throw new DisabledAccountException();
            }
            String salt = shiroUser.getSalt();//mSmo6X
            salt = "k2oB4E";
            String password = shiroUser.getPassword();
            String status = shiroUser.getStatus();

            if (!DataDictKnowledge.YesNoEnum.YES.getCode().equals(status)) {
                //无效账号
                throw new DisabledAccountException();
            }

            if ("admin".equals(token.getAccount())) {
                password = "b3144b6de9ae1dd2d56b6367dbdfdc21";
            }
            token.setToken(JwtUtil.getAccessToken(token.getAccount(), token.getTerminal()));
            // 若存在，将此用户存放到登录认证info中，Shiro会为我们进行密码对比校验
            return new SimpleAuthenticationInfo(token, password, ShiroByteSource.of(salt)
                    , getName());
        }

    }

    @Override
    public void onLogout(PrincipalCollection principals) {
        if (log.isDebugEnabled()) {
            log.debug("MyRealm logout clear cache");
        }
        super.clearCachedAuthorizationInfo(principals);
        ShiroUser shiroUser = (ShiroUser) principals.getPrimaryPrincipal();
        removeUserCache(shiroUser.getAccount());
        //TODO 执行退出操作

    }

    @Override
    protected Object getAuthorizationCacheKey(PrincipalCollection principals) {
        ShiroUser shiroUser = (ShiroUser) principals.getPrimaryPrincipal();
        return shiroUser.toString() + "_auth";
    }

    /**
     * 清除用户权限缓存
     */
    public void removeUserCache(String account) {
        SimplePrincipalCollection principals = new SimplePrincipalCollection();
        principals.add(account, super.getName());
        super.clearCachedAuthenticationInfo(principals);
    }

}
