package com.ofs.web.jwt;

import com.ofs.web.constant.CacheConstant;
import com.ofs.web.constant.WebCommonConstant;
import com.ofs.web.exception.AuthTokenErrorException;
import com.ofs.web.knowledge.AuthMessageEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExpiredCredentialsException;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;
import java.util.Deque;

/**
 * @author ly
 */
@Slf4j
public class JwtFilter extends BasicHttpAuthenticationFilter {

    /**
     * 登陆用户列表
     */
    private Cache<String, Deque<Serializable>> cache;

    /**
     * 设置Cache的key的前缀
     */

    public void setCacheManager(CacheManager cacheManager) {

        this.cache = cacheManager.getCache(CacheConstant.SHIRO_KICK_OUT_DEQUE);


    }

    /**
     * 检测header里面是否包含Authorization字段
     */
    @Override
    protected boolean isLoginAttempt(ServletRequest request, ServletResponse response) {
        HttpServletRequest req = (HttpServletRequest) request;
        String authorization = req.getHeader(JwtUtil.DEFAULT_JWT_PARAM);
        return authorization != null;
    }

    /**
     * 登陆验证
     */
    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        String jwtToken = httpServletRequest.getHeader(JwtUtil.DEFAULT_JWT_PARAM);

        jwtToken = JwtUtil.getJwtToken(jwtToken);
        // 拦截测试token
        if (JwtUtil.TEST_TOKEN.equals(jwtToken)) {
            AuthenticationToken token = new JwtToken(jwtToken, "admin", null, "PC", "", "1");
            SecurityUtils.getSubject().login(token);
            return true;
        }
        if (!JwtUtil.isDecode(jwtToken)) {
            log.error("jwt token 错误 :", AuthMessageEnum.FORBIDDEN_TOKEN_ERROR.getMessage());
            throw new AuthTokenErrorException(AuthMessageEnum.FORBIDDEN_TOKEN_ERROR);
        }

        if (JwtUtil.isTokenExpired(jwtToken)) {
            log.error("jwt 过期 :token expired");
            throw new ExpiredCredentialsException("token expired");
        }
        String account = JwtUtil.getAccount(jwtToken);
        String id = JwtUtil.getId(jwtToken);
        String terminal = JwtUtil.getTerminal(jwtToken);


        if (!JwtUtil.verify(jwtToken, account)) {
            log.error("[{}]:jwt验证不通过", account);
            throw new AuthTokenErrorException(AuthMessageEnum.FORBIDDEN_ACCOUNT_ERROR);
        }
        AuthenticationToken token = new JwtToken(jwtToken, account, null, null, terminal, id);
        // 提交给realm进行登入，如果错误他会抛出异常并被捕获
        SecurityUtils.getSubject().login(token);
        // 如果没有抛出异常则代表登入成功，返回true
        ///Boolean refreshFlg = JwtUtil.isRefreshToken(jwtToken);
        httpServletResponse.setHeader(WebCommonConstant.EXPOSE_HEADERS_REFRESH, Boolean.FALSE.toString());
        return true;
    }


    /**
     * 访问权限验证配置
     * <p>
     * 这里我们详细说明下为什么最终返回的都是true，即允许访问
     * 例如我们提供一个地址 GET /article
     * 登入用户和游客看到的内容是不同的
     * 如果在这里返回了false，请求会被直接拦截，用户看不到任何东西
     * 所以我们在这里返回true，Controller中可以通过 subject.isAuthenticated() 来判断用户是否登入
     * 如果有些资源只有登入用户才能访问，我们只需要在方法上面加上 @RequiresAuthentication 注解即可
     * 但是这样做有一个缺点，就是不能够对GET,POST等请求进行分别过滤鉴权(因为我们重写了官方的方法)，但实际上对应用影响不大
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {

        if (isLoginAttempt(request, response)) {
            try {
                executeLogin(request, response);
                return true;
            } catch (Exception e) {
                log.error("jwt 验证失败:", e);
            }
        }
        this.response401(response);
        return false;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) {
        return false;
    }


    /**
     * 将非法请求返回401状态
     */
    private void response401(ServletResponse resp) {
        try {
            WebUtils.toHttp(resp).sendError(HttpServletResponse.SC_UNAUTHORIZED);

        } catch (IOException e) {
            log.error("jwt 验证失败");
        }
    }


}
