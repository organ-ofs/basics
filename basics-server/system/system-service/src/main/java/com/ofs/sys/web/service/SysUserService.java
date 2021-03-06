package com.ofs.sys.web.service;


import com.ofs.sys.web.dto.ResetPasswordDto;
import com.ofs.sys.web.entity.SysMenus;
import com.ofs.sys.web.entity.SysUser;
import com.ofs.web.base.BaseService;

import java.util.List;

/**
 * @author gaoly
 */
public interface SysUserService extends BaseService<SysUser> {

    /**
     * 根据用户名查找用户
     *
     * @param account 登陆名
     * @return User
     */
    SysUser getUserByAccount(String account);

    /**
     * 根据ID查找用户
     *
     * @param id ID
     * @return User
     */
    SysUser getUserById(String id);


    /**
     * 获取当前登录用户信息
     *
     * @return SysUser
     */
    SysUser getCurrentUser();

    /**
     * 获取当前登录用户所有的权限标示
     *
     * @return SysUser
     */
    List<String> getAllPermissionTag(String account);

    /**
     * 获取用户的菜单树
     *
     * @return menus
     */
    List<SysMenus> getMenus(String account);

    /**
     * 重置用户密码
     *
     * @param user
     */
    void resetPassword(ResetPasswordDto user);
}
