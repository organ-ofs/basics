package com.ofs.web.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEvent;

/**
 * @author: ly
 * @date: 2018/11/29 19:57
 */
@Slf4j
public class LogoutEvent extends ApplicationEvent {
    public LogoutEvent(Object source) {
        super(source);
    }

    public String getAuthId() {
        return (String) this.source;
    }

}

