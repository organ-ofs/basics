package com.ofs.web.base.bean;

import com.ofs.web.base.BaseDto;
import com.ofs.web.exception.BaseErrorException;
import com.ofs.web.knowledge.IMessageEnum;
import com.ofs.web.utils.LocaleMessageSourceUtil;
import lombok.*;
import org.apache.commons.lang3.StringUtils;

import java.text.MessageFormat;

/**
 * API通过返回数据集体一
 *
 * @param <T> the type parameter
 * @author ly
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ToString
@Builder
@AllArgsConstructor
public class Result<T> extends BaseDto {

    private static final long serialVersionUID = 1L;

    //"返回状态", notes = "为false时，请查看msgId以及msgText信息")
    @Setter(AccessLevel.PRIVATE)
    private boolean status;

    //"返回状态码", notes = "只有status为false才会有信息")
    private String code;

    //"返回状态说明", notes = "只有status为false才会有信息")
    private String msg;

    //"返回实体对象", notes = "只有status为true时才会有信息")
    private T data;

    //"加密后的内容", notes = "只有启动加密后才会有信息")
    private String encryptData;

    public Result() {
        this.status = true;
        this.code = ResultCode.OK.getCode();
        this.msg = ResultCode.OK.getMessage();
    }

    /**
     * Instantiates a new Response dto.
     * 默认为成功
     */
    public static Result result() {
        return Result.builder()
                .status(true)
                .code(ResultCode.OK.getCode())
                .msg(ResultCode.OK.getMessage())
                .build();
    }

    /**
     * 成功设置
     *
     * @param data
     */
    public static <T> Result result(T data) {
        return Result.builder()
                .status(true)
                .code(ResultCode.OK.getCode())
                .msg(ResultCode.OK.getMessage())
                .data(data)
                .build();
    }

    /**
     * 成功设置
     *
     * @param data
     */
    public static <T> Result result(boolean status, String msgId, String msgText, T data) {
        return Result.builder()
                .status(status)
                .code(msgId)
                .msg(msgText)
                .data(data)
                .build();
    }


    /**
     * 错误设置
     */
    public static Result error() {
        return Result.builder()
                .status(false)
                .code(ResultCode.ERROR.getCode())
                .msg(ResultCode.ERROR.getMessage())
                .build();
    }

    /**
     * 错误设置
     *
     * @param error
     */
    public static <T> Result error(BaseErrorException error) {
        return Result.builder()
                .status(false)
                .code(error.getId())
                .msg(error.getMessage())
                .build();
    }


    /**
     * 错误设置
     *
     * @param msgId
     */
    public static Result error(IMessageEnum msgId) {
        return error(msgId, null);
    }

    /**
     * 错误设置
     *
     * @param msgId
     * @param args
     */
    public static Result error(IMessageEnum msgId, Object[] args) {
        String code = msgId.getCode();
        String messageTemplate = LocaleMessageSourceUtil.getMessage(code);
        if (StringUtils.isNotEmpty(messageTemplate) && args != null && args.length > 0) {
            messageTemplate = MessageFormat.format(messageTemplate, args);
        }
        return Result.builder()
                .status(false)
                .code(code)
                .msg(messageTemplate)
                .build();

    }

    /**
     * 对象映射
     *
     * @param result
     */
    public void converter(Result<?> result) {
        this.code = result.getCode();
        this.status = result.isStatus();
        this.msg = result.getMsg();
    }

    public boolean isStatus() {
        return status;
    }

}


