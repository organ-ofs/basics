package com.ofs.sys.core.global.impl;

import com.ofs.sys.serv.entity.SysMenus;
import com.ofs.sys.serv.service.SysMenusService;
import com.ofs.sys.serv.service.SysRoleService;
import com.ofs.sys.serv.service.SysUserService;
import com.ofs.web.auth.model.ShiroMenus;
import com.ofs.web.auth.model.ShiroRole;
import com.ofs.web.auth.model.ShiroUser;
import com.ofs.web.auth.service.ShiroService;
import com.ofs.web.auth.shiro.MyRealm;
import com.ofs.web.base.bean.SystemCode;
import com.ofs.web.exception.RequestException;
import com.ofs.web.utils.BeanConverterUtil;
import com.ofs.web.utils.SpringUtils;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.mgt.DefaultFilterChainManager;
import org.apache.shiro.web.filter.mgt.PathMatchingFilterChainResolver;
import org.apache.shiro.web.servlet.AbstractShiroFilter;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author gaoly
 * @version 2019/4/23/14:01
 */
@Service
public class ShiroServiceImpl implements ShiroService {

    @Autowired
    private SysMenusService menusServiceService;


    @Autowired
    @Lazy
    private SysUserService userService;

    @Autowired
    @Lazy
    private SysRoleService roleService;

    @Override
    public Map<String, String> getFilterChainDefinitionMap() {

        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        List<String[]> permsList = new LinkedList<>();
        List<String[]> anonList = new LinkedList<>();

        List<SysMenus> menus = menusServiceService.list(SysMenus.builder().build());

        if (menus != null) {
            for (SysMenus menu : menus) {
                if (!StringUtils.isEmpty(menu.getPath()) && !StringUtils.isEmpty(menu.getPermission())) {
                    if (!"".equals(menu.getPermission().trim())) {
                        //判断是否需要权限验证
                        if (menu.getVerification()) {
                            permsList.add(0, new String[]{menu.getPath() +
                                    "/**", "perms[" + menu.getPermission() + ":*]"});
                        } else {
                            anonList.add(0, new String[]{menu.getPath() +
                                    "/**", "anon"});
                        }
                    }
                }
                ShiroMenus shiroMenus = new ShiroMenus();
                BeanUtils.copyProperties(menu, shiroMenus);
                iterationAllResourceInToFilter(shiroMenus, permsList, anonList);
            }
        }


        for (String[] strings : anonList) {
            filterChainDefinitionMap.put(strings[0], strings[1]);
        }

        for (String[] strings : permsList) {
            filterChainDefinitionMap.put(strings[0], strings[1]);
        }

        filterChainDefinitionMap.put("/**", "anon");

        return filterChainDefinitionMap;
    }

    @Override
    public void iterationAllResourceInToFilter(ShiroMenus menu,
                                               List<String[]> permsList, List<String[]> anonList) {
        if (menu.getChildren() != null && menu.getChildren().size() > 0) {
            for (ShiroMenus v : menu.getChildren()) {
                if (!StringUtils.isEmpty(v.getPath()) && !StringUtils.isEmpty(v.getPermission())) {
                    if (v.getVerification()) {
                        permsList.add(0, new String[]{v.getPath() + "/**", "perms[" + v.getPermission() + ":*]"});
                    } else {
                        anonList.add(0, new String[]{v.getPath() + "/**", "anon"});
                    }
                    iterationAllResourceInToFilter(v, permsList, anonList);
                }
            }
        }
    }

    @Override
    public void reloadPerms() {

        ShiroFilterFactoryBean shiroFilterFactoryBean = SpringUtils.getBean(ShiroFilterFactoryBean.class);

        AbstractShiroFilter abstractShiroFilter;
        try {
            abstractShiroFilter = (AbstractShiroFilter) shiroFilterFactoryBean.getObject();
        } catch (Exception e) {
            throw new RequestException(SystemCode.FAIL.code, "重新加载权限失败", e);
        }
        PathMatchingFilterChainResolver filterChainResolver =
                (PathMatchingFilterChainResolver) abstractShiroFilter.getFilterChainResolver();
        DefaultFilterChainManager manager = (DefaultFilterChainManager) filterChainResolver
                .getFilterChainManager();

        /*清除旧版权限*/
        manager.getFilterChains().clear();
        shiroFilterFactoryBean.getFilterChainDefinitionMap().clear();

        /*更新新数据*/
        Map<String, String> filterChainDefinitionMap = getFilterChainDefinitionMap();
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        filterChainDefinitionMap.forEach(manager::createChain);
    }

    @Override
    public void clearAuthByUserId(String uid, Boolean author, Boolean out) {
        MyRealm myRealm = SpringUtils.getBean(MyRealm.class);
    }

    @Override
    public void clearAuthByUserIdCollection(List<String> userList, Boolean author, Boolean out) {
        MyRealm myRealm = SpringUtils.getBean(MyRealm.class);
    }

    @Override
    public List<ShiroRole> getAllRoleByUserId(String uid) {

        List<ShiroRole> list = BeanConverterUtil.convert(roleService.getAllRoleByUserId(uid, false), ShiroRole.class);
        return list;
    }

    @Override
    public List<String> getAllPermissionTag(String loginId) {
        return userService.getAllPermissionTag(loginId);
    }

    @Override
    public ShiroUser getUserByLoginId(String loginId) {
        return BeanConverterUtil.convert(userService.getUserByLoginId(loginId, false), ShiroUser.class);
    }
}
