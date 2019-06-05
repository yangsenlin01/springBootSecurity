package com.hand.security.core.validate.code.sms;

import com.hand.security.core.properties.SecurityProperties;
import com.hand.security.core.validate.code.ValidateCode;
import com.hand.security.core.validate.code.ValidateCodeGenerator;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;

/**
 * @author senlin.yang@hand-china.com
 * @version V1.0
 * @Date 2019-6-5
 * @description
 */
@Component("smsValidateCodeGenerator")
@Getter
@Setter
public class SmsCodeGenerator implements ValidateCodeGenerator {

    @Autowired
    private SecurityProperties securityProperties;

    @Override
    public ValidateCode generate(ServletWebRequest request) {
        String code = RandomStringUtils.randomNumeric(securityProperties.getCode().getSms().getLength());
        return new ValidateCode(code, securityProperties.getCode().getSms().getExpireIn());
    }

}
