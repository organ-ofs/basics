package com.ofs.sys.config.shiro;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ofs.sys.entity.SysUser;
import com.ofs.sys.service.SysUserService;
import com.ofs.web.exception.RequestException;
import com.ofs.web.jwt.JwtToken;
import com.ofs.web.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author gaoly
 * @version 2017/9/22
 */
@Slf4j
public class MyRealm extends AuthorizingRealm {

    @Autowired
    private SysUserService userService;

    @Autowired
    private CacheManager cacheManager;

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        log.info("Shiro权限验证执行");
        JwtToken jwtToken = new JwtToken();
        BeanUtils.copyProperties(principalCollection.getPrimaryPrincipal(), jwtToken);
        if (jwtToken.getLoginId() != null) {
            SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
            SysUser findUser = userService.findUserByLoginId(jwtToken.getLoginId(), true);
            if (findUser != null) {
                if (findUser.getRoles() != null) {
                    findUser.getRoles().forEach(role -> {
                        info.addRole(role.getName());
                        if (role.getMenus() != null) {
                            role.getMenus().forEach(v -> {
                                if (!"".equals(v.getPermission().trim())) {
                                    info.addStringPermission(v.getPermission());
                                }
                            });
                        }
                    });
                }
                return info;
            }
        }
        throw new DisabledAccountException("用户信息异常，请重新登录！");
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        JwtToken token = (JwtToken) authenticationToken;
        SysUser user;
        String username = token.getLoginId() != null ? token.getLoginId() : JwtUtil.getLoginId(token.getToken());
        try {
            QueryWrapper wrapper = new QueryWrapper<SysUser>();
            wrapper.eq("username", username);
//            wrapper.set("id,username,status,password")
            user = userService.getOne(wrapper);
        } catch (RequestException e) {
            throw new DisabledAccountException(e.getMsg());
        }
        if (user == null) {
            throw new DisabledAccountException("用户不存在！");
        }
        if (user.getStatus() != "1") {
            throw new DisabledAccountException("用户账户已锁定，暂无法登陆！");
        }
        if (token.getLoginId() == null) {
            token.setLoginId(user.getLoginId());
        }
        String sign = JwtUtil.sign(user.getId(), user.getLoginId(), user.getPassword());
        if (token.getToken() == null) {
            token.setToken(sign);
        }
        token.setUid(user.getId());
        return new SimpleAuthenticationInfo(token, user.getPassword(), user.getId());
    }

    public void clearAuthByUserId(String uid, Boolean author, Boolean out) {
        //获取所有session
        Cache<Object, Object> cache = cacheManager
                .getCache(MyRealm.class.getName() + ".authorizationCache");
        cache.remove(uid);
    }

    public void clearAuthByUserIdCollection(List<String> userList, Boolean author, Boolean out) {
        Cache<Object, Object> cache = cacheManager
                .getCache(MyRealm.class.getName() + ".authorizationCache");
        userList.forEach(cache::remove);
    }
}
