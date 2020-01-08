package com.ofs.sys.serv.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ofs.sys.core.global.ShiroService;
import com.ofs.sys.serv.entity.SysResource;
import com.ofs.sys.serv.entity.SysRole;
import com.ofs.sys.serv.entity.SysUserRole;
import com.ofs.sys.serv.mapper.SysRoleMapper;
import com.ofs.sys.serv.service.SysResourceService;
import com.ofs.sys.serv.service.SysRoleService;
import com.ofs.sys.serv.service.SysUserRoleService;
import com.ofs.web.base.impl.BaseServiceImpl;
import com.ofs.web.bean.SystemCode;
import com.ofs.web.exception.RequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SysRoleServiceImpl extends BaseServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

    @Autowired
    private SysUserRoleService userRoleService;

    @Autowired
    private SysResourceService resourceService;

    @Autowired
    private ShiroService shiroService;

    @Override
    public List<SysRole> findAllRoleByUserId(String uid, Boolean hasResource) {
        QueryWrapper<SysUserRole> wrapper = new QueryWrapper();
        wrapper.eq(SysUserRole.USER_ID, uid);
        List<SysUserRole> userRoles = userRoleService.list(wrapper);
        List<SysRole> roles = new ArrayList<>();
        userRoles.forEach(v -> {
            SysRole role = this.getById(v.getRoleId());
            if (role != null) {
                if (hasResource) {
                    SysResource resource = SysResource.builder().roleId(role.getId()).build();
                    List<SysResource> permissions = resourceService.getListByRole(resource);
                    role.setResources(permissions);
                }
            }
            roles.add(role);
        });
        return roles;
    }

    @Override
    public IPage<SysRole> listPage(Page<SysRole> page, SysRole role) {
        IPage<SysRole> rolePage = super.listPage(new Page<SysRole>(page.getCurrent(),
                page.getSize()), role);
        return rolePage;
    }

    @Override
    public void removes(List<String> ids) {
        ids.forEach(id -> {

            try {
                this.remove(id);
                this.updateCache(id, true, false);
            } catch (DataIntegrityViolationException e) {
                throw RequestException.fail(e.getMessage());
            } catch (Exception e) {
                throw RequestException.fail("角色删除失败！", e);
            }
        });

    }

    @Override
    public void remove(String id) throws Exception {
        try {
            SysRole role = super.getById(id);
            if (role == null) {
                throw RequestException.fail("删除失败，角色不存在！");
            }
            this.removeById(id);
        } catch (Exception e) {
            throw new RequestException(SystemCode.FAIL.code, "删除失败", e);
        }
    }
    @Override
    public void update(SysRole role) {
        if (this.getById(role.getId()) == null) {
            throw RequestException.fail("角色不存在！");
        }
        try {
            this.updateById(role);

            this.updateCache(role.getId(), true, false);
        } catch (Exception e) {
            throw RequestException.fail("角色更新失败！", e);
        }

    }

    @Override
    public void add(SysRole role) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq(SysRole.NAME, role.getName());
        SysRole roleOld = this.getOne(wrapper);
        if (roleOld != null) {
            throw RequestException.fail(
                    String.format("已经存在名称为 %s 的角色", role.getName()));
        }
        try {
            super.add(role);
        } catch (Exception e) {
            throw RequestException.fail("添加失败", e);
        }
    }

    @Override
    public void updateCache(String roleId, Boolean author, Boolean out) {
        QueryWrapper<SysUserRole> wrapper = new QueryWrapper();
        wrapper.eq("rid", roleId);
        wrapper.groupBy("uid");
        List<SysUserRole> sysUserRoles = userRoleService.list(wrapper);
        List<String> userIdList = new ArrayList<>();
        if (sysUserRoles != null && sysUserRoles.size() > 0) {
            sysUserRoles.forEach(v -> userIdList.add(v.getUserId()));
        }
        shiroService.clearAuthByUserIdCollection(userIdList, author, out);
    }
}