package com.ofs.web.knowledge;

/**
 * 字典码表常量
 *
 * @author 自动生成工具
 */
public class DataDictKnowledge {

    private DataDictKnowledge() {
    }

    /**
     * 是否标志
     */
    public enum YesNoEnum implements IDataDictEnum {
        /**
         * 是
         */
        YES("1", "是"),
        /**
         * 否
         */
        NO("0", "否");

        private String code;
        private String text;

        YesNoEnum(String code, String text) {
            this.code = code;
            this.text = text;
        }

        /**
         * 根据布尔型取得字符串
         *
         * @param flg
         * @return
         */
        public static String getCode(boolean flg) {
            if (flg) {
                return YesNoEnum.YES.code;
            } else {
                return YesNoEnum.NO.code;

            }

        }

        /**
         * 通过代码找到枚举
         *
         * @param code 代码""""""
         * @return 枚举
         */
        public YesNoEnum forCode(String code) {
            YesNoEnum item = null;
            YesNoEnum[] allItems = values();
            for (int i = 0; i < allItems.length; i++) {
                if (allItems[i].codeEquals(code)) {
                    item = allItems[i];
                    break;
                }
            }
            return item;
        }

        @Override
        public String getCode() {
            return code;
        }

        @Override
        public String getText() {
            return text;
        }
    }

    /**
     * 系统语言区分
     */
    public enum LanguageTypeEnum implements IDataDictEnum {
        /**
         * 汉语
         */
        LANGUAGE_CHINESE("zh", "汉语"),
        /**
         * 英语
         */
        LANGUAGE_ENGLISH("en", "英语"),
        /**
         * 日语
         */
        LANGUAGE_JAPAN("jp", "日语");

        private String code;
        private String text;

        LanguageTypeEnum(String code, String text) {
            this.code = code;
            this.text = text;
        }

        /**
         * 通过代码找到枚举
         *
         * @param code 代码""""""
         * @return 枚举
         */
        public LanguageTypeEnum forCode(String code) {
            LanguageTypeEnum item = null;
            LanguageTypeEnum[] allItems = values();
            for (int i = 0; i < allItems.length; i++) {
                if (allItems[i].codeEquals(code)) {
                    item = allItems[i];
                    break;
                }
            }
            return item;
        }

        @Override
        public String getCode() {
            return code;
        }

        @Override
        public String getText() {
            return text;
        }
    }


}
