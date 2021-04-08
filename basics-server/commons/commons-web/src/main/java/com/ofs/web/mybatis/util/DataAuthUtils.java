package com.ofs.web.mybatis.util;

import com.ofs.web.mybatis.annotation.DataAuth;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.mapping.MappedStatement;

import java.lang.reflect.Method;

/**
 * @author gaoly
 */
@Slf4j
public class DataAuthUtils {
    private DataAuthUtils() {

    }

    public static DataAuth getDataAuthAnnotation(MappedStatement mappedStatement) {
        DataAuth dataAuth = null;
        try {
            String id = mappedStatement.getId();
            // 获取mapper类名
            String className = id.substring(0, id.lastIndexOf('.'));
            // 获取mapper的方法名
            String methodName = id.substring(id.lastIndexOf('.') + 1, id.length());
            final Class cls = Class.forName(className);
            final Method[] method = cls.getMethods();
            for (Method me : method) {
                if (me.getName().equals(methodName) && me.isAnnotationPresent(DataAuth.class)) {
                    dataAuth = me.getAnnotation(DataAuth.class);
                }
            }
        } catch (ClassNotFoundException e) {
            if (log.isErrorEnabled()) {
                log.error(mappedStatement.getId() + " class not found.");
            }
        }
        return dataAuth;
    }
}
