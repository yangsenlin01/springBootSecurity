package com.hand.security.core.validate.code.sms;

/**
 * @author senlin.yang@hand-china.com
 * @version V1.0
 * @Date 2019-6-5
 */
public interface SmsCodeSender {

    void send(String mobile, String code);

}
