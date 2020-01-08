package com.ofs.sys.serv.message;

import com.ofs.web.knowledge.IDataDictEnum;

/**
 * @author gaoly
 */

public enum DictEnum implements IDataDictEnum {

    /**
     * 类型：菜单
     */
    MENU("1", "菜单"),
    /**
     * 类型：按钮
     */
    BUTTON("2", "按钮");

    private final String code;
    private final String text;

    /**
     * 构造方法
     */
    DictEnum(String code, String text) {
        this.code = code;
        this.text = text;
    }


    /**
     * 通过代码找到枚举
     *
     * @param code 代码
     * @return 枚举
     */
    public static IDataDictEnum forCode(String code) {
        IDataDictEnum item = null;
        IDataDictEnum[] allItems = values();
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
