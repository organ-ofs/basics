package com.ofs.web.utils;
/**
 * @Package net.cits.tour.common.util
 * @Description: fastjson基础类
 * @author ly
 * @date 2017/4/21 13:35
 * @version V1.0
 * <p>
 * FastJsonUtil ly
 * @create 2017-04-21 13:35
 **/

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SimpleDateFormatSerializer;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.sql.Timestamp;
import java.util.*;

@Slf4j
public class FastJsonUtil {
    private FastJsonUtil() {

    }

    /**
     * 对日期处理
     */
    private static SerializeConfig mapping = new SerializeConfig();
    private static String dateFormat;

    static {
        dateFormat = "yyyy-MM-dd HH:mm:ss";
        mapping.put(Date.class, new SimpleDateFormatSerializer(dateFormat));
        mapping.put(Timestamp.class, new SimpleDateFormatSerializer(dateFormat));
        mapping.put(java.sql.Date.class, new SimpleDateFormatSerializer(dateFormat));
    }


    /**
     * 生成JSON
     *
     * @return String
     */
    public static String toJson(Object object, FastJsonConfig frameFastJsonConfig) {
        String jsonString = "";
        try {

            jsonString = JSON.toJSONString(object,
                    frameFastJsonConfig.getSerializeConfig(),
                    frameFastJsonConfig.getSerializeFilters(),
                    frameFastJsonConfig.getDateFormat(),
                    JSON.DEFAULT_GENERATE_FEATURE,
                    frameFastJsonConfig.getSerializerFeatures());
        } catch (Exception e) {
            log.error("toJson", e);
        }

        return jsonString;

    }

    /**
     * 生成JSON
     *
     * @return String
     */
    public static String toJson(Object object) {
        String jsonString = "";
        try {
            if (object == null) {
                return jsonString;
            }

            jsonString = JSON.toJSONString(object, mapping);
        } catch (Exception e) {
            log.error("toJson", e);
        }

        return jsonString;

    }

    /**
     * 排除要过滤的属性
     *
     * @param o
     * @param excludeKeys
     * @return
     */
    public static String toJson(Object o, String... excludeKeys) {
        List<String> excludes = Arrays.asList(excludeKeys);
        SimplePropertyPreFilter filter = new SimplePropertyPreFilter();
        filter.getExcludes().addAll(excludes);

        return JSON.toJSONString(o, mapping, filter);
    }

    /**
     * 用fastjson 将json字符串解析为一个 Map<String, Object>
     *
     * @return Map<String,Object>
     */
    public static Map<String, Object> getMap(String jsonString) {
        Map<String, Object> t = null;
        try {

            t = JSON.parseObject(jsonString, new TypeReference<Map<String, Object>>() {
            });
        } catch (Exception e) {
            log.error("getMap", e);

        }
        return t;
    }

    /**
     * 用fastjson 将json字符串解析为一个 JSONArray
     */
    public static JSONArray getJSONArray(String jsonString) {
        JSONArray t = new JSONArray();
        try {
            if (StringUtils.isEmpty(jsonString)) {
                return t;
            }
            t = JSON.parseArray(jsonString);
        } catch (Exception e) {
            log.error("getJSONArray", e);

        }
        return t;
    }

    /**
     * 用fastjson 将json字符串解析为一个 JSONObject
     */
    public static JSONObject getJSONObject(String jsonString) {
        JSONObject t = new JSONObject();
        try {
            if (StringUtils.isEmpty(jsonString)) {
                return t;
            }
            t = JSON.parseObject(jsonString);
        } catch (Exception e) {
            log.error("getJSONObject", e);

        }
        return t;
    }

    /**
     * 用fastjson 将json字符串解析为一个 JavaBean
     */
    public static <T> T getObject(String jsonString, Class<T> cls) {
        T t = null;
        try {
            t = JSON.parseObject(jsonString, cls);
        } catch (Exception e) {

            log.error("getObject", e);

        }
        return t;
    }

    /**
     * 用fastjson 将json字符串解析为一个 JavaBean
     */
    public static <T> T getObject(String jsonString, TypeReference<T> typeReference) {
        T t = null;
        try {
            t = JSON.parseObject(jsonString, typeReference);
        } catch (Exception e) {

            log.error("getObject", e);

        }
        return t;
    }

    /**
     * 用fastjson 将json字符串 解析成为一个 List<JavaBean> 及 List<String>
     */
    public static <T> List<T> getObjects(String jsonString, Class<T> cls) {
        List<T> list = new ArrayList<>();
        try {
            list = JSON.parseArray(jsonString, cls);
        } catch (Exception e) {
            log.error("getObjects", e);

        }
        return list;
    }


    /**
     * 用fastjson 将jsonString 解析成 List<Map<String,Object>>
     */
    public static List<Map<String, Object>> getListMap(String jsonString) {
        List<Map<String, Object>> list = new ArrayList<>();
        try {

            list = JSON.parseObject(jsonString, new TypeReference<List<Map<String, Object>>>() {
            });
        } catch (Exception e) {
            log.error("getListMap", e);

        }
        return list;

    }

    /**
     * 用fastjson 将jsonString 解析成 List<Map<String,String>>
     */
    public static List<Map<String, String>> getListStringMap(String jsonString) {
        List<Map<String, String>> list = new ArrayList<>();
        try {
            // 两种写法
            list = JSON.parseObject(jsonString, new TypeReference<List<Map<String, String>>>() {
            }.getType());

        } catch (Exception e) {
            log.error("getListStringMap", e);

        }
        return list;
    }

    /**
     * 用fastjson 将jsonString 解析成 Map<String,JavaBean>
     */
    public static <T> Map<String, T> getJavaBeanMap(String jsonString) {
        Map<String, T> list = new HashMap<>(16);
        try {
            // 两种写法
            list = JSON.parseObject(jsonString, new TypeReference<Map<String, T>>() {
            }.getType());
        } catch (Exception e) {
            log.error("getJavaBeanMap", e);

        }
        return list;
    }

    /**
     * 用fastjson 将jsonString 解析成 List<List<Object>>
     */
    public static <T> List<List<T>> getListS(String jsonString, Class<T> cls) {
        List<List<T>> list = new ArrayList<>(0);
        try {
            List<String> s = JSON.parseArray(jsonString, String.class);
            list = new ArrayList<>(s.size());
            for (String list2 : s) {
                List<T> e = FastJsonUtil.getObjects(list2, cls);
                list.add(e);
            }
        } catch (Exception e) {
            log.error("getListS", e);

        }
        return list;

    }

    /**
     * 用fastjson 将jsonString 解析成 Map<String,List<T>>
     */
    public static <T> Map<String, List<T>> getJavaBeanListMap(String jsonString) {
        Map<String, List<T>> map = new LinkedHashMap<>();
        try {
            // 两种写法
            map = JSON.parseObject(jsonString, new TypeReference<LinkedHashMap<String, List<T>>>() {
            }.getType());
        } catch (Exception e) {
            log.error("getJavaBeanListMap", e);

        }
        return map;
    }
}
