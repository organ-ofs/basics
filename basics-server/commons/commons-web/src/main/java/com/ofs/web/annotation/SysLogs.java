package com.ofs.web.annotation;

import java.lang.annotation.*;

/**
 * @author gaoly
 * @version 2019/4/27/17:36
 * 系统日志
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SysLogs {

    String value();

}
