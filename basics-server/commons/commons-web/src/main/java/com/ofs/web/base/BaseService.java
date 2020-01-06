package com.ofs.web.base;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ofs.web.jwt.JwtToken;

import java.util.List;

/**
 * @author gaoly
 * @version 2019/5/25/11:43
 */
public interface BaseService<T extends BaseEntity> extends IService<T> {
    void add(T entity);

    void removes(List<String> ids);

    void remove(String id);

    IPage<T> listPage(Page<T> page, T t);

    List<T> list(T entity);

    void update(T entity);

    JwtToken getJwtToken();
}
