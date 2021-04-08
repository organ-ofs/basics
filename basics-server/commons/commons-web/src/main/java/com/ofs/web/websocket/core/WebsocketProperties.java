package com.ofs.web.websocket.core;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 从配置文件注入的参数
 *
 * @author gaoly
 */
@Component
@ConfigurationProperties(prefix = "frame.websocket", ignoreUnknownFields = false)
public class WebsocketProperties {
    /**
     * WebSocket URL
     */
    private String url;
    /**
     * 白名单
     */
    private String[] allowedOrigins;
    /**
     * 消息最大长度
     */
    private Integer maxTextMessageBufferSize;
    /**
     * 消息最大长度
     */
    private Integer maxBinaryMessageBufferSize;
    /**
     * 消息超时时间
     */
    private Long asyncSendTimeout;
    /**
     * 连接超时时间
     */
    private Long maxSessionIdleTimeout;
    /**
     * PingPong间隔
     */
    private Integer pingPongTime;

    /**
     * @return WebSocket URL
     */
    public String getUrl() {
        return url == null ? "/ws" : url;
    }

    /**
     * @param url WebSocket URL
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * @return 白名单
     */
    public String[] getAllowedOrigins() {
        return allowedOrigins == null ? ArrayUtils.EMPTY_STRING_ARRAY : allowedOrigins;
    }

    /**
     * @param allowedOrigins 白名单
     */
    public void setAllowedOrigins(String[] allowedOrigins) {
        this.allowedOrigins = allowedOrigins;
    }

    /**
     * @return 消息最大长度
     */
    public Integer getMaxTextMessageBufferSize() {
        return maxTextMessageBufferSize == null ? 8388608 : maxTextMessageBufferSize;
    }

    /**
     * @param maxTextMessageBufferSize 消息最大长度
     */
    public void setMaxTextMessageBufferSize(Integer maxTextMessageBufferSize) {
        this.maxTextMessageBufferSize = maxTextMessageBufferSize;
    }

    /**
     * @return 消息最大长度
     */
    public Integer getMaxBinaryMessageBufferSize() {
        return maxBinaryMessageBufferSize == null ? 8388608 : maxBinaryMessageBufferSize;
    }

    /**
     * @param maxBinaryMessageBufferSize 消息最大长度
     */
    public void setMaxBinaryMessageBufferSize(Integer maxBinaryMessageBufferSize) {
        this.maxBinaryMessageBufferSize = maxBinaryMessageBufferSize;
    }

    /**
     * @return 消息超时时间
     */
    public Long getAsyncSendTimeout() {
        return asyncSendTimeout == null ? 30000L : asyncSendTimeout;
    }

    /**
     * @param asyncSendTimeout 消息超时时间
     */
    public void setAsyncSendTimeout(Long asyncSendTimeout) {
        this.asyncSendTimeout = asyncSendTimeout;
    }

    /**
     * @return 连接超时时间
     */
    public Long getMaxSessionIdleTimeout() {
        return maxSessionIdleTimeout == null ? 60000L : maxSessionIdleTimeout;
    }

    /**
     * @param maxSessionIdleTimeout 连接超时时间
     */
    public void setMaxSessionIdleTimeout(Long maxSessionIdleTimeout) {
        this.maxSessionIdleTimeout = maxSessionIdleTimeout;
    }

    /**
     * @return PingPong间隔
     */
    public Integer getPingPongTime() {
        return pingPongTime == null ? 30000 : pingPongTime;
    }

    /**
     * @param pingPongTime PingPong间隔
     */
    public void setPingPongTime(Integer pingPongTime) {
        this.pingPongTime = pingPongTime;
    }

}
