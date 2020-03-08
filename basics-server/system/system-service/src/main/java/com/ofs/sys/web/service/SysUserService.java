package com.ofs.sys.web.service;


import com.ofs.sys.web.dto.ResetPasswordDto;
import com.ofs.sys.web.entity.SysMenus;
import com.ofs.sys.web.entity.SysRole;
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
    SysUser getUserByAccount(String account, boolean hasMenu);

    /**
     * 根据ID查找用户
     *
     * @param id ID
     * @return User
     */
    SysUser getUserById(String id, boolean hasMenu);


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
     * 用户角色资源匹配
     *
     * @param roles 用户角色集
     * @return 资源集合
     */
    List<SysMenus> userRolesRegexMenu(List<SysRole> roles);

    /**
     * 重置用户密码
     *
     * @param user
     */
    void resetPassword(ResetPasswordDto user);
}
