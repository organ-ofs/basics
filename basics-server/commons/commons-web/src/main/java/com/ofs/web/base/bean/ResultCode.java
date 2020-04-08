package com.ofs.web.base.bean;

import com.ofs.web.knowledge.IMessageEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author gaoly
 * @version 2019/4/18/10:54
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum ResultCode implements IMessageEnum {

    /**
     *
     */
    OK("200", "成功。"),
    /**
     *
     */
    ERROR("00", "失败。"),
    /**
     *
     */
    BAD_REQUEST("400", "请求参数有误。"),
    /**
     *
     */
    AUTHENTICATE("401", "用户未登录或身份异常。"),
    /**
     *
     */
    AUTH_NO("403", "没有权限。"),
    /**
     *
     */
    SERVER_ERROR("500", "服务异常。");


    private String code;

    private String message;


}
