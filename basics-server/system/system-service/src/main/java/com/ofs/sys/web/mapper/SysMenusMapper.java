package com.ofs.sys.web.mapper;

import com.ofs.sys.web.entity.SysMenus;
import com.ofs.web.base.IBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author gaoly
 * @version 2019/4/28/9:56
 */
@Mapper
@Repository
public interface SysMenusMapper extends IBaseMapper<SysMenus> {
    /**
     * 条件查询信息
     *
     * @param menus
     * @return
     */
    List<SysMenus> getList(@Param("params") SysMenus menus);


    /**
     * 根据角色ID条件查询信息
     *
     * @param menus
     * @return
     */
    List<SysMenus> getListByRole(@Param("params") SysMenus menus);
}
