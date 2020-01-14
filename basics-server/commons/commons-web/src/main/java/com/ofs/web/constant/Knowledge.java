package com.ofs.web.constant;

import org.apache.commons.lang3.StringUtils;

import java.util.Locale;

/**
 * @author ly
 * @version V1.0
 * @Title: ErrorMsgKnowledge
 * @date 2019/3/22 10:53
 */
public class Knowledge {


    public static final String LOCALE_EN = "en";

    public static final String LOCALE_CN = "zh";

    /**
     * 多语言标识
     *
     * @author ly
     */
    public enum LocaleLangEnum {

        /**
         * 英文
         */
        EN(LOCALE_EN),

        /**
         * 中文
         */
        CN(LOCALE_CN);


        /**
         * 成员变量
         */
        private String value;

        /**
         * 构造方法
         */
        LocaleLangEnum(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        /**
         * 取得Locale类型
         *
         * @param value 值
         * @return Locale类型
         */
        public static Locale getLocale(String value) {
            switch (value) {
                case LOCALE_EN:
                    return Locale.US;
                case LOCALE_CN:
                    return Locale.CHINA;
                default:
                    break;
            }
            return Locale.CHINA;
        }

        /**
         * 取得枚举类型
         *
         * @param value 值
         * @return 枚举类型
         */
        public static LocaleLangEnum get(String value) {
            for (LocaleLangEnum p : LocaleLangEnum.values()) {
                if (p.getValue().equals(value)) {
                    return p;
                }
            }
            return null;
        }


        /**
         * 类型是否包含
         *
         * @param type
         * @return
         */
        public static boolean isContains(String type) {
            if (StringUtils.isEmpty(type)) {
                return false;
            }
            for (LocaleLangEnum langEnum : LocaleLangEnum.values()) {
                if (langEnum.getValue().equals(type)) {
                    return true;
                }
            }
            return false;
        }
    }

    /**
     * 终端信息
     *
     * @author ly
     */
    public enum TerminalEnum {

        /**
         * PC
         */
        PC("PC"),
        /**
         * WAP
         */
        WAP("WAP"),
        /**
         * APP
         */
        APP("APP");

        /**
         * 成员变量
         */
        private String value;

        /**
         * 构造方法
         */
        TerminalEnum(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        /**
         * 取得枚举类型
         *
         * @param value 值
         * @return 枚举类型
         */
        public static TerminalEnum get(String value) {
            for (TerminalEnum p : TerminalEnum.values()) {
                if (p.getValue().equals(value)) {
                    return p;
                }
            }
            return TerminalEnum.PC;
        }
    }


}
