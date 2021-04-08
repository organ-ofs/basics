package com.ofs.web.mybatis.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 启用数据权限
 *
 * @author wanggh
 * @since 2018-5-31
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DataAuth {
    /**
     * 查询sql的ID标识
     *
     * @return
     */
    String value() default "";
}
