package com.ofs.sys.service;


import com.baomidou.mybatisplus.plugins.Page;
import com.ofs.sys.dto.ResetPasswordDto;
import com.ofs.sys.dto.SignInDto;
import com.ofs.sys.entity.SysMenus;
import com.ofs.sys.entity.SysRole;
import com.ofs.sys.entity.SysUser;
import com.ofs.web.base.BaseService;

import java.util.List;

public interface SysUserService extends BaseService<SysUser> {

    /**
     * 根据用户名查找用户
     *
     * @param loginId 登陆名
     * @return User
     */
    SysUser findUserByLoginId(String loginId, boolean hasMenu);

    /**
     * 根据ID查找用户
     *
     * @param id ID
     * @return User
     */
    SysUser findUserById(String id, boolean hasMenu);

    /**
     * 用户登录操作
     *
     * @param signInDto 登录信息
     */
    void signIn(SignInDto signInDto);

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
    List<String> getAllPermissionTag();

    /**
     * 用户角色资源匹配
     *
     * @param roles 用户角色集
     * @return 资源集合
     */
    List<SysMenus> userRolesRegexMenu(List<SysRole> roles);


    /**
     * 获取所有用户（分页）
     *
     * @param user 过滤条件
     * @return RequestResult
     */
    Page<SysUser> getPage(Page<SysUser> page, SysUser user);


    /**
     * 重置用户密码
     *
     * @param user
     */
    void resetPassword(ResetPasswordDto user);
}
