package com.ofs.web.exception;

import com.ofs.web.utils.LocaleMessageSourceUtil;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.text.MessageFormat;

/**
 * @author ly
 */
public abstract class BaseErrorException extends RuntimeException implements Serializable {


    public BaseErrorException() {
    }

    public BaseErrorException(Exception e) {
        super(e);
    }

    public BaseErrorException(Throwable e) {
        super(e);
    }

    public BaseErrorException(String message) {
        super(message);
    }


    /**
     * 返回枚举对应的消息ID
     *
     * @return 消息ID
     */
    public abstract String getId();

    /**
     * 返回枚举对应的消息ID
     *
     * @return 消息ID
     */
    public abstract Object[] getArgs();


    /**
     * 返回枚举对应的消息内容(中文)
     *
     * @return 消息内容(中文)
     */
    @Override
    public String getMessage() {

        String messageTemplate = LocaleMessageSourceUtil.getMessage(this.getId());
        if (StringUtils.isNotEmpty(messageTemplate) && this.getArgs() != null && this.getArgs().length > 0) {
            messageTemplate = MessageFormat.format(messageTemplate, this.getArgs());

        }
        return messageTemplate;

    }


}
