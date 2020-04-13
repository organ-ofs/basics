package com.ofs.sys.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ofs.sys.web.entity.SysResource;
import com.ofs.sys.web.entity.SysRolePermission;
import com.ofs.sys.web.mapper.SysResourceMapper;
import com.ofs.sys.web.service.SysResourceService;
import com.ofs.sys.web.service.SysRolePermissionService;
import com.ofs.web.base.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 菜单业务操作接口实现
 *
 * @author gaoly
 */
@Service
public class SysResourceServiceImpl extends BaseServiceImpl<SysResourceMapper, SysResource> implements SysResourceService {

    @Autowired
    SysResourceMapper mapper;

    @Autowired
    SysRolePermissionService roleResourceService;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removes(List<String> ids) {
        mapper.deleteChildren(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void remove(String id) {
        SysResource resource = mapper.selectById(id);
        mapper.deleteById(id);
        QueryWrapper<SysRolePermission> rrWrapper = new QueryWrapper<>();
        rrWrapper.eq(SysRolePermission.PERMISSION, resource.getPermission());
        roleResourceService.remove(rrWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<SysResource> getListByRole(SysResource resource) {
        List<SysResource> list = mapper.getListByRole(resource);
        return list;
    }
}

