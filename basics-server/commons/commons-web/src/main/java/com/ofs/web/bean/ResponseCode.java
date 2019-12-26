package com.ofs.web.bean;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.service.ResponseMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * @author gaoly
 * @version 2019/4/18/10:54
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum ResponseCode {

    OK(200, "成功。"),
    BAD_REQUEST(400, "请求参数有误。"),
    AUTHENTICATE(401, "用户未登录或身份异常。"),
    AUTH_NO(403, "没有权限。"),
    SERVER_ERROR(500, "服务异常。");


    private Integer code;

    private String msg;

    public static List<ResponseMessage> getArrayMessage() {
        ArrayList<ResponseMessage> responseMessages = new ArrayList<>();
        for (SystemCode statusEnum : SystemCode.values()) {
            responseMessages.add(new ResponseMessageBuilder()
                    .code(statusEnum.code)
                    .message(statusEnum.msg)
                    .build());
        }
        return responseMessages;
    }
}
