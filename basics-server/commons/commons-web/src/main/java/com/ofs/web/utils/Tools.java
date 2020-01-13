package com.ofs.web.utils;

import com.ofs.web.base.bean.SystemCode;
import com.ofs.web.constant.StaticConstant;
import com.ofs.web.exception.RequestException;
import com.ofs.web.jwt.JwtToken;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.subject.Subject;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

/**
 * @author gaoly
 * @version 2019/4/28/9:46
 */
public class Tools {
    public static final String UNKNOWN = "unknown";
    public static final String VERIFYFAIL = "verifyFail";

    /**
     * 获取客户端IP
     *
     * @param request
     * @return
     */
    public static String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Real-IP");
        if (ip == null || "".equals(ip.trim()) || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Forwarded-For");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if ("0.0.0.0".equals(ip) || "0:0:0:0:0:0:0:1".equals(ip) || "localhost".equals(ip) || "127.0.0.1".equals(ip)) {
            ip = "127.0.0.1";
        }
        return ip;
    }

    /**
     * 判断请求是否是Ajax
     *
     * @param request
     * @return
     */
    public static boolean ajax(HttpServletRequest request) {
        String accept = request.getHeader("accept");
        return accept != null && accept.contains("application/json") || (request.getHeader("X-Requested-With") != null && request.getHeader("X-Requested-With").contains("XMLHttpRequest"));
    }

    public static boolean executeLogin(ServletRequest request) {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String authorization = httpServletRequest.getHeader(StaticConstant.AUTHORIZATION);
        if (authorization == null || "".equals(authorization.trim())) {
            throw RequestException.fail("未含授权标示，禁止访问");
        }
        JwtToken token = new JwtToken(authorization, null, null);
        // 提交给realm进行登入，如果错误他会抛出异常并被捕获
        Subject subject = SecurityUtils.getSubject();
        try {
            subject.login(token);
        } catch (DisabledAccountException e) {
            if (VERIFYFAIL.equals(e.getMessage())) {
                throw new RequestException(SystemCode.NOT_SING_IN.code, "身份已过期，请重新登录", e);
            }
            throw new RequestException(SystemCode.SIGN_IN_INPUT_FAIL.code, e.getMessage(), e);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RequestException(SystemCode.SIGN_IN_FAIL, e);
        }
        // 如果没有抛出异常则代表登入成功，返回true
        return true;
    }

    /**
     * 登陆验证 根据Authorization
     */
    public static synchronized void executeLogin() {
        HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        boolean b = Tools.executeLogin(request);
        if (!b) {
            throw RequestException.fail("身份已过期或无效，请重新认证");
        }
    }

}
