package com.ofs.sys.mapper;

import com.ofs.sys.entity.SysRole;
import com.ofs.web.base.IBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface SysRoleMapper extends IBaseMapper<SysRole> {

}
