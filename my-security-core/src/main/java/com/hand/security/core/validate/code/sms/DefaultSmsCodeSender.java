package com.hand.security.core.validate.code.sms;

import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.hand.security.core.validate.code.sms.SmsCodeSender;
import com.hand.security.core.validate.code.sms.SmsUtil;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

/**
 * 默认的短信验证码发送器
 *
 * @author zhailiang
 */
public class DefaultSmsCodeSender implements SmsCodeSender {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${hand.security.code.sms.accessKeyId}")
    private String accessKeyId;

    @Value("${hand.security.code.sms.accessKeySecret}")
    private String accessKeySecret;

    @Override
    public void send(String mobile, String code) {
        SendSmsResponse sendSmsResponse = new SendSmsResponse();
        try {
            String templateCode = "SMS_168825302";
            String signName = "007安全框架";
            String param = "{\"smsCode\":\"" + code + "\"}";
            sendSmsResponse = SmsUtil.sendSms(mobile, templateCode, signName, param, accessKeyId, accessKeySecret);
        } catch (ClientException e) {
            logger.info(e.getMessage());
        }
        logger.info(ReflectionToStringBuilder.toString(sendSmsResponse, ToStringStyle.MULTI_LINE_STYLE));
        logger.info("向手机" + mobile + "发送短信验证码" + code);
    }

}
