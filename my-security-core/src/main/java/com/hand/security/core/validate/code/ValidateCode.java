package com.hand.security.core.validate.code;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * @author senlin.yang@hand-china.com
 * @version V1.0
 * @Date 2019-6-5
 * @description 实现Serializable接口
 * 使用session集群时如果用到第三方存储工具，验证码需要支持序列化和反序列化
 */

@Getter
@Setter
public class ValidateCode implements Serializable {

    private static final long serialVersionUID = -745588883833550246L;

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
