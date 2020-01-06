package com.ofs.web.base.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ofs.web.base.BaseEntity;
import com.ofs.web.base.BaseService;
import com.ofs.web.base.IBaseMapper;
import com.ofs.web.bean.SystemCode;
import com.ofs.web.exception.RequestException;
import com.ofs.web.jwt.JwtToken;
import com.ofs.web.utils.Tools;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.BeanUtils;

import java.util.Arrays;
import java.util.List;

public class BaseServiceImpl<M extends IBaseMapper<T>, T extends BaseEntity> extends ServiceImpl<M, T> implements BaseService<T> {

    @Override
    public void add(T entity) {
    }

    @Override
    public void removes(List<String> idList) {
        try {
            this.removeByIds(idList);
        } catch (Exception e) {
            throw new RequestException(SystemCode.FAIL.code, "批量删除失败", e);
        }
    }

    @Override
    public void remove(String id) {
        try {
            this.removeByIds(Arrays.asList(new String[]{id}));
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
    public void update(T entity) {

    }

    @Override
    public JwtToken getJwtToken() {
        Tools.executeLogin();
        Subject subject = SecurityUtils.getSubject();
        if (!subject.isAuthenticated()) {
            throw new RequestException(SystemCode.NOT_SING_IN);
        }
        JwtToken jwtToken = new JwtToken();
        Object principal = subject.getPrincipal();
        if (principal == null) {
            throw RequestException.fail("用户信息获取失败");
        }
        BeanUtils.copyProperties(principal, jwtToken);
        return jwtToken;
    }

}
