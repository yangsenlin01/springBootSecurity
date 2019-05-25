package com.hand.security.core.properties;

import lombok.Getter;
import lombok.Setter;

/**
 * @author senlin.yang@hand-china.com
 * @version V1.0
 * @Date 2019-5-25
 * @description 图片验证码默认配置
 */

@Getter
@Setter
public class ImageCodeProperties {

    /**
     * 默认宽度
     */
    private int width = 67;

    /**
     * 默认高度
     */
    private int heigth = 23;

    /**
     * 默认位数
     */
    private int length = 4;

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
