package com.ofs.web.websocket.core;

import com.ofs.web.auth.model.ShiroUser;
import com.ofs.web.auth.service.ShiroService;
import com.ofs.web.jwt.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.HashMap;
import java.util.Map;

/**
 * 授权拦截
 *
 * @author gaoly
 */
@Component
@Slf4j
class WsHandshakeInterceptor implements HandshakeInterceptor {

    @Autowired
    @Lazy
    private ShiroService authService;

    @Override
    public boolean beforeHandshake(
            ServerHttpRequest request,
            ServerHttpResponse response,
            WebSocketHandler wsHandler,
            Map<String, Object> attributes)
            throws Exception {
        String query = request.getURI().getRawQuery();
        if (StringUtils.isEmpty(query)) {
            return false;
        }
        String[] queryArray = query.split(WebsocketConstance.URL_SPLIT_REGEX);
        if (queryArray == null) {
            return false;
        }

        Map<String, String> paramMap = new HashMap<>(16);
        for (String param : queryArray) {
            if (StringUtils.isEmpty(param)) {
                continue;
            }
            int idx = param.indexOf(WebsocketConstance.URL_KV_SPLIT);
            String key = param.substring(0, idx);
            String val = param.substring(idx + 1);
            paramMap.put(key, val);
        }

        String token = paramMap.get(WebsocketConstance.REQUEST_TOKEN);

        // 通过token取当前用户
        ShiroUser shiroUser = null;
        try {
            String account = JwtUtil.getAccount(token);
            if (StringUtils.isNoneEmpty(account)) {
                shiroUser = authService.getUserByAccount(account);
            }
        } catch (Exception e) {
            log.warn(e.toString(), e);
            log.warn("无法解析的token, 客户端地址:{}, token:{}", request.getRemoteAddress(), token);
            return false;
        }
        if (shiroUser == null) {
            log.warn("无效的token, 客户端地址:{}, token:{}", request.getRemoteAddress(), token);
            return false;
        }

        attributes.put(WebsocketConstance.SESSION_USER_ID, shiroUser.getId());
        attributes.put(WebsocketConstance.SESSION_URL_PARAM, paramMap);

        return true;
    }

    @Override
    public void afterHandshake(
            ServerHttpRequest request,
            ServerHttpResponse response,
            WebSocketHandler wsHandler,
            Exception exception) {
        // Do nothing because 连接成功后这里不需要处理
    }

}
