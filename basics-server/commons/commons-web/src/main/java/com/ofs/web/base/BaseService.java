package com.ofs.web.base;

import com.baomidou.mybatisplus.plugins.Page;

import java.util.List;

/**
 * @author Licoy
 * @version 2018/5/25/11:43
 */
public interface BaseService<T extends BaseEntity> {
    void add(T entity);

    void remove(List<String> idList);

    Page<T> list(Page<T> page, T entity);

    List<T> list(T entity);

    void update(String id, T entity);
}
