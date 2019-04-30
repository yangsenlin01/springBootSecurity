package com.hand.validator;

import com.hand.service.HelloService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author senlin.yang@com.hand-china.com
 * @version V1.0
 * @Date 2019-1-29
 * @description
 */

/**
 * ConstraintValidator 表示需要验证的注解
 * Object 可以解释任意类型（注解使用的地方的那个类型）
 */
public class MyConstraintValidator implements ConstraintValidator<MyConstraint, Object> {

    /**
     * 可以在这个类中注入任何Bean
     */
    @Autowired
    private HelloService helloService;

    @Override
    public void initialize(MyConstraint constraintAnnotation) {
        System.out.println("my validator init");
    }

    /**
     *
     * @param value 被注解的属性的值
     * @param context
     * @return
     */
    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        helloService.greeting("tom");
        System.out.println("valid : " + value);
        return false;
    }
}
