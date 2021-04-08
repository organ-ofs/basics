package com.ofs.web.listener;

import com.ofs.web.event.LogoutEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @author: ly
 * @date: 2018/11/29 20:03
 */
@Component
@Slf4j
public class LogoutListener {

    // 监听到用户退出后执行的操作
    @EventListener
    @Async
    public void onLogoutEvent(LogoutEvent event) {
        String staffId = event.getAuthId();
        log.info("-------LogoutListener:[{}]", staffId);


    }

}
