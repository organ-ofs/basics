package com.ofs.web.knowledge;

import lombok.Getter;

/**
 * 权限错误集合
 * 错误标识前缀为SY
 *
 * @author ly
 */
@Getter
public enum AuthMessageEnum implements IMessageEnum {


    /**
     * 用户API权限验证失败
     */
    UNAUTH_API_ERROR("EAUT0001", "用户API权限验证失败"),
    /**
     * 账户验证不通过
     */
    FORBIDDEN_ACCOUNT_ERROR("EAUT0002", "账户验证不通过"),

    /**
     * TOKEN验证不通过
     */
    FORBIDDEN_TOKEN_ERROR("EAUT0003", "TOKEN验证不通过"),

    /**
     * 用户权限验证失败过期
     */
    TOKEN_EXPIRED_ERROR("EAUT0004", "用户权限验证失败过期"),

    /**
     * 用户不存在(user does not exist)
     */
    ACCOUNT_NOT_EXIST("EAUT0005", "用户不存在"),
    /**
     * 用户账户已锁定，暂无法登陆
     */
    ACCOUNT_LOCK("EAUT0006", "用户账户已锁定，暂无法登陆"),
    /**
     * 用户账户重复登陆
     */
    ACCOUNT_KICK_OUT("EAUT0007", "用户账户重复登陆"),
    /**
     * 用户账户已锁定，暂无法登陆
     */
    ACCOUNT_LIMIT("EAUT0009", "密码重试次数过多，账户暂时无法登陆"),

    /**
     * TOKEN验证不通过
     */
    REFRESH_TOKEN_ERROR("EAUT0010", "刷新指令失败"),
    ;


    /**
     * 构造方法
     */
    AuthMessageEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 消息ID
     */
    private final String code;
    /**
     * 消息内容(中文)
     */
    private final String message;

}
