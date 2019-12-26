package com.ofs.web.knowledge;


import lombok.Getter;

/**
 * 系统通用错误集合
 * 错误标识前缀为SY
 *
 * @author ly
 */
@Getter
public enum FrameMessageEnum implements IMessageEnum {

    /**
     * 系统异常
     */
    SYSTEM_ERROR("SY01", "系统忙..."),

    /**
     * 方法输入参数不合格异常，在返回消息调用者时，会自动添加相关验证错误信息
     */
    PARAMETER_ERROR("SY02", "参数输入不合法"),

    /**
     * 数据更新失败，可能为数据不可用，请刷新后重试
     */
    UPDATE_VERSION_ERROR("SY03", "数据不可用，请刷新后重试"),
    /**
     * 数据更新失败
     */
    UPDATE_ERROR("SY04", "数据更新失败"),
    /**
     * 数据添加失败
     */
    INSERT_ERROR("SY05", "数据添加失败"),
    /**
     * 数据删除失败
     */
    DELETE_ERROR("SY06", "数据删除失败"),
    /**
     * 转换为数值时发生异常。
     */
    SY10("SY07", "转换为数值时发生异常。"),
    /**
     * 转换为日期时发生异常。
     */
    SY11("SY08", "转换为日期时发生异常。"),
    /**
     * 转换为时间时发生异常。
     */
    SY12("SY09", "转换为时间时发生异常。"),
    /**
     * 转换为日期时间时发生异常。
     */
    SY13("SY10", "转换为日期时间时发生异常。"),

    /**
     * 文件不存在: {0}
     */
    SY16("SY11", "文件不存在: {0}"),
    /**
     * 文件名称不合法: {0}
     */
    SY17("SY12", "文件名称不合法: {0}"),

    /**
     * 加密操作失败
     */
    ENCRYPT_ERROR("SY13", "加密操作失败"),
    /**
     * 解密操作失败
     */
    DECRYPT_ERROR("SY14", "解密操作失败"),

    /**
     * 签名操作失败
     */
    SIGN_ERROR("SY15", "签名操作失败"),
    /**
     * 验签操作失败
     */
    VERIFY_ERROR("SY16", "验签操作失败")
    ;


    /**
     * 构造方法
     */
    FrameMessageEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 消息ID
     */
    private String code;
    /**
     * 消息内容(中文)
     */
    private String message;

}
