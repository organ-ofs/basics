package com.ofs.sys.web.mapper;

import com.ofs.sys.web.entity.SysResource;
import com.ofs.web.base.IBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author gaoly
 * @desc
 */
@Mapper
@Repository
public interface SysResourceMapper extends IBaseMapper<SysResource> {
    /**
     * 删除下属的子按钮
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
    List<SysResource> getListByRole(@Param("params") SysResource resource);

    /**
     * 条件查询信息
     *
     * @param resource
     * @return
     */
    List<SysResource> getList(@Param("params") SysResource resource);
}
