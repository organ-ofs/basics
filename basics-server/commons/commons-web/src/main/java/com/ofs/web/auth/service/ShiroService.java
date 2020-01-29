package com.ofs.web.auth.service;

import com.ofs.web.auth.model.ShiroMenus;
import com.ofs.web.auth.model.ShiroRole;
import com.ofs.web.auth.model.ShiroUser;

import java.util.List;
import java.util.Map;

/**
 * @author gaoly
 * @version 2019/4/23/13:59
 */
public interface ShiroService {

    /**
     * 获取拦截器数据
     *
     * @return
     */
    Map<String, String> getFilterChainDefinitionMap();

    /**
     * 迭代所有的资源子集
     *
     * @param resource
     * @param permsList
     */
    void iterationAllResourceInToFilter(ShiroMenus resource,
                                        List<String[]> permsList, List<String[]> anonList);

    /**
     * 重新加载权限
     */
    void reloadPerms();

    /**
     * 清除指定用户ID的授权信息
     *
     * @param uid    用户ID
     * @param author 是否清空授权信息
     * @param out    是否清空session
     */
    void clearAuthByUserId(String uid, Boolean author, Boolean out);

    /**
     * 清除指定用户ID集合的授权信息
     *
     * @param userList 用户ID集合
     * @param author   是否清空授权信息
     * @param out      是否清空session
     */
    void clearAuthByUserIdCollection(List<String> userList, Boolean author, Boolean out);

    /**
     * 获取指定ID用户的所有角色（并附带查询所有的角色的权限）
     *
     * @param uid 用户ID
     * @return 角色集合
     */
    List<ShiroRole> getAllRoleByUserId(String uid);


    /**
     * 获取当前登录用户所有的权限标示
     *
     * @return SysUser
     */
    List<String> getAllPermissionTag(String loginId);

    /**
     * 根据用户名查找用户
     *
     * @param loginId 登陆名
     * @return User
     */
    ShiroUser getUserByLoginId(String loginId);
}
