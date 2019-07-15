package com.hand.security.core.validate.code;

import com.hand.security.core.properties.SecurityConstants;
import com.hand.security.core.support.ResponseData;
import com.hand.security.core.validate.code.image.ImageCode;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @author senlin.yang@hand-china.com
 * @version V1.0
 * @Date 2019-5-24
 * @description 验证码
 */

@RestController
public class ValidateCodeController {

    @Autowired
    private ValidateCodeProcessorHolder validateCodeProcessorHolder;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 创建验证码，根据验证码类型不同，调用不同的 {@link ValidateCodeProcessor}接口实现
     *
     * @param request
     * @param response
     * @param type
     * @throws Exception
     */
    @GetMapping(SecurityConstants.DEFAULT_VALIDATE_CODE_URL_PREFIX + "/{type}")
    public ResponseData createCode(HttpServletRequest request, HttpServletResponse response, @PathVariable String type)
            throws Exception {

        ServletWebRequest servletWebRequest = new ServletWebRequest(request, response);

        // 如果是手机验证码，一分钟内只能发送一次
        if (StringUtils.equals("sms", type)) {
            String mobile = ServletRequestUtils.getRequiredStringParameter(servletWebRequest.getRequest(), SecurityConstants.DEFAULT_PARAMETER_NAME_MOBILE);
            Object o = redisTemplate.opsForValue().get("springsecurity:login:sms:smsCode_" + mobile);
            if (o != null) {
                ResponseData responseData = new ResponseData();
                responseData.setSuccess(Boolean.FALSE);
                responseData.setMessage("同一个手机一分钟只能发送一次");
                return responseData;
            } else {
                redisTemplate.opsForValue().set("springsecurity:login:sms:smsCode_" + mobile, 1, 60, TimeUnit.SECONDS);
            }
        }
        validateCodeProcessorHolder.findValidateCodeProcessor(type).create(servletWebRequest);
        return new ResponseData();
    }

}
