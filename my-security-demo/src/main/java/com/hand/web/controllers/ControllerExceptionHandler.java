package com.hand.web.controllers;

import com.hand.exception.UserNotExistException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

/**
 * @author senlin.yang@com.hand-china.com
 * @version V1.0
 * @Date 2019-1-29
 * @description
 */

// 声明为controller的通知
@ControllerAdvice
public class ControllerExceptionHandler {

    // 拦截的是这个类型的异常
    @ExceptionHandler(UserNotExistException.class)
    // 返回的是json格式
    @ResponseBody
    // 返回的响应码
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, Object> handleUserNotExistException(UserNotExistException ex) {
        // 使用mam封装我们想要的返回信息
        Map<String, Object> map = new HashMap<>();
        map.put("id", ex.getId());
        map.put("message", ex.getMessage());
        return map;
    }

}
