package com.ofs.web.websocket;

/**
 * 如果需要通过WebSocket接收来自网页的消息，需要继承这个接口
 *
 * @author gaoly
 * @see IWebSocketService#addListener
 */
@FunctionalInterface
public interface IWebSocketListener {

    /**
     * 接收来自网页侧消息<br>
     * 当有来自客户端的WebSocket消息时，会触发此方法
     *
     * @param session
     * @param message
     */
    void receiveMessage(IWebSocketSession session, String message);
}
