package com.ofs.web.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.UUID;

/**
 * 提供系统静态功能方法
 *
 * @author
 */
public class FunctionUtil {

    /**
     * 变更模块代码后缀代码
     */
    private static final String MODULE_CHANGE = "_MOD";

    private FunctionUtil() {

    }

    /**
     * 取得MD5key Administrator 2014-7-4
     *
     * @return String
     */
    public static String getCacheKeyByMD5(Object[] obj) {
        String jsonKey = FastJsonUtil.toJson(obj);
        String md5Key = StringUtils.trimToEmpty(org.apache.commons.codec.digest.DigestUtils.md5Hex(jsonKey));
        if ("".equals(md5Key)) {
            return UUID.randomUUID().toString();
        }
        return md5Key;
    }

    /**
     * 防止XSS攻击的方法
     *
     * @param value
     * @return
     */
    public static String cleanXSS(String value) {
        if (StringUtils.isEmpty(value)) {
            return value;
        }
//        value = value.replaceAll("'", "\\'");
//        value = value.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
//        value = value.replaceAll("%3C", "&lt;").replaceAll("%3E", "&gt;");
//        value = value.replaceAll("\\(", "&#40;").replaceAll("\\)", "&#41;");
//        value = value.replaceAll("%28", "&#40;").replaceAll("%29", "&#41;");
//        value = value.replaceAll("'", "&#39;");
//        value = value.replaceAll("eval\\((.*)\\)", "");
//        value = value.replaceAll("[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']", "\"\"");
//        value = value.replaceAll("script", "");
        return value;
    }

    /**
     * 返回模块代码，
     *
     * @param bpmModule
     * @return
     */
    public static String getBpmModule(String bpmModule) {
        if (!"".equals(StringUtils.trimToEmpty(bpmModule)) && bpmModule.length() >= 4) {
            int end = bpmModule.lastIndexOf(MODULE_CHANGE);
            if (end == bpmModule.length() - MODULE_CHANGE.length()) {
                bpmModule = bpmModule.substring(0, end);
            }
        }
        return bpmModule;
    }

    /**
     * 判断是否为变更模块代码
     *
     * @param bpmModule
     * @return
     */
    public static Boolean change(String bpmModule) {
        boolean isChange = false;
        if (!"".equals(StringUtils.trimToEmpty(bpmModule)) && bpmModule.length() >= 4) {
            if (bpmModule.lastIndexOf(MODULE_CHANGE) == bpmModule.length() - MODULE_CHANGE.length()) {
                isChange = true;
            }
        }
        return isChange;
    }
}
