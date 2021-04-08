package com.ofs.web.websocket.core;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistration;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;

/**
 * WebSocket配置
 *
 * @author gaoly
 */
@Configuration
@EnableWebSocket
@Slf4j
public class WebSocketConfig implements WebSocketConfigurer {

    @Autowired
    private WebSocketHandler webSocketHandler;

    @Autowired
    private WsHandshakeInterceptor handshakeInterceptor;

    @Autowired
    private WebsocketProperties websocketProperties;

    /**
     * 注册服务
     */
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        WebSocketHandlerRegistration reg = registry.addHandler(webSocketHandler, websocketProperties.getUrl());
        reg.setAllowedOrigins(websocketProperties.getAllowedOrigins());
        reg.addInterceptors(handshakeInterceptor);
        log.debug("绑定WebSocket: {}", websocketProperties.getUrl());
    }

    /**
     * ServletServerContainerFactoryBean配置
     *
     * @return ServletServerContainerFactoryBean
     */
    @Bean("QmWebSocketConfig.createWebSocketContainer")
    public ServletServerContainerFactoryBean createWebSocketContainer() {
        ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean();
        container.setMaxTextMessageBufferSize(8388608);
        container.setMaxBinaryMessageBufferSize(8388608);
        container.setAsyncSendTimeout(30000L);
        container.setMaxSessionIdleTimeout(60000L);
        return container;
    }

}
