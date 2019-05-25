package com.hand.security.core.validate.code;

import org.springframework.security.core.AuthenticationException;

/**
 * @author senlin.yang@hand-china.com
 * @version V1.0
 * @Date 2019-5-25
 * @description 集成AuthenticationException并覆盖ValidateCodeException方法即可
 */
public class ValidateCodeException extends AuthenticationException {


    private static final long serialVersionUID = 5010544894984465853L;

    public ValidateCodeException(String msg) {
        super(msg);
    }
}
