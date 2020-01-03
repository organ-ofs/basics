package com.ofs.sys.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ofs.sys.entity.SysLog;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author gaoly
 * @version 2019/4/28/9:56
 */
@Mapper
@Repository
public interface SysLogMapper extends BaseMapper<SysLog> {
}
