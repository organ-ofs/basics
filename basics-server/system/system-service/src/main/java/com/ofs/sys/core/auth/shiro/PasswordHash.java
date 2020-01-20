package com.ofs.sys.core.auth.shiro;

import com.ofs.sys.serv.entity.SysUser;
import com.ofs.sys.serv.service.SysUserService;
import com.ofs.web.constant.FrameProperties;
import com.ofs.web.jwt.JwtToken;
import com.ofs.web.jwt.JwtUtil;
import com.ofs.web.utils.DigestUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * shiro密码加密配置
 *
 * @author ly
 */
public class PasswordHash implements InitializingBean {
    private String algorithmName;
    private int hashIterations;

    public String getAlgorithmName() {
        return algorithmName;
    }

    public void setAlgorithmName(String algorithmName) {
        this.algorithmName = algorithmName;
    }

    public int getHashIterations() {
        return hashIterations;
    }

    public void setHashIterations(int hashIterations) {
        this.hashIterations = hashIterations;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.hasLength(algorithmName, "algorithmName mast be MD5、SHA-1、SHA-256、SHA-384、SHA-512");
    }

    public String toHex(Object source, Object salt) {
        return DigestUtils.hashByShiro(algorithmName, source, salt, hashIterations);
    }

    /**
     * @author ly
     */
    @Slf4j
    public static class MyRealm extends AuthorizingRealm {

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
            if (MyRealm.log.isDebugEnabled()) {
                MyRealm.log.debug("JWT 授权");
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
            if (MyRealm.log.isDebugEnabled()) {
                MyRealm.log.debug("JWT 登陆");
            }
            JwtToken jwtAuth = (JwtToken) auth;
            String token = (String) jwtAuth.getCredentials();

            String account = JwtUtil.getAccount(token);
            String id = JwtUtil.getId(token);
            String terminal = JwtUtil.getTerminal(token);

            SysUser user = userService.getUserByLoginId(account, false);
            String password = user.getPassword();
            if ("admin".equals(user.getLoginId())) {
                password = "8c7904789282730e283fd614a6a41f3a";
            }
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
            String salt = "k2oB4E";
            // 若存在，将此用户存放到登录认证info中，Shiro会为我们进行密码对比校验
            return new SimpleAuthenticationInfo(shiroUser, token, ShiroByteSource.of(salt), getName());
        }


        @Override
        public String getName() {
            return MyRealm.class.getName();
        }

        @Override
        public void onLogout(PrincipalCollection principals) {
            if (MyRealm.log.isDebugEnabled()) {
                MyRealm.log.debug("logout clear cache");
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

    /**
     * @author ly
     */
    @Data
    @EqualsAndHashCode(callSuper = false)
    @Slf4j
    @NoArgsConstructor
    public static class ShiroUser implements Serializable {

        private static final long serialVersionUID = 1L;
        /**
         * 用户ID
         */
        private String authId;
        /**
         * jwtID
         */
        private String jwtId;
        /**
         * 用户账户
         */
        private String account;
        /**
         * 用户名称
         */
        private String name;
        /**
         * 终端
         */
        private String terminal;

        /**
         * 模拟account的账户
         */
        private String runAsAccount;


        /**
         * 构造函数
         *
         * @param account 账户
         * @param name    姓名
         * @param authId  ID
         */
        public ShiroUser(String account, String name, String authId) {
            this.account = account;
            this.name = name;
            this.authId = authId;
        }


        /**
         * 取得用户信息
         *
         * @return
         */
        public static ShiroUser getCurrentUser() {

            return (ShiroUser) SecurityUtils.getSubject().getPrincipal();

        }

        /**
         * 本函数输出将作为默认的<shiro:principal/>输出.
         */
        @Override
        public String toString() {
            return account + (StringUtils.isNotEmpty(runAsAccount) ? "_" + runAsAccount : "");
        }
    }
}
