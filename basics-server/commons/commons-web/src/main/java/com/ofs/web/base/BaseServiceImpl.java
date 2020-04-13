package com.ofs.web.base;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ofs.web.base.bean.SystemCode;
import com.ofs.web.exception.RequestException;
import com.ofs.web.jwt.JwtToken;
import com.ofs.web.utils.WebTools;

import java.util.List;

public class BaseServiceImpl<M extends IBaseMapper<T>, T extends BaseEntity> extends ServiceImpl<M, T> implements BaseService<T> {

    @Override
    public boolean add(T entity) throws Exception {
        return super.save(entity);
    }

    @Override
    public void update(T entity) throws Exception {
        super.updateById(entity);
    }

    @Override
    public void removes(List<String> idList) throws Exception {
        try {
            this.removeByIds(idList);
        } catch (Exception e) {
            throw new RequestException(SystemCode.FAIL.code, "批量删除失败", e);
        }
    }

    @Override
    public void remove(String id) throws Exception {
        try {
            T t = super.getById(id);
            if (t == null) {
                throw RequestException.fail("删除失败，不存在！");
            }
            this.removeById(id);
        } catch (Exception e) {
            throw new RequestException(SystemCode.FAIL.code, "删除失败", e);
        }
    }

    @Override
    public IPage<T> listPage(Page<T> page, T entity) {
        return baseMapper.listPage(page, entity);
    }

    @Override
    public List<T> list(T entity) {
        return null;
    }

    @Override
    public String getAccount() {
        JwtToken token = WebTools.getJwtToken();
        return token.getAccount();
    }
}
