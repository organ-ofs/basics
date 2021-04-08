package com.ofs.web.websocket;

import java.util.Map;

/**
 * WebSocket Session
 *
 * @author gaoly
 */
public interface IWebSocketSession {

    /**
     * @return SessionId
     */
    String getSessionId();

    /**
     * @return 用户ID
     */
    String getUserId();

    /**
     * 发送消息
     *
     * @param message 消息
     * @return 是否发送成功
     */
    boolean sendMessage(String message);

    /**
     * 将对象系列化为JSON后，发送消息
     *
     * @param message 消息
     * @return 是否发送成功
     */
    boolean sendMessage(WsMsgParam<?> message);

    /**
     * 取得连接状态
     *
     * @return 连接状态
     */
    boolean isOpen();

    /**
     * 取得Session属性
     *
     * @return
     */
    Map<String, Object> getAttributes();

    default Object getAttribute(String key) {
        return getAttributes().get(key);
    }

    default void setAttribute(String key, Object value) {
        getAttributes().put(key, value);
    }

    /**
     * 取得远程地址
     *
     * @return 远程地址
     */
    String getRemoteAddress();

    /**
     * 取得本地地址
     *
     * @return 本地地址
     */
    String getLocalAddress();
}
