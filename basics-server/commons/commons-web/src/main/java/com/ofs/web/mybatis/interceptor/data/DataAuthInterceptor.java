package com.ofs.web.mybatis.interceptor.data;

import com.baomidou.mybatisplus.core.toolkit.PluginUtils;
import com.baomidou.mybatisplus.extension.handlers.AbstractSqlParserHandler;
import com.ofs.web.auth.model.ShiroUser;
import com.ofs.web.constant.FrameProperties;
import com.ofs.web.mybatis.annotation.DataAuth;
import com.ofs.web.mybatis.service.IDataAuthService;
import com.ofs.web.mybatis.util.DataAuthUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Connection;
import java.util.Properties;

/**
 * <p>
 * 数据权限拦截器
 * 放在分页拦截器之后注册
 * </p>
 *
 * @author gaoly
 * @since 2018-05-31
 */
@Intercepts({@Signature(
        type = StatementHandler.class,
        method = "prepare",
        args = {Connection.class, Integer.class}
)})
@Slf4j
public class DataAuthInterceptor extends AbstractSqlParserHandler implements Interceptor {

    @Autowired
    BeanFactory beanFactory;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        RoutingStatementHandler statementHandler = (RoutingStatementHandler) PluginUtils.realTarget(invocation.getTarget());
        MetaObject metaObject = SystemMetaObject.forObject(statementHandler);

        MappedStatement mappedStatement = (MappedStatement) metaObject.getValue("delegate.mappedStatement");
        SqlCommandType sqlCommandType = mappedStatement.getSqlCommandType();


        DataAuth dataAuth = DataAuthUtils.getDataAuthAnnotation(mappedStatement);
        //如果没有权限控制注解，则跳过
        if (dataAuth == null || StringUtils.isBlank(dataAuth.value())) {
            return invocation.proceed();
        }
        //只处理查询
        if (sqlCommandType != SqlCommandType.SELECT) {
            return invocation.proceed();
        }
        // 获取注解中的查询键值，结合登录用户获取拼接的查询条件
        if (!SecurityUtils.getSubject().isAuthenticated()) {
            return invocation.proceed();
        }
        if (this.isSuperAdmin()) {
            return invocation.proceed();
        }
        // 设置新的sql
        BoundSql boundSql = (BoundSql) metaObject.getValue("delegate.boundSql");
        metaObject.setValue("delegate.boundSql.sql", getSql(boundSql.getSql(), dataAuth.value()));

        return invocation.proceed();
    }

    /**
     * 判断是否为超级管理员
     *
     * @return
     */
    private boolean isSuperAdmin() {
        FrameProperties frameProperties = beanFactory.getBean(FrameProperties.class);
        String adminAccount = StringUtils.trimToEmpty(frameProperties.getAuth().getAdminAccount());
        if (adminAccount.equals(ShiroUser.getCurrentUser().getAccount())) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 权利控制代码拼装
     *
     * @param sql
     * @param dataAuth
     * @return
     * @throws Exception
     */
    private String getSql(String sql, String dataAuth) throws Exception {
        String authSql = this.getAuthSql(dataAuth);

        StringBuilder sbSql = new StringBuilder("SELECT * FROM (")
                .append(sql)
                .append(" ) TBL  ");
        if (StringUtils.isNotBlank(authSql)) {

            sbSql.append(" WHERE ").append(authSql);
        }
        return sbSql.toString();

    }

    /**
     * 取得增强sql
     *
     * @param dataAuth
     * @return
     */
    private String getAuthSql(String dataAuth) {
        // 获取登录id
        String staffId = ShiroUser.getCurrentUser().getId();
        // 获取数据权限sql数据
        IDataAuthService dataAuthService = beanFactory.getBean(IDataAuthService.class);

        return dataAuthService.getDataAuth(staffId, dataAuth);

    }


    @Override
    public Object plugin(Object target) {
        return target instanceof StatementHandler ? Plugin.wrap(target, this) : target;
    }

    @Override
    public void setProperties(Properties properties) {
        if (log.isDebugEnabled()) {
            log.debug("setProperties");
        }
    }

}

