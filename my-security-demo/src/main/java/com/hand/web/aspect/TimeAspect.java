package com.hand.web.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * @author senlin.yang@com.hand-china.com
 * @version V1.0
 * @Date 2019-3-28
 * @description
 */

@Aspect
@Component
public class TimeAspect {

    /**监听UserController的任何方法*/
    @Around("execution(* com.hand.web.controllers.UserController.*(..))")
    public Object handleControllerMethod(ProceedingJoinPoint pjp) throws Throwable {
        System.out.println("time aspect start");
        // 接收传入控制器的参数
        Object[] args = pjp.getArgs();
        for (Object arg : args) {
            System.out.println("arg is " + arg);
        }
        Long startTime = System.currentTimeMillis();
        // 调用控制器方法并接收控制器的返回结果
        Object object = pjp.proceed();
        System.out.println("time aspect 耗时：" + (System.currentTimeMillis() - startTime));
        System.out.println("time aspect end");
        // 返回控制器的结果
        return object;
    }

}
