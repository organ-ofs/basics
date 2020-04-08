package com.ofs.web.handler;


import com.ofs.web.base.bean.Result;
import com.ofs.web.base.bean.SystemCode;
import com.ofs.web.exception.RequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author gaoly
 * @version 2017/11/18
 */
@ControllerAdvice(basePackages = {"cn.ofs.web"})
@Slf4j
public class DefaultExceptionHandler {

    /**
     * 处理所有的Controller层面的异常
     * 如果这里添加 @ResponseBody 注解 表示抛出的异常以 Rest 的方式返回，这时就系统就不会指向到错误页面 /error
     */
    @ExceptionHandler(value = RequestException.class)
    @ResponseBody
    public Result requestExceptionHandler(RequestException e) {
        if (e.getE() != null) {
            e.printStackTrace();
        }

        return Result.builder().msg(e.getMsg()).code(e.getStatus()).build();
    }


    @ExceptionHandler(value = DataIntegrityViolationException.class)
    @ResponseBody
    public Result requestExceptionHandler(DataIntegrityViolationException e) {
        return Result.builder().msg("数据操作格式异常").code(SystemCode.FAIL.code).build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public Result methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        BindingResult result = e.getBindingResult();
        String s = "参数验证失败";
        if (result.hasErrors()) {
            List<ObjectError> errors = result.getAllErrors();
            s = errors.get(0).getDefaultMessage();
        }
        return Result.builder().code(SystemCode.FAIL.code).msg(s).build();
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Result requestExceptionHandler(Exception e) {
        e.printStackTrace();
        return Result.builder().msg("服务器飘了，管理员去拿刀修理了~").code(SystemCode.FAIL.code).build();
    }


}
