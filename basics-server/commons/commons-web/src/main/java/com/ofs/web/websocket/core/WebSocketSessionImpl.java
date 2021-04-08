package com.ofs.web.websocket.core;

import com.ofs.web.utils.FastJsonUtil;
import com.ofs.web.websocket.IWebSocketSession;
import com.ofs.web.websocket.WsMsgParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * WebSocket模块对外开放的类
 *
 * @author gaoly
 */
@Slf4j
public class WebSocketSessionImpl implements IWebSocketSession {

    /**
     * SessionId
     */
    private String sessionId;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * WebSocketSession, 这里使用WeakHashMap，无论何时都可以回收WebSocketSession的内存
     */
    private final WeakHashMap<WebSocketSession, String> sessionMap;

    /**
     * Session属性
     */
    private final ConcurrentHashMap<String, Object> attributes;

    /**
     * 远程地址
     */
    private String remoteAddress;

    /**
     * 本地地址
     */
    private String localAddress;

    /**
     * 构造方法
     */
    WebSocketSessionImpl(WebSocketSession session, String userId) {
        this.userId = userId;
        UUID uuid = UUID.randomUUID();
        this.sessionId = String.format("%016x%016x", uuid.getLeastSignificantBits(), uuid.getMostSignificantBits());
        remoteAddress = session.getRemoteAddress().toString();
        localAddress = session.getLocalAddress().toString();
        this.attributes = new ConcurrentHashMap<>(16);
        this.sessionMap = new WeakHashMap<>(1);
        this.sessionMap.put(session, this.sessionId);
    }

    /**
     * @return SessionId
     */
    @Override
    public String getSessionId() {
        return sessionId;
    }

    /**
     * @return 用户ID
     */
    @Override
    public String getUserId() {
        return userId;
    }

    /**
     * 发送消息
     *
     * @param message 消息
     * @return 是否发送成功
     */
    @Override
    public boolean sendMessage(String message) {
        return sendMessage(new TextMessage(message));
    }

    /**
     * 将对象系列化为JSON后，发送消息
     *
     * @param message 消息
     * @return 是否发送成功
     */
    @Override
    public boolean sendMessage(WsMsgParam<?> message) {
        return sendMessage(new TextMessage(FastJsonUtil.toJson(message)));
    }

    boolean sendMessage(WebSocketMessage<?> message) {
        WebSocketSession session = getSession();
        if (session == null) {
            return false;
        }
        try {
            if (!session.isOpen()) {
                return false;
            }
            synchronized (session) {
                session.sendMessage(message);
            }
            return true;
        } catch (IOException e) {
            log.error("发送WebSocket消息失败。", e);
            return false;
        }
    }

    /**
     * 取得连接状态
     *
     * @return 连接状态
     */
    @Override
    public boolean isOpen() {
        WebSocketSession session = getSession();
        return session == null ? false : session.isOpen();
    }

    WebSocketSession getSession() {
        Set<WebSocketSession> sessionSet = sessionMap.keySet();
        for (WebSocketSession session : sessionSet) {
            if (session != null) {
                return session;
            }
        }
        return null;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getRemoteAddress() {
        return remoteAddress;
    }

    @Override
    public String getLocalAddress() {
        return localAddress;
    }

}
