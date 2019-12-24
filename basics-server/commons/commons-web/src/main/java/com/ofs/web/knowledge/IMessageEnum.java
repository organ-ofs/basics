package com.ofs.web.knowledge;

/**
 * 消息用枚举的接口
 *
 * @author gaoly
 */
public interface IMessageEnum {

    /**
     * 返回枚举对应的消息Code
     *
     * @return 消息Code
     */
    String getCode();

    /**
     * 返回枚举对应的消息内容(中文)
     *
     * @return 消息内容(中文)
     */
    String getMessage();
}
