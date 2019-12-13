package com.ofs.sys.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.ofs.sys.entity.SysMenus;
import com.ofs.sys.service.SysMenusService;
import com.ofs.web.base.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Licoy
 * @version 2018/4/28/9:57
 */
@Service
public class SysMenusServiceImpl extends BaseServiceImpl<SysMenus> implements SysMenusService {

    @Override
    public Page<SysMenus> list(Page<SysMenus> page, SysMenus menus) {
        EntityWrapper<SysMenus> wrapper = new EntityWrapper<>();
        wrapper.orderBy(true, "create_date");
        return this.selectPage(page, wrapper);
    }

    @Override
    public List<SysMenus> list(SysMenus menus) {
        EntityWrapper<SysMenus> wrapper = new EntityWrapper<>();
        wrapper.orderBy(true, "create_date");
        return this.selectList(wrapper);
    }

}
