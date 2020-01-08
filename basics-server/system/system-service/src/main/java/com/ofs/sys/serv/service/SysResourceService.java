package com.ofs.sys.serv.service;

import com.ofs.sys.serv.entity.SysResource;
import com.ofs.web.base.BaseService;

import java.util.List;

/**
 * 菜单业务操作接口
 *
 * @author gaoly
 */
public interface SysResourceService extends BaseService<SysResource> {

    /**
     * 根据角色Id获取资源树
     *
     * @param roleId
     * @return
     */
    List<SysResource> getTreeByRole(String roleId);

    /**
     * 获取资源树
     *
     * @param resource
     * @return
     */
    List<SysResource> getTree(SysResource resource);

    /**
     * 获取资源list by role
     *
     * @param resource
     * @return
     */
    List<SysResource> getListByRole(SysResource resource);

}

