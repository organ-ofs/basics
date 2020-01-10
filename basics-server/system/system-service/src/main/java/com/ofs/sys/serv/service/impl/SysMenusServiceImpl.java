package com.ofs.sys.serv.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.ofs.sys.serv.entity.SysMenus;
import com.ofs.sys.serv.entity.SysResource;
import com.ofs.sys.serv.mapper.SysMenusMapper;
import com.ofs.sys.serv.message.Dict;
import com.ofs.sys.serv.service.SysMenusService;
import com.ofs.sys.serv.service.SysResourceService;
import com.ofs.utils.IdentifierUtils;
import com.ofs.web.base.impl.BaseServiceImpl;
import com.ofs.web.exception.RequestException;
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
    private SysResourceService resourceService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(SysMenus entity) throws Exception {

        SysResource resource = SysResource.builder()
                .parentId(entity.getParentId())
                .name(entity.getName())
                .url(entity.getPath())
                .type(Dict.DictEnum.MENU.getCode())
                .build();
        resource.setId(IdentifierUtils.nextUuid());
        boolean b = resourceService.save(resource);
        if (!b) {
            throw RequestException.fail("操作失败");
        }
        entity.setId(IdentifierUtils.nextUuid());
        b = super.save(entity);
        if (!b) {
            throw RequestException.fail("操作失败");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(SysMenus entity) throws Exception {
        boolean b = super.updateById(entity);
        if (!b) {
            throw RequestException.fail("操作失败");
        }
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void remove(String id) throws Exception {
        SysMenus menus = super.getById(id);
        if (menus != null) {
            super.remove(id);
            String resourceId = menus.getResourceId();
            resourceService.remove(resourceId);
        }

        //子项
        QueryWrapper<SysMenus> wrapper = new QueryWrapper<>();
        wrapper.eq(SysMenus.PARENT_ID, id);
        List<SysMenus> menusList = baseMapper.selectList(wrapper);
        if (CollectionUtils.isNotEmpty(menusList)) {
            List<String> ids = menusList.stream().map(SysMenus::getId).collect(Collectors.toList());
            List<String> resourceIds = menusList.stream().map(SysMenus::getResourceId).collect(Collectors.toList());
            super.removeByIds(ids);
            resourceService.removes(resourceIds);
        }
    }
}
