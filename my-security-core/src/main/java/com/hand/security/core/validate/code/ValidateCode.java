package com.hand.security.core.validate.code;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


/**
 * @author senlin.yang@hand-china.com
 * @version V1.0
 * @Date 2019-6-5
 * @description
 */

@Getter
@Setter
public class ValidateCode {

    private String code;

    private LocalDateTime expireTime;

    public ValidateCode(String code, int expireIn) {
        this.code = code;
        this.expireTime = LocalDateTime.now().plusSeconds(expireIn);
    }

    public ValidateCode(String code, LocalDateTime expireTime) {
        this.code = code;
        this.expireTime = expireTime;
    }

    public boolean isExpried() {
        return LocalDateTime.now().isAfter(expireTime);
    }

}
