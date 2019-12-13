package com.ofs.web.base.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ofs.web.base.BaseEntity;
import com.ofs.web.base.BaseService;
import com.ofs.web.bean.ResponseCode;
import com.ofs.web.exception.RequestException;

import java.util.List;

public class BaseServiceImpl<T extends BaseEntity> extends ServiceImpl implements BaseService<T> {

    @Override
    public void add(T entity) {

    }

    @Override
    public void remove(List<String> idList) {
        try {
            this.deleteBatchIds(idList);
        } catch (Exception e) {
            throw new RequestException(ResponseCode.FAIL.code, "批量删除日志失败", e);
        }
    }

    @Override
    public Page<T> list(Page<T> page, T entity) {
        return null;
    }

    @Override
    public List<T> list(T entity) {
        return null;
    }

    @Override
    public void update(String id, T entity) {

    }
}
