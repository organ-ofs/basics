package com.ofs.web.jwt;

import com.ofs.web.auth.model.ShiroRole;
import com.ofs.web.auth.model.ShiroUser;
import com.ofs.web.auth.service.ShiroService;
import com.ofs.web.constant.FrameProperties;
import com.ofs.web.shiro.MyRealm;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * @author ly
 */
@Slf4j
public class JwtRealm extends AuthorizingRealm {

    @Autowired
    @Lazy
    private ShiroService shiroService;

    @Autowired
    FrameProperties frameProperties;

    @Override
    protected void onInit() {
        super.onInit();

    }

    /**
     * 判断登陆用户是否拥有权限
     */
    @Override
    public boolean isPermitted(PrincipalCollection principals, String permission) {
        //@RequiresPermissions是否起作用
        if (this.frameProperties.getAuth().isRequiresPermissions()) {
            ShiroUser shiroUser = (ShiroUser) principals.getPrimaryPrincipal();
            String adminAccount = StringUtils.trimToEmpty(this.frameProperties.getAuth().getAdminAccount());
            if (adminAccount.equals(shiroUser.getAccount())) {
                return true;
            } else {
                return super.isPermitted(principals, permission);

            }
        } else {
            return true;
        }
    }

    /**
     * 本real支持的验证规则
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

    /**
     * 检测用户权限
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
        //获取登录用户
        ShiroUser shiroUser = (ShiroUser) principals.getPrimaryPrincipal();
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();

        //添加role 信息
        List<ShiroRole> roles = new ArrayList<>(shiroService.getAllRoleByUserId(shiroUser.getAccount()));

        if (CollectionUtils.isEmpty(roles)) {
            roles.forEach(role -> {
                simpleAuthorizationInfo.addRole(role.getCode());
            });
        } else {
            simpleAuthorizationInfo.setRoles(new HashSet<>());
        }


        //Permission的权限信息
        Set<String> permissionsList = new HashSet<>(shiroService.getAllPermissionTag(shiroUser.getAccount()));

        if (CollectionUtils.isEmpty(permissionsList)) {
            permissionsList = new HashSet<>();
        }
        simpleAuthorizationInfo.setStringPermissions(permissionsList);

        return simpleAuthorizationInfo;
    }

    /**
     * 用户验证。
     * doGetAuthorizationInfo执行时机有三个，如下：
     * <p>
     * 1、subject.hasRole(“admin”) 或 subject.isPermitted(“admin”)：
     * 2、@RequiresRoles("admin") ：在方法上加注解的时候；
     * 3、[@shiro.hasPermission name = "admin"][/@shiro.hasPermission]：在页面上加shiro标签的时候，即进这个页面的时候扫描到有这个标签的时候。
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken auth) {
        if (log.isDebugEnabled()) {
            log.debug("JWT 登陆");
        }
        JwtToken jwtAuth = (JwtToken) auth;
        String token = (String) jwtAuth.getToken();

        String account = JwtUtil.getAccount(token);
        String id = JwtUtil.getId(token);
        String terminal = JwtUtil.getTerminal(token);

        ShiroUser shiroUser = shiroService.getUserByAccount(account);
        shiroUser.setTerminal(terminal);
        shiroUser.setJwtId(id);

        String runAsAccount = JwtUtil.getRunAsAccount(token);
        //处理runas
        if (StringUtils.isNotBlank(runAsAccount)) {
            shiroUser.setRunAsAccount(runAsAccount);
        }
        // 若存在，将此用户存放到登录认证info中，Shiro会为我们进行密码对比校验
        return new SimpleAuthenticationInfo(shiroUser, token, getName());
    }


    @Override
    public String getName() {
        return MyRealm.class.getName();
    }

    @Override
    public void onLogout(PrincipalCollection principals) {
        if (log.isDebugEnabled()) {
            log.debug("logout clear cache");
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
