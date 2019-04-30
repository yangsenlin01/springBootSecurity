package com.hand.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author senlin.yang@com.hand-china.com
 * @version V1.0
 * @Date 2019-1-29
 * @description
 */

// 该注解可以在哪些地方使用
@Target({ElementType.METHOD, ElementType.FIELD})
// 运行时注解
@Retention(RetentionPolicy.RUNTIME)
// 该注解的逻辑在该类中执行
@Constraint(validatedBy = MyConstraintValidator.class)
public @interface MyConstraint {

    /**
     * 校验是失败时的信息
     * @return
     */
    String message() default "{org.hibernate.validator.constraints.NotBlank.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
