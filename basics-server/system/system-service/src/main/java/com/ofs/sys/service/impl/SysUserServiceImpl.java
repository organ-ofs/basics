package com.ofs.sys.service.impl;

import cn.licoy.encryptbody.util.MD5EncryptUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ofs.sys.core.global.ShiroService;
import com.ofs.sys.dto.ResetPasswordDto;
import com.ofs.sys.dto.SignInDto;
import com.ofs.sys.entity.SysMenus;
import com.ofs.sys.entity.SysRole;
import com.ofs.sys.entity.SysUser;
import com.ofs.sys.mapper.SysUserMapper;
import com.ofs.sys.service.SysMenusService;
import com.ofs.sys.service.SysRoleService;
import com.ofs.sys.service.SysUserRoleService;
import com.ofs.sys.service.SysUserService;
import com.ofs.utils.DateUtils;
import com.ofs.web.base.impl.BaseServiceImpl;
import com.ofs.web.bean.SystemCode;
import com.ofs.web.exception.RequestException;
import com.ofs.web.jwt.JwtToken;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
@Transactional
public class SysUserServiceImpl extends BaseServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    @Autowired
    private SysRoleService roleService;

    @Autowired
    private SysUserRoleService userRoleService;

    @Autowired
    private ShiroService shiroService;

    @Autowired
    private SysMenusService menusService;

    @Override
    public SysUser findUserByLoginId(String loginId, boolean hasMenu) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq(SysUser.LOGIN_ID, loginId);
        SysUser user = this.getOne(wrapper);
        if (user == null) {
            return null;
        }
        user.setRoles(roleService.findAllRoleByUserId(user.getId(), hasMenu));
        return user;
    }

    @Override
    public SysUser findUserById(String id, boolean hasMenu) {
        SysUser user = this.getById(id);
        if (user == null) {
            return null;
        }
        user.setRoles(roleService.findAllRoleByUserId(user.getId(), false));
        return user;
    }

    @Override
    public void signIn(SignInDto signInDto) {
        if ("".equals(signInDto.getLoginId()) || "".equals(signInDto.getPassword())) {
            throw new RequestException(SystemCode.SING_IN_INPUT_EMPTY);
        }
        JwtToken token = new JwtToken(null, signInDto.getLoginId(), signInDto.getPassword());
        Subject subject = SecurityUtils.getSubject();
        try {
            subject.login(token);
            if (!subject.isAuthenticated()) {
                throw new RequestException(SystemCode.SIGN_IN_INPUT_FAIL);
            }
        } catch (DisabledAccountException e) {
            throw new RequestException(SystemCode.SIGN_IN_INPUT_FAIL.code, e.getMessage(), e);
        } catch (Exception e) {
            throw new RequestException(SystemCode.SIGN_IN_FAIL, e);
        }
    }


    @Override
    public SysUser getCurrentUser() {
        Subject subject = SecurityUtils.getSubject();
        if (!subject.isAuthenticated()) {
            throw new RequestException(SystemCode.NOT_SING_IN);
        }
        JwtToken jwtToken = super.getJwtToken();
        SysUser user = this.findUserByLoginId(jwtToken.getLoginId(), false);
        if (user == null) {
            throw RequestException.fail("用户不存在");
        }
        //获取菜单/权限信息
        List<SysMenus> allPer = userRolesRegexMenu(roleService.findAllRoleByUserId(user.getId(), true));
        user.setMenus(allPer);
        return user;
    }

    @Override
    public List<String> getAllPermissionTag() {
        JwtToken jwtToken = super.getJwtToken();
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq(SysUser.LOGIN_ID, jwtToken.getLoginId());
        SysUser user = this.getOne(wrapper);
        if (user == null) {
            throw RequestException.fail("用户不存在");
        }
        List<SysRole> allRoleByUserId = roleService.findAllRoleByUserId(user.getId(), true);
        List<String> permissions = new LinkedList<>();
        for (SysRole sysRole : allRoleByUserId) {
            if (sysRole.getMenus() != null && sysRole.getMenus().size() > 0) {
                sysRole.getMenus().forEach(s -> permissions.add(s.getPermission()));
            }
        }
        return permissions;
    }

    @Override
    public List<SysMenus> userRolesRegexMenu(List<SysRole> roles) {
        if (roles != null && roles.size() > 0) {
            Map<String, SysMenus> menuMap = new LinkedHashMap<>();
            roles.forEach(role -> {
                if (role.getMenus() != null && role.getMenus().size() > 0) {
                    role.getMenus().forEach(menu -> //含有则不覆盖
                            menuMap.putIfAbsent(menu.getId(), menu));
                }
            });
            Map<String, SysMenus> cacheMap = new ConcurrentHashMap<>(16);
            List<SysMenus> menuList = new CopyOnWriteArrayList<>();

            return menuList;
        }
        return null;
    }

    @Override
    public IPage<SysUser> getPage(Page<SysUser> page, SysUser user) {
        QueryWrapper<SysUser> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc(SysUser.CREATE_DATE);
        IPage<SysUser> userPage = super.listPage(new Page<>(page.getCurrent(), page.getSize()), user);

        userPage.getRecords().forEach(v -> {
            //查找匹配所有用户的角色
            v.setRoles(roleService.findAllRoleByUserId(v.getId(), false));
        });
        return userPage;
    }

    @Override
    public void remove(String userId) {
        SysUser user = this.getById(userId);
        if (user == null) {
            throw RequestException.fail("用户不存在！");
        }
        SysUser sysUser = new SysUser();
        BeanUtils.copyProperties(SecurityUtils.getSubject().getPrincipal(), sysUser);
        if (user.getLoginId().equals(sysUser.getLoginId())) {
            throw RequestException.fail("不能删除自己的账户！");
        }
        try {
            this.remove(userId);
            shiroService.clearAuthByUserId(userId, true, true);
        } catch (Exception e) {
            throw RequestException.fail("删除失败", e);
        }
    }

    @Override
    public void add(SysUser user) {
        SysUser findUser = this.findUserByLoginId(user.getLoginId(), false);
        if (findUser != null) {
            throw RequestException.fail(
                    String.format("已经存在用户名为 %s 的用户", user.getLoginId()));
        }
        try {
            user.setCreateDate(DateUtils.getCurrentTime());
            user.setPassword(MD5EncryptUtil.encrypt(String.valueOf(findUser.getPassword()) + findUser.getLoginId()));
            this.add(user);
        } catch (Exception e) {
            throw RequestException.fail("添加用户失败", e);
        }
    }

    @Override
    public void update(SysUser user) {
        if (this.getById(user.getId()) == null) {
            throw RequestException.fail(
                    String.format("更新失败，不存在ID为 %s 的用户", user.getId()));
        }
        SysUser findUser = this.getOne(new QueryWrapper<SysUser>()
                .eq(SysUser.LOGIN_ID, user.getLoginId()).ne(SysUser.ID, user.getId()));
        if (findUser != null) {
            throw RequestException.fail(
                    String.format("更新失败，已经存在用户名为 %s 的用户", user.getLoginId()));
        }
        try {
            this.updateById(user);
            shiroService.clearAuthByUserId(user.getId(), true, false);
        } catch (RequestException e) {
            throw RequestException.fail(e.getMsg(), e);
        } catch (Exception e) {
            throw RequestException.fail("用户信息更新失败", e);
        }
    }

    @Override
    public void resetPassword(ResetPasswordDto resetPasswordDTO) {
        SysUser user = this.getById(resetPasswordDTO.getUserId().trim());
        if (user == null) {
            throw RequestException.fail(String.format("不存在ID为 %s 的用户", resetPasswordDTO.getUserId()));
        }
        String password = MD5EncryptUtil.encrypt(String.valueOf(resetPasswordDTO.getPassword()) + user.getLoginId());
        user.setPassword(password);
        try {
            this.updateById(user);
            shiroService.clearAuthByUserId(user.getId(), true, true);
        } catch (Exception e) {
            throw RequestException.fail(String.format("ID为 %s 的用户密码重置失败", resetPasswordDTO.getUserId()), e);
        }
    }
}
