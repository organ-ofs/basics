package com.ofs.web.mybatis.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.ofs.web.auth.model.ShiroUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.shiro.SecurityUtils;

import java.util.Date;

/**
 * mybatisplus自定义填充公共字段 ,即没有传的字段自动填充
 *
 * @author gaoly
 */
@Slf4j
public class SysMetaObjectHandler implements MetaObjectHandler {


    private static final String CREATE_DATE = "createDate";
    private static final String CREATE_USER = "createUser";
    private static final String UPDATE_DATE = "updateDate";
    private static final String UPDATE_USER = "updateUser";

    /**
     * 新增填充
     */

    @Override
    public void insertFill(MetaObject metaObject) {
        String staffId = "获取用户信息为空";
        try {
            if (SecurityUtils.getSubject().isAuthenticated()) {
                staffId = ShiroUser.getCurrentUser().getId();
            }
        } catch (Exception e) {
            log.error("获取用户信息为空");
        }
        setFieldValByName(CREATE_USER, staffId, metaObject);
        setFieldValByName(UPDATE_USER, staffId, metaObject);
        setFieldValByName(CREATE_DATE, new Date(), metaObject);
        setFieldValByName(UPDATE_DATE, new Date(), metaObject);
    }

    /**
     * 更新填充
     */

    @Override
    public void updateFill(MetaObject metaObject) {
        String staffId = "获取用户信息为空";
        try {
            if (SecurityUtils.getSubject().isAuthenticated()) {
                staffId = ShiroUser.getCurrentUser().getId();
            }
        } catch (Exception e) {
            log.error("获取用户信息为空");
        }
        //防止用户修改
        setFieldValByName(UPDATE_USER, staffId, metaObject);
        setFieldValByName(UPDATE_DATE, new Date(), metaObject);
    }
}
