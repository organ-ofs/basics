package com.ofs.web.websocket.core;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.PongMessage;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;

/**
 * WebSocket Handler
 *
 * @author gaoly
 */
@Slf4j
@Component
class WebSocketHandler extends TextWebSocketHandler {

    @Autowired
    private WebSocketServiceImpl qmWebSocketService;

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        WebSocketSessionImpl sessionData = qmWebSocketService.getDataBySession(session);
        String data = message.getPayload();
        log.debug("接收到数据：{}", data);
        qmWebSocketService.receiveMessage(sessionData, data);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String userId = (String) session.getAttributes().get(WebsocketConstance.SESSION_USER_ID);
        log.debug("新连接接入,IP:{}, 用户ID:{}", session.getRemoteAddress(), userId);
        qmWebSocketService.addSession(session, userId);
    }

    @Override
    protected void handlePongMessage(WebSocketSession session, PongMessage message) throws Exception {
        WebSocketSessionImpl sessionData = qmWebSocketService.getDataBySession(session);
        if (sessionData == null) {
            session.close();
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        qmWebSocketService.removeSession(session);
    }

}
