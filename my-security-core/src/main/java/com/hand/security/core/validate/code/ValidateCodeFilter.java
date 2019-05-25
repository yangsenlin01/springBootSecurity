package com.hand.security.core.validate.code;

import org.apache.commons.lang.StringUtils;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author senlin.yang@hand-china.com
 * @version V1.0
 * @Date 2019-5-25
 * @description OncePerRequestFilter这个是security的基础过滤器，表示只会被调用一次，不重复
 */
public class ValidateCodeFilter extends OncePerRequestFilter {

    private AuthenticationFailureHandler authenticationFailureHandler;

    /**
     * ValidateCodeController中使用的就是这个将验证码对象放入session中的
     */
    private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 如果是登录请求并且是post方式
        if (StringUtils.equals("/authentication/form", request.getRequestURI())
                && StringUtils.equalsIgnoreCase(request.getMethod(), "post")) {

            try {
                validate(new ServletWebRequest(request));
            } catch (ValidateCodeException e) {
                // 验证失败调用失败的处理器
                authenticationFailureHandler.onAuthenticationFailure(request, response, e);
                // 失败直接返回，不调用后面的过滤器
                return;
            }
        }
        // 如果不是登录请求或者验证成功，执行后续filter
        filterChain.doFilter(request, response);

    }

    /**
     * 开始验证
     *
     * @param request
     */
    private void validate(ServletWebRequest request) throws ServletRequestBindingException {

        // 从session拿到之前存入的图片对象
        ImageCode codeSession = (ImageCode) sessionStrategy.getAttribute(request, ValidateCodeController.SESSION_KEY);

        // 拿到登录页面传来的验证码
        String codeInRequest = ServletRequestUtils.getStringParameter(request.getRequest(), "imageCode");

        if (StringUtils.isBlank(codeInRequest)) {
            throw new ValidateCodeException("验证码的值不能为空");
        }

        if (codeSession == null) {
            throw new ValidateCodeException("验证码不存在");
        }

        if (codeSession.isExpried()) {
            sessionStrategy.removeAttribute(request, ValidateCodeController.SESSION_KEY);
            throw new ValidateCodeException("验证码已过期");
        }

        if (!StringUtils.equals(codeSession.getCode(), codeInRequest)) {
            throw new ValidateCodeException("验证码不匹配");
        }

        sessionStrategy.removeAttribute(request, ValidateCodeController.SESSION_KEY);

    }

    public AuthenticationFailureHandler getAuthenticationFailureHandler() {
        return authenticationFailureHandler;
    }

    public void setAuthenticationFailureHandler(AuthenticationFailureHandler authenticationFailureHandler) {
        this.authenticationFailureHandler = authenticationFailureHandler;
    }
}
