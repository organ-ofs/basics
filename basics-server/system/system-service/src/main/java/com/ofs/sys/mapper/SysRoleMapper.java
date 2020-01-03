package com.ofs.sys.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ofs.sys.entity.SysRole;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface SysRoleMapper extends BaseMapper<SysRole> {

}
