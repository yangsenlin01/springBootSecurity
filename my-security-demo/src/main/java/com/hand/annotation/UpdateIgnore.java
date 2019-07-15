package com.hand.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author senlin.yang@hand-china.com
 * @version V1.0
 * @Date 2019-7-15
 * @description 更新时不需要修改的字段
 */

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UpdateIgnore {
}
