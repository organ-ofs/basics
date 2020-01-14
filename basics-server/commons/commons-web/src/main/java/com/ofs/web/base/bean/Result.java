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
public class Result<T> extends BaseDto {

    private static final long serialVersionUID = 1L;

    //"返回状态", notes = "为false时，请查看msgId以及msgText信息")
    @Setter(AccessLevel.PRIVATE)
    private boolean status;

    //"返回状态码", notes = "只有status为false才会有信息")
    private String msgId;

    //"返回状态说明", notes = "只有status为false才会有信息")
    private String msgText;

    //"返回实体对象", notes = "只有status为true时才会有信息")
    private T data;

    //"加密后的内容", notes = "只有启动加密后才会有信息")
    private String encryptData;

    /**
     * Instantiates a new Response dto.
     * 默认为成功
     */
    public Result() {
        this.status = true;
    }

    /**
     * 成功设置
     *
     * @param data
     */
    public Result(T data) {
        this.status = true;
        this.data = data;
    }

    /**
     * 成功设置
     *
     * @param data
     */
    public Result(boolean status, String msgId, String msgText, T data) {
        this.status = status;
        if (StringUtils.isEmpty(msgId)) {
            this.status = true;
        } else {
            this.status = false;
        }
        this.msgId = msgId;
        this.msgText = msgText;
        this.data = data;
    }

    /**
     * 成功设置
     *
     * @param data
     */
    public Result(boolean status, String msgId, String msgText, T data, String encryptToken) {
        this.status = status;
        if (StringUtils.isEmpty(msgId)) {
            this.status = true;
        } else {
            this.status = false;
        }
        this.msgId = msgId;
        this.msgText = msgText;
        this.data = data;
        this.encryptData = "";
    }

    /**
     * 错误设置
     *
     * @param error
     */
    public Result(BaseErrorException error) {

        this.status = false;
        this.msgId = error.getId();
        this.msgText = error.getMessage();
    }


    /**
     * 错误设置
     *
     * @param msgId
     */
    public Result(IMessageEnum msgId) {
        this(msgId, null);
    }

    /**
     * 错误设置
     *
     * @param msgId
     * @param args
     */
    public Result(IMessageEnum msgId, Object[] args) {
        this.status = false;
        this.msgId = msgId.getCode();
        String messageTemplate = LocaleMessageSourceUtil.getMessage(this.msgId);
        if (StringUtils.isNotEmpty(messageTemplate) && args != null && args.length > 0) {
            messageTemplate = MessageFormat.format(messageTemplate, args);
        }
        this.msgText = messageTemplate;

    }

    /**
     * 对象映射
     *
     * @param result
     */
    public void converter(Result<?> result) {
        this.msgId = result.getMsgId();
        this.status = result.isStatus();
        this.msgText = result.getMsgText();
    }

    public boolean isStatus() {
        return status;
    }


    /**
     * 调协 MSGID 时，status 为False
     *
     * @param msgId
     */
    public void setMsgId(String msgId) {
        this.msgId = msgId;
        this.status = false;
    }
}


