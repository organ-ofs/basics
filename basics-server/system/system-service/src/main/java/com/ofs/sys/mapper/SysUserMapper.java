package com.ofs.sys.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.ofs.sys.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface SysUserMapper extends BaseMapper<SysUser> {

}
