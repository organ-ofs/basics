package com.ofs.sys.web.service;

import com.ofs.sys.web.entity.SysResource;
import com.ofs.web.base.BaseService;

import java.util.List;

/**
 * 菜单业务操作接口
 *
 * @author gaoly
 */
public interface SysResourceService extends BaseService<SysResource> {

    /**
     * 获取资源list by role
     *
     * @param resource
     * @return
     */
    List<SysResource> getListByRole(SysResource resource);

}

