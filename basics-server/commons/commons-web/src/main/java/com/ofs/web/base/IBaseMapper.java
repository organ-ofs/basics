package com.ofs.web.base;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

public interface IBaseMapper<T> extends BaseMapper<T> {
    /**
     * 自定义分页
     *
     * @param page
     * @param t
     * @return
     */
    IPage<T> listPage(Page<T> page, @Param("params") T t);
}
