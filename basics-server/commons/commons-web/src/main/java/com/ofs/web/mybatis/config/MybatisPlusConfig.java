package com.ofs.web.mybatis.config;
/**
 * @Description: 权限数据源
 * @author gaoly
 * @date 2017/5/8 15:52
 * @version V1.0
 */

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.core.incrementer.IKeyGenerator;
import com.baomidou.mybatisplus.core.injector.ISqlInjector;
import com.baomidou.mybatisplus.extension.incrementer.OracleKeyGenerator;
import com.baomidou.mybatisplus.extension.injector.LogicSqlInjector;
import com.baomidou.mybatisplus.extension.plugins.OptimisticLockerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PerformanceInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisMapperRefresh;
import com.ofs.web.mybatis.handler.SysMetaObjectHandler;
import com.ofs.web.mybatis.interceptor.data.DataAuthInterceptor;
import com.ofs.web.mybatis.interceptor.page.SortPaginationInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.IOException;


@Configuration
@Slf4j
@MapperScan("com.frame.**.mapper")
public class MybatisPlusConfig {
    /**
     * 分页
     *
     * @return
     */
    @Bean(name = "paginationInterceptor")
    public PaginationInterceptor paginationInterceptor() {
        SortPaginationInterceptor page = new SortPaginationInterceptor();
        page.setDialectType("oracle");
        page.setOverflow(true);
        //分页count优化，需要的话请自行指定
        return page;
    }

    /**
     * 乐观锁
     *
     * @return
     */
    @Bean(name = "optimisticLockerInterceptor")
    public OptimisticLockerInterceptor optimisticLockerInterceptor() {
        return new OptimisticLockerInterceptor();

    }

    /**
     * 热加载 xml
     *
     * @param sessionFactory
     * @return
     * @throws IOException
     */
    @Bean
    @Profile({"dev", "test"})
    public MybatisMapperRefresh mybatisMapperRefresh(SqlSessionFactory sessionFactory) throws IOException {

        ResourcePatternResolver loader = new PathMatchingResourcePatternResolver();
        return new MybatisMapperRefresh(loader.getResources("classpath*:mapper/**/*Mapper.xml"), sessionFactory, true);
    }

    /**
     * SQL 执行性能分析，开发环境使用，线上不推荐。
     * 设置 dev test 环境开启
     *
     * @return
     */
//    @Bean
    @Profile({"dev", "test"})
    public PerformanceInterceptor performanceInterceptor() {
        PerformanceInterceptor performanceInterceptor = new PerformanceInterceptor();
        ///maxTime SQL 执行最大时长，超过自动停止运行，有助于发现问题。
        performanceInterceptor.setMaxTime(1000);
        ///*<!--SQL是否格式化 默认false-->*/
//        performanceInterceptor.setFormat(true);

        return performanceInterceptor;
    }

    /**
     * 数据权限SQL拦截
     *
     * @return
     */
    @Bean(name = "dataAuthInterceptor")
    public DataAuthInterceptor dataAuthInterceptor() {
        return new DataAuthInterceptor();
    }


    /**
     * 自定义填充策略接口实现
     *
     * @return
     */
    @Bean(name = "metaObjectHandler")
    public MetaObjectHandler metaObjectHandler() {
        return new SysMetaObjectHandler();
    }

    /**
     * 逻辑删除配置
     *
     * @return
     */
    @Bean(name = "sqlInjector")
    public ISqlInjector logicSqlInjector() {
        return new LogicSqlInjector();
    }

    /**
     * 注入主键生成器
     *
     * @return
     */
    @Bean
    public IKeyGenerator keyGenerator() {
        return new OracleKeyGenerator();
    }


}
