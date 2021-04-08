package com.ofs.web.websocket.core;

import com.ofs.web.utils.FastJsonUtil;
import com.ofs.web.websocket.IWebSocketListener;
import com.ofs.web.websocket.IWebSocketService;
import com.ofs.web.websocket.IWebSocketSession;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.PingMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author ly
 */
@Slf4j
@Component
public class WebSocketServiceImpl implements IWebSocketService, InitializingBean, DisposableBean {

    private static final String MODULE_KEY = "module";
    private final ConcurrentHashMap<WebSocketSession, WebSocketSessionImpl> allSessionMap = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, CopyOnWriteArraySet<IWebSocketListener>> listenerMap = new ConcurrentHashMap<>();
    private final ScheduledThreadPoolExecutor pingPongExecutor = new ScheduledThreadPoolExecutor(8);

    @Autowired
    private WebsocketProperties websocketProperties;

    /**
     * 加入新的Session
     *
     * @param session
     */
    IWebSocketSession addSession(WebSocketSession session, String userId) {
        WebSocketSessionImpl sessionData = new WebSocketSessionImpl(session, userId);
        allSessionMap.put(session, sessionData);
        return sessionData;
    }

    /**
     * 移除Session
     *
     * @param session
     */
    void removeSession(WebSocketSession session) {
        allSessionMap.remove(session);
    }

    /**
     * 取得Session
     *
     * @param session
     * @return
     */
    WebSocketSessionImpl getDataBySession(WebSocketSession session) {
        return allSessionMap.get(session);
    }

    /**
     * 获取所有的Session
     *
     * @return 所有网页端的WebSocket session
     */
    @Override
    public List<IWebSocketSession> listAllSessionData() {
        List<IWebSocketSession> dataList = new ArrayList<>(allSessionMap.size());
        dataList.addAll(allSessionMap.values());
        return dataList;
    }

    /**
     * 添加需要监听来自客户端WebSocket消息的监听服务
     *
     * @param listener WebSocket消息的监听服务
     */
    @Override
    public void addListener(String module, IWebSocketListener listener) {
        CopyOnWriteArraySet<IWebSocketListener> listenerSet = listenerMap.computeIfAbsent(module,
                k -> new CopyOnWriteArraySet<>());
        listenerSet.add(listener);
    }

    /**
     * 删除WebSocket消息的监听服务
     *
     * @param listener WebSocket消息的监听服务
     */
    @Override
    public void removeListener(String module, IWebSocketListener listener) {
        CopyOnWriteArraySet<IWebSocketListener> listenerSet = listenerMap.get(module);
        if (listenerSet != null) {
            listenerSet.remove(listener);
        }
    }

    /**
     * 接收消息
     *
     * @param message
     */
    void receiveMessage(IWebSocketSession sessionData, String message) {
        Map<String, Object> msgMap = FastJsonUtil.getMap(message);
        if (msgMap != null) {
            String module = Objects.toString(msgMap.get(MODULE_KEY), StringUtils.EMPTY);
            if (!module.isEmpty()) {
                CopyOnWriteArraySet<IWebSocketListener> listenerSet = listenerMap.get(module);
                if (listenerSet != null) {
                    List<IWebSocketListener> listenerList = new ArrayList<>(listenerSet.size());
                    listenerList.addAll(listenerSet);
                    for (IWebSocketListener listener : listenerList) {
                        listener.receiveMessage(sessionData, message);
                    }
                }
            }
        }
    }

    /**
     * 关闭连接
     */
    public void closeSession(WebSocketSessionImpl sessionData) {
        WebSocketSession session = sessionData.getSession();
        if (session != null) {
            try {
                session.close();
            } catch (IOException e) {
                log.error("关闭WebSocket连接失败。", e);
            }
            removeSession(session);
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        pingPongExecutor.scheduleAtFixedRate(
                () -> {
                    List<IWebSocketSession> allSessions = this.listAllSessionData();
                    for (IWebSocketSession session : allSessions) {
                        WebSocketSessionImpl sessionImpl = (WebSocketSessionImpl) session;
                        sessionImpl.sendMessage(new PingMessage());
                    }
                },
                websocketProperties.getPingPongTime(),
                websocketProperties.getPingPongTime(),
                TimeUnit.MILLISECONDS);
    }

    @Override
    public void destroy() throws Exception {
        pingPongExecutor.shutdownNow();
    }
}
