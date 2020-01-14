package com.ofs.web.utils;

import com.ofs.web.base.bean.LocaleMessageSourceBean;
import com.ofs.web.base.component.ApplicationContextComponent;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.text.MessageFormat;

/**
 * 多语言服务UTIL
 *
 * @author ly
 */
@Slf4j
public class LocaleMessageSourceUtil {

    private LocaleMessageSourceUtil() {

    }

    /**
     * 方便静态使用
     *
     * @param msgKey
     * @return
     */
    public static String getMessage(String msgKey) {
        try {

            LocaleMessageSourceBean localeMessageSourceBean = ApplicationContextComponent.getBeanByType(LocaleMessageSourceBean.class);
            return localeMessageSourceBean.getMessage(msgKey);

        } catch (Exception e) {
            log.error("LocaleMessageSourceUtil", e.getMessage());
            return "";
        }
    }


    /**
     * 方便静态使用
     *
     * @param msgKey
     * @return
     */
    public static String getMessage(String msgKey, Object[] args) {
        try {
            LocaleMessageSourceBean localeMessageSourceBean = ApplicationContextComponent.getBeanByType(LocaleMessageSourceBean.class);
            String messageTemplate = localeMessageSourceBean.getMessage(msgKey);
            if (StringUtils.isNotEmpty(messageTemplate) && args != null && args.length > 0) {
                messageTemplate = MessageFormat.format(messageTemplate, args);
            }
            return messageTemplate;
        } catch (Exception e) {
            log.error("LocaleMessageSourceUtil", e);
            return "";
        }
    }
}
