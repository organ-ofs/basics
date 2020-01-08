package com.ofs.sys.serv.service;

import com.ofs.sys.serv.entity.SysRole;
import com.ofs.web.base.BaseService;

import java.util.List;

public interface SysRoleService extends BaseService<SysRole> {

    /**
     * 获取指定ID用户的所有角色（并附带查询所有的角色的权限）
     *
     * @param uid 用户ID
     * @param hasResource
     * @return 角色集合
     */
    List<SysRole> findAllRoleByUserId(String uid, Boolean hasResource);

    /**
     * 更新缓存
     *
     * @param roleId  角色
     * @param author 是否清空授权信息
     * @param out    是否清空session
     */
    void updateCache(String roleId, Boolean author, Boolean out);
}
