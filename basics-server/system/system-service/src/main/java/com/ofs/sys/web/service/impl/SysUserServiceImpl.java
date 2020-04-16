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
import com.ofs.sys.web.service.SysMenusService;
import com.ofs.sys.web.service.SysResourceService;
import com.ofs.sys.web.service.SysRoleService;
import com.ofs.sys.web.service.SysUserService;
import com.ofs.utils.encrypt.utils.MD5EncryptUtil;
import com.ofs.web.auth.service.ShiroService;
import com.ofs.web.base.BaseServiceImpl;
import com.ofs.web.exception.RequestException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SysUserServiceImpl extends BaseServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    @Autowired
    private SysRoleService roleService;

    @Autowired
    private SysResourceService resourceService;

    @Autowired
    private SysMenusService menusService;

    @Autowired
    private ShiroService shiroService;

    @Override
    public SysUser getUserByAccount(String account) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq(SysUser.ACCOUNT, account);
        SysUser user = this.getOne(wrapper);
        if (user == null) {
            return null;
        }
        user.setRoles(roleService.getAllRoleByUserId(user.getId()));
        return user;
    }

    @Override
    public SysUser getUserById(String id) {
        SysUser user = this.getById(id);
        if (user == null) {
            return null;
        }
        user.setRoles(roleService.getAllRoleByUserId(user.getId()));
        return user;
    }

    @Override
    public SysUser getCurrentUser() {
        SysUser user = this.getUserByAccount(super.getAccount());
        if (user == null) {
            throw RequestException.fail("用户不存在");
        }
        //获取菜单/权限信息
        if (CollectionUtils.isNotEmpty(user.getRoles())) {
            List<String> roleIds = user.getRoles().stream().map(SysRole::getId).collect(Collectors.toList());
            List<SysMenus> allPer = menusService.getTreeByRole(roleIds);
            user.setMenus(allPer);
        }
        return user;
    }

    @Override
    public List<String> getAllPermissionTag(String account) {
        SysUser user = this.getUserByAccount(super.getAccount());
        if (user == null) {
            throw RequestException.fail("用户不存在");
        }
        List<SysRole> roles = roleService.getAllRoleByUserId(user.getId());
        List<String> permissions = new LinkedList<>();

        if (CollectionUtils.isNotEmpty(roles)) {
            List<String> roleIds = roles.stream().map(SysRole::getId).collect(Collectors.toList());
            SysResource resource = SysResource.builder().roleIds(roleIds).build();
            List<SysResource> resources = resourceService.getListByRole(resource);
            if (CollectionUtils.isNotEmpty(resources)) {
                List<String> list = resources.stream().map(SysResource::getPermission).collect(Collectors.toList());
                permissions.addAll(list);
            }
        }
        return permissions;
    }

    @Override
    public List<SysMenus> getMenus(String account) {
        SysUser user = this.getUserByAccount(super.getAccount());
        if (user == null) {
            throw RequestException.fail("用户不存在");
        }
        List<SysRole> roles = roleService.getAllRoleByUserId(user.getId());
        if (CollectionUtils.isNotEmpty(roles)) {
            List<String> roleIds = roles.stream().map(SysRole::getId).collect(Collectors.toList());
            return menusService.getTreeByRole(roleIds);
        }
        return new ArrayList<>();
    }

    @Override
    public IPage<SysUser> listPage(Page<SysUser> page, SysUser user) {
        QueryWrapper<SysUser> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc(SysUser.CREATE_DATE);
        IPage<SysUser> userPage = super.listPage(new Page<>(page.getCurrent(), page.getSize()), user);
        userPage.getRecords().forEach(v -> {
            //查找匹配所有用户的角色
            v.setRoles(roleService.getAllRoleByUserId(v.getId()));
        });
        return userPage;
    }

    @Override
    public void remove(String userId) throws Exception {
        SysUser user = this.getById(userId);
        if (user == null) {
            throw RequestException.fail("用户不存在！");
        }
        SysUser sysUser = new SysUser();
        BeanUtils.copyProperties(SecurityUtils.getSubject().getPrincipal(), sysUser);
        if (user.getAccount().equals(sysUser.getAccount())) {
            throw RequestException.fail("不能删除自己的账户！");
        }
        try {
            super.removeById(userId);
            shiroService.clearAuthByUserId(userId, true, true);
        } catch (Exception e) {
            throw RequestException.fail("删除失败", e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean add(SysUser user) {
        SysUser getUser = this.getUserByAccount(user.getAccount());
        if (getUser != null) {
            throw RequestException.fail(
                    String.format("已经存在用户名为 %s 的用户", user.getAccount()));
        }
        try {
            user.setPassword(MD5EncryptUtil.encrypt(user.getPassword() + user.getAccount()));
            super.save(user);
        } catch (Exception e) {
            throw RequestException.fail("添加用户失败", e);
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
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
        SysUser user = this.getUserByAccount(resetPasswordDTO.getAccount().trim());
        if (user == null) {
            throw RequestException.fail(String.format("不存在的用户: %s", resetPasswordDTO.getAccount()));
        }
        String password = MD5EncryptUtil.encrypt(String.valueOf(resetPasswordDTO.getPassword()) + user.getAccount());
        user.setPassword(password);
        try {
            this.updateById(user);
            shiroService.clearAuthByUserId(user.getId(), true, true);
        } catch (Exception e) {
            throw RequestException.fail(String.format("用户密码重置失败: %s", resetPasswordDTO.getAccount()), e);
        }
    }

}
