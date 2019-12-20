package com.ofs.web.base;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;

import java.util.List;

/**
 * @author Licoy
 * @version 2018/5/25/11:43
 */
public interface BaseService<T extends BaseEntity> extends IService<T> {
    void add(T entity);

    void removes(List<String> ids);

    Page<T> list(Page<T> page, T entity);

    List<T> list(T entity);

    void update(T entity);
}
