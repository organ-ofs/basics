package com.ofs.web.resolver;

import com.ofs.web.annotation.JwtClaim;
import com.ofs.web.base.bean.SystemCode;
import com.ofs.web.constant.StaticConstant;
import com.ofs.web.exception.RequestException;
import com.ofs.web.jwt.JwtUtil;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;

/**
 * @author gaoly
 * @version 2019/10/16
 * 自定义参数解析器
 * supportsParameter：用于判定是否需要处理该参数分解，返回true为需要，并会去调用下面的方法resolveArgument。
 * resolveArgument：真正用于处理参数分解的方法，返回的Object就是controller方法上的形参对象。
 */
public class JwtTokenArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(JwtClaim.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        String authorization = request.getHeader(StaticConstant.AUTHORIZATION);
        String result = null;
        JwtClaim token = null;
        if (authorization != null) {
            Annotation[] methodAnnotations = parameter.getParameterAnnotations();
            for (Annotation methodAnnotation : methodAnnotations) {
                if (methodAnnotation instanceof JwtClaim) {
                    token = (JwtClaim) methodAnnotation;
                    break;
                }
            }
            if (token != null) {
                result = JwtUtil.get(authorization, token.value());
            }
        }
        if (result != null) {
            return result;
        }
        if (token == null || token.exception()) {
            throw new RequestException(SystemCode.NOT_SING_IN);
        } else {
            return null;
        }
    }
}
