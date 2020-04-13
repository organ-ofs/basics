package com.ofs.sys.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.ofs.sys.web.entity.SysMenus;
import com.ofs.sys.web.mapper.SysMenusMapper;
import com.ofs.sys.web.service.SysMenusService;
import com.ofs.web.base.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author gaoly
 * @version 2019/4/28/9:57
 */
@Service
public class SysMenusServiceImpl extends BaseServiceImpl<SysMenusMapper, SysMenus> implements SysMenusService {


    @Autowired
    SysMenusMapper mapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void remove(String id) throws Exception {
        SysMenus menus = super.getById(id);
        if (menus != null) {
            super.remove(id);
        }

        //子项
        QueryWrapper<SysMenus> wrapper = new QueryWrapper<>();
        wrapper.eq(SysMenus.PARENT_ID, id);
        List<SysMenus> menusList = baseMapper.selectList(wrapper);
        if (CollectionUtils.isNotEmpty(menusList)) {
            List<String> ids = menusList.stream().map(SysMenus::getId).collect(Collectors.toList());
            super.removeByIds(ids);
        }
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<SysMenus> getTree(SysMenus menus) {
        menus.setParentId("0");
        List<SysMenus> list = mapper.getList(menus);
        this.getMenus(list, menus);
        return list;
    }

    /**
     * 递归获取资源
     *
     * @param list
     * @return
     */
    private List<SysMenus> getMenus(List<SysMenus> list, SysMenus menu) {
        if (CollectionUtils.isNotEmpty(list)) {
            list.stream().forEach(e -> {
                if (e.getLeaf()) {
                    menu.setParentId(e.getId());
                    List<SysMenus> menusList = mapper.getList(menu);
                    e.setChildren(menusList);
                    this.getMenus(menusList, menu);
                }
            });
        }
        return list;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<SysMenus> getTreeByRole(String roleId) {
        SysMenus menus = SysMenus.builder()
                .roleId(roleId)
                .parentId("0")
                .build();
        List<SysMenus> list = mapper.getListByRole(menus);
        this.getMenusByRole(list, menus);
        return list;
    }


    /**
     * 递归获取资源
     *
     * @param list
     * @return
     */
    private List<SysMenus> getMenusByRole(List<SysMenus> list, SysMenus menus) {
        if (CollectionUtils.isNotEmpty(list)) {
            list.stream().forEach(e -> {
                if (e.getLeaf()) {
                    menus.setParentId(e.getId());
                    List<SysMenus> menuss = mapper.getListByRole(menus);
                    e.setChildren(menuss);
                    this.getMenus(menuss, menus);
                }
            });
        }
        return list;
    }

}
