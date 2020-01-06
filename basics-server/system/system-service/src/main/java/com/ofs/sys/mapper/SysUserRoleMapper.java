package com.ofs.sys.mapper;

import com.ofs.sys.entity.SysUserRole;
import com.ofs.web.base.IBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author gaoly
 * @version 2019/4/16/11:31
 */
@Mapper
@Repository
public interface SysUserRoleMapper extends IBaseMapper<SysUserRole> {
}
