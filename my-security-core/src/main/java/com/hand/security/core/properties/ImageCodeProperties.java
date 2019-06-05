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
public class ImageCodeProperties extends SmsCodeProperties {

    public ImageCodeProperties() {
        setLength(4);
    }

    /**
     * 默认宽度
     */
    private int width = 67;

    /**
     * 默认高度
     */
    private int heigth = 23;

}
