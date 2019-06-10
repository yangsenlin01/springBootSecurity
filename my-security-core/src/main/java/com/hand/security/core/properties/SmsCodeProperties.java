package com.hand.security.core.properties;

import lombok.Getter;
import lombok.Setter;

/**
 * @author senlin.yang@hand-china.com
 * @version V1.0
 * @Date 2019-6-5
 * @description 短信验证码配置
 */

@Getter
@Setter
public class SmsCodeProperties {

    /**
     * 默认位数
     */
    private int length = 6;

    /**
     * 默认失效时间
     */
    private int expireIn = 60;

    /**
     * 需要使用验证码认证的接口，多个可使用逗号隔开
     * 比如：hand.security.code.image.url = /user,/user/*
     */
    private String urls;

}
