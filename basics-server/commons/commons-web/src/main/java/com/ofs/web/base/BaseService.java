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
    /**
     * 新增
     *
     * @param entity
     * @throws Exception
     */
    void add(T entity) throws Exception;

    /**
     * update
     *
     * @param entity
     * @throws Exception
     */
    void update(T entity) throws Exception;

    /**
     * 批量删除
     *
     * @param ids
     * @throws Exception
     */
    void removes(List<String> ids) throws Exception;

    /**
     * 删除
     *
     * @param id
     * @throws Exception
     */
    void remove(String id) throws Exception;

    /**
     * 自定义分页
     *
     * @param page
     * @param t
     * @return
     */
    IPage<T> listPage(Page<T> page, T t);

    /**
     * 查询所有
     * @param entity
     * @return
     */
    List<T> list(T entity);

    /**
     * 获取登陆信息
     * @return
     */
    JwtToken getJwtToken();
}
