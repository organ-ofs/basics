package com.ofs.sys.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ofs.sys.web.dto.ResetPasswordDto;
import com.ofs.sys.web.entity.SysMenus;
import com.ofs.sys.web.entity.SysResource;
import com.ofs.sys.web.entity.SysRole;
import com.ofs.sys.web.entity.SysUser;
import com.ofs.sys.web.mapper.SysUserMapper;
import com.ofs.sys.web.service.SysRoleService;
import com.ofs.sys.web.service.SysUserRoleService;
import com.ofs.sys.web.service.SysUserService;
import com.ofs.utils.DateUtils;
import com.ofs.utils.encrypt.utils.MD5EncryptUtil;
import com.ofs.web.auth.service.ShiroService;
import com.ofs.web.base.bean.SystemCode;
import com.ofs.web.base.impl.BaseServiceImpl;
import com.ofs.web.exception.RequestException;
import com.ofs.web.jwt.JwtToken;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class SysUserServiceImpl extends BaseServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    @Autowired
    private SysRoleService roleService;

    @Autowired
    private SysUserRoleService userRoleService;

    @Autowired
    private ShiroService shiroService;

    @Override
    public SysUser getUserByAccount(String account, boolean hasMenu) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq(SysUser.ACCOUNT, account);
        SysUser user = this.getOne(wrapper);
        if (user == null) {
            return null;
        }
        user.setRoles(roleService.getAllRoleByUserId(user.getId(), hasMenu));
        return user;
    }

    @Override
    public SysUser getUserById(String id, boolean hasMenu) {
        SysUser user = this.getById(id);
        if (user == null) {
            return null;
        }
        user.setRoles(roleService.getAllRoleByUserId(user.getId(), hasMenu));
        return user;
    }

    @Override
    public SysUser getCurrentUser() {
        Subject subject = SecurityUtils.getSubject();
        if (!subject.isAuthenticated()) {
            throw new RequestException(SystemCode.NOT_SING_IN);
        }
        JwtToken jwtToken = super.getJwtToken();
        SysUser user = this.getUserByAccount(jwtToken.getAccount(), false);
        if (user == null) {
            throw RequestException.fail("用户不存在");
        }
        //获取菜单/权限信息
        List<SysMenus> allPer = userRolesRegexMenu(roleService.getAllRoleByUserId(user.getId(), true));
        user.setMenus(allPer);
        return user;
    }

    @Override
    public List<String> getAllPermissionTag(String account) {
        JwtToken jwtToken = super.getJwtToken();
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq(SysUser.ACCOUNT, jwtToken.getAccount());
        SysUser user = this.getOne(wrapper);
        if (user == null) {
            throw RequestException.fail("用户不存在");
        }
        List<SysRole> allRoleByUserId = roleService.getAllRoleByUserId(user.getId(), true);
        List<String> permissions = new LinkedList<>();
        for (SysRole sysRole : allRoleByUserId) {
            if (sysRole.getResources() != null && sysRole.getResources().size() > 0) {
                sysRole.getResources().forEach(s -> permissions.add(s.getPermission()));
            }
        }
        return permissions;
    }

    @Override
    public List<SysMenus> userRolesRegexMenu(List<SysRole> roles) {
        if (roles != null && roles.size() > 0) {
            Map<String, SysResource> menuMap = new LinkedHashMap<>();
            roles.forEach(role -> {
                if (role.getResources() != null && role.getResources().size() > 0) {
                    role.getResources().forEach(menu -> //含有则不覆盖
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
    public IPage<SysUser> listPage(Page<SysUser> page, SysUser user) {
        QueryWrapper<SysUser> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc(SysUser.CREATE_DATE);
        IPage<SysUser> userPage = super.listPage(new Page<>(page.getCurrent(), page.getSize()), user);

        userPage.getRecords().forEach(v -> {
            //查找匹配所有用户的角色
            v.setRoles(roleService.getAllRoleByUserId(v.getId(), false));
        });
        return userPage;
    }

    @Override
    public void remove(String userId) {
        SysUser user = this.getById(userId);
        if (user == null) {
            throw RequestException.fail("用户不存在！");
        }
//        SysUser sysUser = new SysUser();
//        BeanUtils.copyProperties(SecurityUtils.getSubject().getPrincipal(), sysUser);
//        if (user.getAccount().equals(sysUser.getAccount())) {
//            throw RequestException.fail("不能删除自己的账户！");
//        }
        try {
            super.removeById(userId);
            shiroService.clearAuthByUserId(userId, true, true);
        } catch (Exception e) {
            throw RequestException.fail("删除失败", e);
        }
    }

    @Override
    public boolean add(SysUser user) {
        SysUser getUser = this.getUserByAccount(user.getAccount(), false);
        if (getUser != null) {
            throw RequestException.fail(
                    String.format("已经存在用户名为 %s 的用户", user.getAccount()));
        }
        try {
            user.setCreateDate(DateUtils.getCurrentTime());
            user.setPassword(MD5EncryptUtil.encrypt(user.getPassword() + user.getAccount()));
            super.save(user);
        } catch (Exception e) {
            throw RequestException.fail("添加用户失败", e);
        }
        return true;
    }

    @Override
    public void update(SysUser user) {
        if (this.getById(user.getId()) == null) {
            throw RequestException.fail(
                    String.format("更新失败，不存在ID为 %s 的用户", user.getId()));
        }
        SysUser getUser = this.getOne(new QueryWrapper<SysUser>()
                .eq(SysUser.ACCOUNT, user.getAccount()).ne(SysUser.ID, user.getId()));
        if (getUser != null) {
            throw RequestException.fail(
                    String.format("更新失败，已经存在用户名为 %s 的用户", user.getAccount()));
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
        SysUser user = this.getById(resetPasswordDTO.getAccount().trim());
        if (user == null) {
            throw RequestException.fail(String.format("不存在ID为 %s 的用户", resetPasswordDTO.getAccount()));
        }
        String password = MD5EncryptUtil.encrypt(String.valueOf(resetPasswordDTO.getPassword()) + user.getAccount());
        user.setPassword(password);
        try {
            this.updateById(user);
            shiroService.clearAuthByUserId(user.getId(), true, true);
        } catch (Exception e) {
            throw RequestException.fail(String.format("ID为 %s 的用户密码重置失败", resetPasswordDTO.getAccount()), e);
        }
    }
}
