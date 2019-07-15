package com.hand.security.core.support;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author senlin.yang@hand-china.com
 * @version V1.0
 * @Date 2019-4-30
 * @description 用于返回给前台的对象
 */
@ToString
@Setter
@Getter
public class SimpleResponse {

    private Object content;

    public SimpleResponse(Object content) {
        this.content = content;
    }
}
