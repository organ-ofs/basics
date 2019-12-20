package com.ofs.sys.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ofs.sys.core.global.ShiroService;
import com.ofs.sys.entity.SysResource;
import com.ofs.sys.entity.SysRole;
import com.ofs.sys.entity.SysUserRole;
import com.ofs.sys.mapper.SysRoleMapper;
import com.ofs.sys.service.SysMenusService;
import com.ofs.sys.service.SysRoleService;
import com.ofs.sys.service.SysUserRoleService;
import com.ofs.web.base.impl.BaseServiceImpl;
import com.ofs.web.exception.RequestException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class SysRoleServiceImpl extends BaseServiceImpl<SysRoleMapper,SysRole> implements SysRoleService {

    @Autowired
    private SysMenusService roleMenusService;

    @Autowired
    private SysUserRoleService userRoleService;

    @Autowired
    private ShiroService shiroService;

    @Override
    public List<SysRole> findAllRoleByUserId(String uid, Boolean hasResource) {
        List<SysUserRole> userRoles = userRoleService.selectList(new EntityWrapper<SysUserRole>().eq(SysUserRole.USER_ID, uid));
        List<SysRole> roles = new ArrayList<>();
        userRoles.forEach(v -> {
            SysRole role = this.selectById(v.getRoleId());
            if (role != null) {
                if (hasResource) {
                    //添加资源列表
//                    List<SysResource> permissions = roleMenusService.findAllResourceByRoleId(role.getId());
//                    role.setResources(permissions);
                }
            }
            roles.add(role);
        });
        return roles;
    }

    @Override
    public Page<SysRole> list(Page<SysRole> page,SysRole role) {
        EntityWrapper<SysRole> wrapper = new EntityWrapper<>();
        wrapper.orderBy(SysRole.CODE, true);
        Page<SysRole> rolePage = this.selectPage(new Page<SysRole>(page.getCurrent(),
                page.getSize()), wrapper);
            if (rolePage.getRecords() != null) {
//                rolePage.getRecords().forEach(v->
                //添加资源列表
//                        v.setResources(roleMenusService.findAllResourceByRoleId(v.getId())));
            }
        return rolePage;
    }

    @Override
    public void removes(List<String> ids) {
        ids.forEach(id->{
            SysRole role = super.selectById(id);
            if (id == null) {
                throw RequestException.fail("角色不存在！");
            }
            try {
                this.deleteById(id);
                this.updateCache(role, true, false);
            } catch (DataIntegrityViolationException e) {
                throw RequestException.fail(
                        String.format("请先解除角色为 %s 角色的全部用户！", role.getName()), e);
            } catch (Exception e) {
                throw RequestException.fail("角色删除失败！", e);
            }
        });

    }

    @Override
    public void update(SysRole role) {
        if (this.selectById(role.getId()) == null) {
            throw RequestException.fail("角色不存在！");
        }
        try {
            this.updateById(role);

            this.updateCache(role, true, false);
        } catch (Exception e) {
            throw RequestException.fail("角色更新失败！", e);
        }

    }

    @Override
    public void add(SysRole addDTO) {
        SysRole role = this.selectOne(new EntityWrapper<SysRole>().eq("name", addDTO.getName()));
        if (role != null) {
            throw RequestException.fail(
                    String.format("已经存在名称为 %s 的角色", addDTO.getName()));
        }
        role = new SysRole();
        BeanUtils.copyProperties(addDTO, role);
        try {
            this.insert(role);
        } catch (Exception e) {
            throw RequestException.fail("添加失败", e);
        }
    }

    @Override
    public void updateCache(SysRole role, Boolean author, Boolean out) {
        List<SysUserRole> sysUserRoles = userRoleService.selectList(new EntityWrapper<SysUserRole>()
                .eq("rid", role.getId())
                .groupBy("uid"));
        List<String> userIdList = new ArrayList<>();
        if (sysUserRoles != null && sysUserRoles.size() > 0) {
            sysUserRoles.forEach(v -> userIdList.add(v.getUserId()));
        }
        shiroService.clearAuthByUserIdCollection(userIdList, author, out);
    }
}
