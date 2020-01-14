package com.ofs.web.base.bean;

import com.ofs.web.utils.ThreadLocalUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * 多语言服务
 *
 * @author ly
 */
@Component
@Slf4j
public class LocaleMessageSourceBean {


    @Autowired
    private MessageSource messageSource;


    /**
     * 按指定的语言key获取相应的国际化信息
     *
     * @param msgKey
     * @return String
     */
    public String getMessage(String msgKey) {
        log.error("locale:" + LocaleContextHolder.getLocale());
        return messageSource.getMessage(msgKey, null, ThreadLocalUtil.getLocaleLang());
    }

    public void setLocale(Locale locale) {
        LocaleContextHolder.setLocale(locale);
    }
}
