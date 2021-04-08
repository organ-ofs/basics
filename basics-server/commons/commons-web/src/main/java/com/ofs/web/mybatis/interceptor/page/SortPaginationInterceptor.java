package com.ofs.web.mybatis.interceptor.page;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.parser.ISqlParser;
import com.baomidou.mybatisplus.core.parser.SqlInfo;
import com.baomidou.mybatisplus.core.toolkit.PluginUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.toolkit.JdbcUtils;
import com.baomidou.mybatisplus.extension.toolkit.SqlParserUtils;
import com.ofs.web.base.SortPage;
import com.ofs.web.mybatis.util.SortSqlUtils;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.session.RowBounds;

import java.sql.Connection;
import java.util.Map;
import java.util.Properties;

import static org.apache.ibatis.session.RowBounds.NO_ROW_LIMIT;

/**
 * 重写orderby 处理逻辑
 *
 * @author gaoly
 */
@Slf4j
@Setter
@Accessors(chain = true)
@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})})
public class SortPaginationInterceptor extends PaginationInterceptor implements Interceptor {
    /**
     * COUNT SQL 解析
     */
    private ISqlParser sqlParser;
    /**
     * 溢出总页数，设置第一页
     */
    private boolean overflow = false;
    /**
     * 方言类型
     */
    private String dialectType;
    /**
     * 方言实现类
     */
    private String dialectClazz;


    /**
     * Physical Pagination Interceptor for all the queries with parameter
     * {@link RowBounds}
     */
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        StatementHandler statementHandler = (StatementHandler) PluginUtils.realTarget(invocation.getTarget());
        MetaObject metaObject = SystemMetaObject.forObject(statementHandler);

        // SQL 解析
        this.sqlParser(metaObject);

        // 先判断是不是SELECT操作
        MappedStatement mappedStatement = (MappedStatement) metaObject.getValue("delegate.mappedStatement");
        if (!SqlCommandType.SELECT.equals(mappedStatement.getSqlCommandType())) {
            return invocation.proceed();
        }

        // 针对定义了rowBounds，做为mapper接口方法的参数
        BoundSql boundSql = (BoundSql) metaObject.getValue("delegate.boundSql");
        Object paramObj = boundSql.getParameterObject();

        // 判断参数里是否有page对象
        IPage page = null;
        if (paramObj instanceof IPage) {
            page = (IPage) paramObj;
        } else if (paramObj instanceof Map) {
            for (Object arg : ((Map) paramObj).values()) {
                if (arg instanceof IPage) {
                    page = (IPage) arg;
                    break;
                }
            }
        }

        /**
         * 不需要分页的场合，如果 size 小于 0 返回结果集
         */
        if (null == page || page.getSize() < 0) {

            metaObject.setValue("delegate.rowBounds.offset", RowBounds.NO_ROW_OFFSET);
            metaObject.setValue("delegate.rowBounds.limit", NO_ROW_LIMIT);
            return invocation.proceed();
        }

        // 针对定义了rowBounds，做为mapper接口方法的参数
        String originalSql = boundSql.getSql();
        Connection connection = (Connection) invocation.getArgs()[0];

        DbType dbType = StringUtils.isNotEmpty(dialectType) ? DbType.getDbType(dialectType)
                : JdbcUtils.getDbType(connection.getMetaData().getURL());


        boolean orderBy = true;
        if (page.getTotal() == 0) {
            SqlInfo sqlInfo = SqlParserUtils.getOptimizeCountSql(page.optimizeCountSql(), sqlParser, originalSql);
            orderBy = sqlInfo.isOrderBy();
            this.queryTotal(overflow, sqlInfo.getSql(), mappedStatement, boundSql, page, connection);
            if (page.getTotal() <= 0) {
                return invocation.proceed();
            }
        }
        String buildSql = "";
        if (page instanceof SortPage) {
            SortPage rowBounds = (SortPage) page;
            buildSql = SortSqlUtils.concatOrderBy(originalSql, rowBounds, orderBy);
        } else {
            buildSql = concatOrderBy(originalSql, page, orderBy);
        }

//        originalSql = DialectFactory.buildPaginationSql(page, buildSql, dbType, dialectClazz);

        /*
         * <p> 禁用内存分页 </p>
         * <p> 内存分页会查询所有结果出来处理（这个很吓人的），如果结果变化频繁这个数据还会不准。</p>
         */
        metaObject.setValue("delegate.boundSql.sql", originalSql);
        metaObject.setValue("delegate.rowBounds.offset", RowBounds.NO_ROW_OFFSET);
        metaObject.setValue("delegate.rowBounds.limit", RowBounds.NO_ROW_LIMIT);
        return invocation.proceed();

    }


    @Override
    public Object plugin(Object target) {
        if (target instanceof StatementHandler) {
            return Plugin.wrap(target, this);
        }
        return target;
    }

    @Override
    public void setProperties(Properties prop) {
        String dialectType = prop.getProperty("dialectType");
        String dialectClazz = prop.getProperty("dialectClazz");
        if (StringUtils.isNotEmpty(dialectType)) {
            this.dialectType = dialectType;
        }
        if (StringUtils.isNotEmpty(dialectClazz)) {
            this.dialectClazz = dialectClazz;
        }
    }

}
