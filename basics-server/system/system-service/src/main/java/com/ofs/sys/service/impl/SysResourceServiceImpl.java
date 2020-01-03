package com.ofs.sys.service.impl;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.ofs.sys.entity.SysResource;
import com.ofs.sys.mapper.SysResourceMapper;
import com.ofs.sys.service.SysResourceService;
import com.ofs.web.base.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 菜单业务操作接口实现
 *
 * @author gaoly
 */
@Service
public class SysResourceServiceImpl extends BaseServiceImpl<SysResourceMapper, SysResource> implements SysResourceService {

    @Autowired
    SysResourceMapper mapper;


    @Override
    public List<SysResource> getListTree(SysResource resource) {
        resource.setParentId("0");
        List<SysResource> list = mapper.getList(resource);
        this.getResource(list, resource);
        return list;
    }

    /**
     * 递归获取资源
     *
     * @param list
     * @return
     */
    private List<SysResource> getResource(List<SysResource> list, SysResource resource) {
        list.forEach(e -> {
            if (e.getLeaf()) {
                resource.setParentId(e.getId());
                List<SysResource> resources = mapper.getList(resource);
                this.getResource(resources, resource);
            } else {
                resource.setParentId(e.getId());
                List<SysResource> resources = mapper.getList(resource);
                e.setChildren(resources);
            }
        });
        return list;
    }

    @Override
    public List<SysResource> getListTreeByRole(String roleId) {
        SysResource resource = SysResource.builder()
                .roleId(roleId)
                .parentId("0")
                .build();
        List<SysResource> list = mapper.getListByRole(resource);
        this.getResourceByRole(list, resource);
        return list;
    }


    /**
     * 递归获取资源
     *
     * @param list
     * @return
     */
    private List<SysResource> getResourceByRole(List<SysResource> list, SysResource resource) {
        list.forEach(e -> {
            if (e.getLeaf()) {
                resource.setParentId(e.getId());
                List<SysResource> resources = mapper.getListByRole(resource);
                this.getResource(resources, resource);
            } else {
                resource.setParentId(e.getId());
                List<SysResource> resources = mapper.getListByRole(resource);
                e.setChildren(resources);
            }
        });
        return list;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removes(List<String> ids) {
        List<String> buttons = new ArrayList<>();
        List<SysResource> list = mapper.getList(SysResource.builder().ids(ids).build());
        if (CollectionUtils.isNotEmpty(list)) {
            list.forEach(menu -> {
                if (menu.getLeaf()) {
                    List<SysResource> childList = mapper.getList(SysResource.builder().parentId(menu.getId()).build());
                    if (CollectionUtils.isNotEmpty(childList)) {
                        buttons.addAll(childList.stream().map(SysResource::getId).collect(Collectors.toList()));
                    }
                    mapper.deleteChildren(ids);
                } else {
                    buttons.add(menu.getId());
                }
            });
            mapper.deleteChildren(buttons);
        }
    }
}

