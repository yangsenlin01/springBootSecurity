package com.hand.security.core.properties;

import lombok.Getter;
import lombok.Setter;

/**
 * @author senlin.yang@hand-china.com
 * @version V1.0
 * @Date 2019-5-25
 * @description 验证码等信息，包括图片验证码、手机验证码
 */

@Getter
@Setter
public class ValidateCodeProperties {

    private ImageCodeProperties image = new ImageCodeProperties();

}
