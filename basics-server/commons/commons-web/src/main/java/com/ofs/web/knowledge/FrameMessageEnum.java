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
    SYSTEM_ERROR("EFRA0001", "系统忙..."),

    /**
     * 方法输入参数不合格异常，在返回消息调用者时，会自动添加相关验证错误信息
     */
    PARAMETER_ERROR("EFRA0002", "参数输入不合法"),

    /**
     * 数据更新失败，可能为数据不可用，请刷新后重试
     */
    UPDATE_VERSION_ERROR("EFRA0005", "数据不可用，请刷新后重试"),
    /**
     * 数据更新失败
     */
    UPDATE_ERROR("EFRA0006", "数据更新失败"),
    /**
     * 数据添加失败
     */
    INSERT_ERROR("EFRA0007", "数据添加失败"),
    /**
     * 数据删除失败
     */
    DELETE_ERROR("EFRA0008", "数据删除失败"),
    /**
     * 定时任务操作失败
     */
    QUARTZ_ERROR("EFRA0009", "定时任务操作失败"),

    /**
     * 转换为数值时发生异常。
     */
    EFRA0010("EFRA0010", "\u8F6C\u6362\u4E3A\u6570\u503C\u65F6\u53D1\u751F\u5F02\u5E38\u3002"),
    /**
     * 转换为日期时发生异常。
     */
    EFRA0011("EFRA0011", "\u8F6C\u6362\u4E3A\u65E5\u671F\u65F6\u53D1\u751F\u5F02\u5E38\u3002"),
    /**
     * 转换为时间时发生异常。
     */
    EFRA0012("EFRA0012", "\u8F6C\u6362\u4E3A\u65F6\u95F4\u65F6\u53D1\u751F\u5F02\u5E38\u3002"),
    /**
     * 转换为日期时间时发生异常。
     */
    EFRA0013("EFRA0013", "\u8F6C\u6362\u4E3A\u65E5\u671F\u65F6\u95F4\u65F6\u53D1\u751F\u5F02\u5E38\u3002"),
    /**
     * 文件不存在
     */
    EFRA0014("EFRA0014", "\u6587\u4EF6\u4E0D\u5B58\u5728"),
    /**
     * 文件名称不合法
     */
    EFRA0015("EFRA0015", "\u6587\u4EF6\u540D\u79F0\u4E0D\u5408\u6CD5"),
    /**
     * 文件不存在: {0}
     */
    EFRA0016("EFRA0016", "\u6587\u4EF6\u4E0D\u5B58\u5728: {0}"),
    /**
     * 文件名称不合法: {0}
     */
    EFRA0017("EFRA0017", "\u6587\u4EF6\u540D\u79F0\u4E0D\u5408\u6CD5: {0}"),
    /**
     * 附件表数据不存在(附件ID: {0})
     */
    EFRA0018("EFRA0018", "\u9644\u4EF6\u8868\u6570\u636E\u4E0D\u5B58\u5728(\u9644\u4EF6ID: {0})"),

    /**
     * 加密操作失败
     */
    ENCRYPT_ERROR("EFRA0019", "加密操作失败"),
    /**
     * 解密操作失败
     */
    DECRYPT_ERROR("EFRA0020", "解密操作失败"),

    /**
     * 签名操作失败
     */
    SIGN_ERROR("EFRA0021", "签名操作失败"),
    /**
     * 验签操作失败
     */
    VERIFY_ERROR("EFRA0022", "验签操作失败"),
    /**
     * 摘要操作失败
     */
    DIGEST_ERROR("EFRA0023", "摘要操作失败"),
    ;


    /**
     * 成员变量
     */
    private String value;

    /**
     * 构造方法
     */
    FrameMessageEnum(String id, String message) {
        this.id = id;
        this.message = message;
    }

    /**
     * 消息ID
     */
    private final String id;
    /**
     * 消息内容(中文)
     */
    private final String message;

}
