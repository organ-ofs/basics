package com.ofs.sys.core.config.shiro;

import com.ofs.sys.serv.entity.SysUser;
import com.ofs.sys.serv.service.SysUserService;
import com.ofs.web.constant.FrameProperties;
import com.ofs.web.jwt.JwtToken;
import com.ofs.web.jwt.JwtUtil;
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

import java.util.HashSet;
import java.util.Set;



/**
 * @author ly
 */
@Slf4j
public class MyRealm extends AuthorizingRealm {

    @Autowired
    @Lazy
    private SysUserService userService;


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
     * 检测用户权限，此方法调用hasRole,hasPermission
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
        ShiroUser shiroUser = (ShiroUser) principals.getPrimaryPrincipal();
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();

        //Permission的权限信息
        Set<String> permissionsList = new HashSet<>(userService.getAllPermissionTag(shiroUser.getAccount()));

        //Permission的权限信息
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
        String token = (String) jwtAuth.getCredentials();

        String account = JwtUtil.getAccount(token);
        String id = JwtUtil.getId(token);
        String terminal = JwtUtil.getTerminal(token);

        SysUser user = userService.getUserByLoginId(account, false);
        ShiroUser shiroUser = new ShiroUser();
        shiroUser.setAccount(account);
        shiroUser.setAuthId(user.getId());
        shiroUser.setName(user.getName());
        shiroUser.setTerminal(terminal);
        shiroUser.setJwtId(id);

        String runAsAccount = JwtUtil.getRunAsAccount(token);
        //处理runas
        if (StringUtils.isNotBlank(runAsAccount)) {
            shiroUser.setRunAsAccount(runAsAccount);
        }
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
