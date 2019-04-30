package com.hand.web.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author senlin.yang@com.hand-china.com
 * @version V1.0
 * @Date 2019-3-28
 * @description
 */

@Component
public class TimeInterceptor implements HandlerInterceptor{

    /**控制器被调用之前调用此方法*/
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        System.out.println("preHandle");

        // interceptor与filter相比，多了一个Object参数，可以获取控制器的信息
        // 获取控制器名称
        System.out.println(((HandlerMethod)o).getBean().getClass().getName());
        // 获取当前请求的控制器方法
        System.out.println(((HandlerMethod)o).getMethod().getName());

        // 可以使用http传递参数
        httpServletRequest.setAttribute("startTime", System.currentTimeMillis());

        // 这里return false的话直接返回请求，不会再调控制器以及后面的方法
        return true;
    }

    /**控制器被调用之后调用此方法，若控制器发生异常将不会调用此方法，直接走后面的afterCompetion*/
    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        System.out.println("postHandle");
        Long startTime = (Long) httpServletRequest.getAttribute("startTime");
        System.out.println("time interceptor 耗时: " + (System.currentTimeMillis() - startTime));
    }

    /**不管是否发成异常，最后都会调用此方法*/
    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        System.out.println("afterCompletion");
        Long startTime = (Long) httpServletRequest.getAttribute("startTime");
        System.out.println("time interceptor 耗时: " + (System.currentTimeMillis() - startTime));
        // 如果控制器有异常，可以在这里捕获
        System.out.println("ex is " + e);
    }
}
