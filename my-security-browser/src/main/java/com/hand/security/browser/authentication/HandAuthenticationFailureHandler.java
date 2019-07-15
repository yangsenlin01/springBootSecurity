package com.hand.security.browser.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hand.security.core.properties.LoginType;
import com.hand.security.core.properties.SecurityProperties;
import com.hand.security.core.support.SimpleResponse;
import com.hand.security.core.validate.code.ValidateCodeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;

/**
 * @author senlin.yang@hand-china.com
 * @version V1.0
 * @Date 2019-5-1
 * @description 重写security登录失败后执行的操作
 * AuthenticationException 因为登录失败，所以拿不到用户的认证信息，但是可以拿到认证失败的信息
 */

@Component("handAuthenticationFailureHandler")
public class HandAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SecurityProperties securityProperties;

    /**
     * 使用这个转发请求
     */
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        String message;
        if (exception instanceof BadCredentialsException || exception instanceof UsernameNotFoundException) {
            message = "用户名或密码错误";
        } else if (exception instanceof LockedException) {
            message = "用户已被锁定";
        } else if (exception instanceof DisabledException) {
            message = "用户不可用";
        } else if (exception instanceof AccountExpiredException) {
            message = "账户已过期";
        } else if (exception instanceof CredentialsExpiredException) {
            message = "用户密码已过期";
        } else if (exception instanceof ValidateCodeException) {
            message = exception.getMessage();
        } else {
            message = exception.getMessage();
        }

        logger.info("登录失败");
        logger.info(new SimpleResponse(message).toString());
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");

        if (LoginType.JSON.equals(securityProperties.getBrowser().getLoginType())) {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.getWriter().write(objectMapper.writeValueAsString(new SimpleResponse(message)));
        } else {
            // super.onAuthenticationFailure(request, response, exception);

            redirectStrategy.sendRedirect(request, response, "/to-demo-signIn?message=" + URLEncoder.encode(message, "UTF-8"));
        }

    }
}
