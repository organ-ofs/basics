package com.ofs.sys.core.aspect;

import com.alibaba.fastjson.JSON;
import com.ofs.sys.web.dto.ResetPasswordDto;
import com.ofs.sys.web.dto.SignInDto;
import com.ofs.sys.web.entity.SysLog;
import com.ofs.sys.web.entity.SysUser;
import com.ofs.sys.web.service.SysLogService;
import com.ofs.utils.DateUtils;
import com.ofs.utils.IdentifierUtils;
import com.ofs.web.annotation.SysLogs;
import com.ofs.web.jwt.JwtToken;
import com.ofs.web.utils.WebTools;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author gaoly
 * @version 2019/4/27/17:19
 */
@Aspect
@Component
public class SysLogAspect {

    private final SysLogService sysLogService;

    @Autowired
    public SysLogAspect(SysLogService sysLogService) {
        this.sysLogService = sysLogService;
    }

    /**
     * 定义切点
     * 匹配有SysLogs注解的方法
     */
    @Pointcut("@annotation(com.ofs.web.annotation.SysLogs)")
    public void log() {
    }

    @AfterReturning("log()")
    public void after(JoinPoint joinPoint) throws Exception {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        PrincipalCollection spc = null;
        Subject subject = SecurityUtils.getSubject();
        if (subject.getPrincipals() != null) {
            spc = subject.getPrincipals();
        }
        SysLog sysLog = new SysLog();
        sysLog.setId(IdentifierUtils.nextUuid());
        //获取动作Action释义
        sysLog.setContent(getMethodSysLogsAnnotationValue(joinPoint));
        //获取IP
        sysLog.setIp(WebTools.getClientIp(request));
        sysLog.setAjax(WebTools.ajax(request) ? "1" : "0");
        sysLog.setUri(request.getRequestURI());
        String s = this.paramFilter(joinPoint.getArgs());
        //根据系统需求自定义
        sysLog.setParams(s.length() > 500 ? "数据过大，不给予记录" : s);
        sysLog.setHttpMethod(request.getMethod());
        sysLog.setClassMethod(joinPoint.getSignature().getDeclaringTypeName()
                + "." + joinPoint.getSignature().getName() + "()");
        //判断身份是否为空
        if (spc != null) {
            JwtToken jwtToken = new JwtToken();
            BeanUtils.copyProperties(spc.getPrimaryPrincipal(), jwtToken);
            sysLog.setCreateUser(jwtToken.getUid());
        } else {
            sysLog.setCreateUser("0");
        }
        sysLog.setCreateDate(DateUtils.getCurrentTime());
        sysLogService.add(sysLog);
    }

    private String getMethodSysLogsAnnotationValue(JoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        if (method.isAnnotationPresent(SysLogs.class)) {
            //获取方法上注解中表明的权限
            SysLogs sysLogs = method.getAnnotation(SysLogs.class);
            return sysLogs.value();
        }
        return "未知";
    }

    private String paramFilter(Object[] params) {
        List<Object> logArgs = new ArrayList<>();
        //判断是否含有密码敏感数据
        final String filterString = "******";
        if (params.length > 0) {
            for (int i = 0; i < params.length; i++) {
                if (params[i] instanceof SignInDto) {
                    SignInDto sign = (SignInDto) params[i];
                    sign.setPassword(filterString);
                    logArgs.add(sign);
                }
                if (params[i] instanceof SysUser) {
                    SysUser userAddDTO = (SysUser) params[i];
                    userAddDTO.setPassword(filterString);
                    logArgs.add(userAddDTO);
                }
                if (params[i] instanceof ResetPasswordDto) {
                    ResetPasswordDto resetPasswordDTO = (ResetPasswordDto) params[i];
                    resetPasswordDTO.setPassword(filterString);
                    logArgs.add(resetPasswordDTO);
                }
                if (params[i] instanceof ServletRequest || params[i] instanceof ServletResponse || params[i] instanceof MultipartFile) {
                    //ServletRequest不能序列化，从入参里排除，否则报异常：java.lang.IllegalStateException: It is illegal to call this method if the current request is not in asynchronous mode (i.e. isAsyncStarted() returns false)
                    //ServletResponse不能序列化 从入参里排除，否则报异常：java.lang.IllegalStateException: getOutputStream() has already been called for this response
                    continue;
                }
            }
        }
        return JSON.toJSONString(logArgs);
    }


}
