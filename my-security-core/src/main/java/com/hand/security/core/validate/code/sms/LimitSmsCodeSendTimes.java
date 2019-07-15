package com.hand.security.core.validate.code.sms;

import com.hand.security.core.utils.AddressUtils;
import com.hand.security.core.utils.IpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

/**
 * @author senlin.yang@hand-china.com
 * @version V1.0
 * @Date 2019-7-12
 * @description
 */
public class LimitSmsCodeSendTimes {

    private static final Logger logger = LoggerFactory.getLogger(LimitSmsCodeSendTimes.class);

    /**
     * 限制同一手机或者同一IP在指定时间内发能发送一次手机验证码
     *
     * @param request       用来获取ip
     * @param redisTemplate 是否发送的flag存在redis中
     * @param mobile        手机号
     * @param time          指定时间
     * @return
     */
    public static Boolean validateTimes(HttpServletRequest request, RedisTemplate redisTemplate, String mobile, Integer time) {

        // 同一手机
        Object mobileFlag = redisTemplate.opsForValue().get("springsecurity:regist:sms:smsCode_mobileFlag_" + mobile);
        if (mobileFlag != null) {
            logger.info("手机号" + mobile + "近期已发送过验证码");
            return Boolean.FALSE;
        } else {
            // 获取真实IP
            String ip = IpUtils.getIpAddr(request);
            // 根据ip获取真实地址
            String ipAddress = AddressUtils.getCityInfo(ip);
            Object ipFlag = redisTemplate.opsForValue().get("springsecurity:regist:sms:smsCode_IpFlag_" + ip);
            if (ipFlag != null) {
                logger.info(ip + "(" + ipAddress + ")" + "近期已发送过验证码");
                return Boolean.FALSE;
            } else {

                logger.info(ip + "(" + ipAddress + ")" + "请求向手机号" + mobile + "发送验证码");
                redisTemplate.opsForValue().set("springsecurity:regist:sms:smsCode_mobileFlag_" + mobile, 1, time, TimeUnit.SECONDS);
                redisTemplate.opsForValue().set("springsecurity:regist:sms:smsCode_IpFlag_" + ip, 1, time, TimeUnit.SECONDS);
            }
        }
        return Boolean.TRUE;
    }

    public static Boolean validateTimes(HttpServletRequest request, RedisTemplate redisTemplate, String mobile) {
        return LimitSmsCodeSendTimes.validateTimes(request, redisTemplate, mobile, 60);
    }

}
