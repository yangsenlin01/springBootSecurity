package com.hand.security.core.validate.code;

import org.springframework.web.context.request.ServletWebRequest;

/**
 * @author senlin.yang@hand-china.com
 * @version V1.0
 * @Date 2019-5-25
 * @description 验证码生成接口
 */
public interface ValidateCodeGenerator {

    /**
     * 验证码生成方法
     *
     * @param request
     * @return
     */
    ValidateCode generate(ServletWebRequest request);

}
