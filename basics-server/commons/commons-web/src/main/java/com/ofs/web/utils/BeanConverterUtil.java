package com.ofs.web.utils;
/**
 * @author ly
 */

import com.baomidou.mybatisplus.toolkit.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Slf4j
public class BeanConverterUtil {
    private BeanConverterUtil() {

    }

    /**
     * 单个对象转换
     */
    public static <F, T> T convert(F from, Class<T> clazz) {
        if (from == null) {
            return null;
        }
        T to = null;
        try {
            to = clazz.newInstance();
        } catch (Exception e) {
            log.error("初始化{}对象失败。", clazz, e);
        }
        convert(from, to);
        return to;
    }

    /**
     * 批量对象转换
     */
    public static <F, T> List<T> convert(List<F> fromList, Class<T> clazz) {

        if (fromList == null) {
            return Collections.emptyList();
        }
        List<T> toList = new ArrayList<>();
        if (fromList.isEmpty()) {
            return toList;
        }
        for (F from : fromList) {
            toList.add(convert(from, clazz));
        }
        return toList;
    }

    /**
     * 属性拷贝方法，有特殊需求时子类覆写此方法
     */
    private static <F, T> void convert(F from, T to) {
        BeanUtils.copyProperties(from, to);
    }


    /**
     * Id转换为set方法
     *
     * @param fieldName
     * @return
     */
    public static String setMethodName(String fieldName) {

        if ("".equals(Objects.toString(fieldName, StringUtils.EMPTY))) {
            return "";
        }

        return "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1, fieldName.length());
    }
}
