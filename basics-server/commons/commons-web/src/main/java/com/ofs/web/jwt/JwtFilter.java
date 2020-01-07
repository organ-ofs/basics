package com.ofs.web.jwt;

import com.alibaba.fastjson.JSON;
import com.ofs.web.bean.ResponseResult;
import com.ofs.web.bean.StaticConstant;
import com.ofs.web.bean.SystemCode;
import com.ofs.web.exception.RequestException;
import com.ofs.web.utils.Tools;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtFilter extends BasicHttpAuthenticationFilter {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    /**
     * 判断用户是否想要登入。
     * 检测header里面是否包含Authorization字段即可
     */
    @Override
    protected boolean isLoginAttempt(ServletRequest request, ServletResponse response) {
        HttpServletRequest req = (HttpServletRequest) request;
        String authorization = req.getHeader(StaticConstant.AUTHORIZATION);
        return authorization != null;
    }

    /**
     *
     */
    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) {
        return Tools.executeLogin(request);
    }

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        HttpServletResponse res = WebUtils.toHttp(response);
        if (!isLoginAttempt(request, response)) {
            writerResponse(res, SystemCode.NOT_SING_IN.code, "无身份认证权限标示");
            return false;
        }
        try {
            executeLogin(request, response);
        } catch (RequestException e) {
            writerResponse(res, e.getStatus(), e.getMsg());
            return false;
        }
        Subject subject = getSubject(request, response);
        if (null != mappedValue) {
            String[] value = (String[]) mappedValue;
            for (String permission : value) {
                if (permission == null || "".equals(permission.trim())) {
                    continue;
                }
                if (subject.isPermitted(permission)) {
                    return true;
                }
            }
        }
        //表示没有登录，返回登录提示
        if (null == subject.getPrincipal()) {
            writerResponse(res, SystemCode.NOT_SING_IN.code, SystemCode.NOT_SING_IN.msg);
        } else {
            writerResponse(res, SystemCode.FAIL.code, "无权限访问");
        }
        return false;
    }

    private void writerResponse(HttpServletResponse response, Integer status, String content) {
        response.setHeader("Content-Type", "application/json;charset=utf-8");
        try {
            response.getWriter().write(JSON.toJSONString(ResponseResult.builder()
                    .status(status)
                    .msg(content)
                    .build()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws IOException {
        return false;
    }

}
