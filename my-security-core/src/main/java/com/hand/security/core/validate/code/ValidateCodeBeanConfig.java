package com.hand.security.core.validate.code;

import com.hand.security.core.properties.SecurityProperties;
import com.hand.security.core.validate.code.image.ImageCodeGenerator;
import com.hand.security.core.validate.code.sms.DefaultSmsCodeSender;
import com.hand.security.core.validate.code.sms.SmsCodeSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author senlin.yang@hand-china.com
 * @version V1.0
 * @Date 2019-5-25
 * @description 配置类，用来获取验证码bean
 * @ConditionalOnMissingBean(name = "imageCodeGenerator")
 * 使用这个注解，如果服务启动时没有找到imageCodeGenerator，那么将使用本方法
 */

@Configuration
public class ValidateCodeBeanConfig {

    @Autowired
    private SecurityProperties securityProperties;

    @Bean
    @ConditionalOnMissingBean(name = "imageValidateCodeGenerator")
    public ValidateCodeGenerator imageValidateCodeGenerator() {
        ImageCodeGenerator codeGenerator = new ImageCodeGenerator();
        codeGenerator.setSecurityProperties(securityProperties);
        return codeGenerator;
    }

    @Bean
    @ConditionalOnMissingBean(SmsCodeSender.class)
    public SmsCodeSender smsCodeSender() {
        return new DefaultSmsCodeSender();
    }

}
