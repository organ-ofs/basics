package com.ofs.web.websocket;

import lombok.Data;

/**
 * WebSocket发送消息的共通数据类型
 *
 * @param <T> 实际数据类型
 * @author gaoly
 */
@Data
public class WsMsgParam<T> {
    /**
     * 所属模块
     */
    private String module;
    /**
     * 操作类型
     */
    private String operate;
    /**
     * 数据
     */
    private T data;
}
