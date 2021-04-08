package com.ofs.web.websocket;

import java.util.List;

/**
 * WebSocket服务类
 *
 * @author gaoly
 */
public interface IWebSocketService {
    /**
     * 获取所有的Session
     *
     * @return 所有网页端的WebSocket session
     */
    List<IWebSocketSession> listAllSessionData();

    /**
     * 添加需要监听来自客户端WebSocket消息的监听服务
     *
     * @param module
     * @param listener WebSocket消息的监听服务
     */

    void addListener(String module, IWebSocketListener listener);

    /**
     * 删除WebSocket消息的监听服务
     *
     * @param module
     * @param listener WebSocket消息的监听服务
     */
    void removeListener(String module, IWebSocketListener listener);
}
