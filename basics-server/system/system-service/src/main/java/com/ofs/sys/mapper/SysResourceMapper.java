package com.ofs.sys.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ofs.sys.entity.SysResource;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author gaoly
 * @desc
 */
@Mapper
@Repository
public interface SysResourceMapper extends BaseMapper<SysResource> {
    /**
     * 删除下属的子菜单
     *
     * @param ids
     */
    void deleteChildren(List<String> ids);

    /**
     * 根据角色ID条件查询信息
     *
     * @param resource
     * @return
     */
    List<SysResource> getListByRole(SysResource resource);

    /**
     * 根据角色ID条件查询信息
     *
     * @param resource
     * @return
     */
    List<SysResource> getList(SysResource resource);
}
