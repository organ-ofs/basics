package com.ofs.sys.web.mapper;

import com.ofs.sys.web.entity.SysRole;
import com.ofs.web.base.IBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface SysRoleMapper extends IBaseMapper<SysRole> {

}
