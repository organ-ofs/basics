package com.ofs.web.exception;


import com.ofs.web.knowledge.IMessageEnum;

/**
 * @author ly
 * @version V1.0
 * @Title: GlobalErrorException.java
 * @Package com.frame.common.base.exception
 * @Description: 全局异常
 * @date 2019-6-25 下午5:18:03
 */

public class GlobalErrorException extends BaseErrorException {


    private IMessageEnum messageEnum;

    private Object[] args;

    protected GlobalErrorException() {

    }

    public GlobalErrorException(IMessageEnum messageEnum, Object[] args) {
        super(messageEnum.getMessage());
        this.args = args;
        this.messageEnum = messageEnum;

    }

    public GlobalErrorException(IMessageEnum messageEnum) {
        this(messageEnum, null);

    }

    /**
     * 返回枚举对应的消息ID
     *
     * @return 消息ID
     */
    @Override
    public String getId() {
        return this.messageEnum.getCode();
    }

    /**
     * 返回枚举对应的消息ID
     *
     * @return 消息ID
     */
    @Override
    public Object[] getArgs() {
        return this.args;
    }

}
