package com.ofs.web.knowledge;

import java.util.Objects;

/**
 * 字典码表常量的共通接口
 *
 * @author gaoly
 */
public interface IDataDictEnum {

    /**
     * 返回枚举的代码
     *
     * @return 枚举的代码
     */
    String getCode();

    /**
     * 返回枚举的文字(中文)
     *
     * @return 枚举的文字(中文)
     */
    String getText();

    /**
     * 参数代码 与 枚举代码 是否相同
     *
     * @param code 代码
     * @return @return true:相同，false:不同
     */
    default boolean codeEquals(String code) {
        return Objects.equals(getCode(), code);
    }

}
