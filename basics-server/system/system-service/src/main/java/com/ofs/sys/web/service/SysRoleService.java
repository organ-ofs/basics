package com.ofs.sys.web.service;

import com.ofs.sys.web.entity.SysRole;
import com.ofs.web.base.BaseService;

import java.util.List;

public interface SysRoleService extends BaseService<SysRole> {

    /**
     * 获取指定ID用户的所有角色
     *
     * @param uid         用户ID
     * @return 角色集合
     */
    List<SysRole> getAllRoleByUserId(String uid);

    /**
     * 更新缓存
     *
     * @param roleId 角色
     * @param author 是否清空授权信息
     * @param out    是否清空session
     */
    void updateCache(String roleId, Boolean author, Boolean out);
}
