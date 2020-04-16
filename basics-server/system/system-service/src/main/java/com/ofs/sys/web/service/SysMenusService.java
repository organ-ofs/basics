package com.ofs.sys.web.service;

import com.ofs.sys.web.entity.SysMenus;
import com.ofs.web.base.BaseService;

import java.util.List;

/**
 * @author gaoly
 * @version 2019/4/28/9:56
 */
public interface SysMenusService extends BaseService<SysMenus> {

    /**
     * 获取菜单树
     *
     * @param menus
     * @return
     */
    List<SysMenus> getTree(SysMenus menus);

    /**
     * 获取菜单树
     *
     * @param roleIds
     * @return
     */
    List<SysMenus> getTreeByRole(List<String> roleIds);

}
