package com.hand.security.browser;

import com.hand.security.browser.support.SimpleResponse;
import com.hand.security.core.properties.SecurityProperties;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author senlin.yang@hand-china.com
 * @version V1.0
 * @Date 2019-4-30
 * @description 需要身份认证就会跳转到这个控制器
 * 在这里判断当前请求是html请求还是客户端请求：
 * 如果是html请求就将请求转发到登陆页面，否者返回一段401状态码的json字符串
 */

@RestController
public class BrowserSecurityController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 可以使用RequestCache拿到当前的请求，即触发身份认证的这个请求
     */
    private RequestCache requestCache = new HttpSessionRequestCache();

    /**
     * 使用这个转发请求
     */
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Autowired
    private SecurityProperties securityProperties;

    /**
     * 需要身份认证时，跳转到这里
     *
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    @RequestMapping("/authentication/require")
    @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
    public SimpleResponse requireAuthentication(HttpServletRequest request, HttpServletResponse response) throws IOException {

        // 从缓存里拿到http信息
        SavedRequest savedRequest = requestCache.getRequest(request, response);
        if (savedRequest != null) {
            {
                // 拿到引发跳转的请求
                String targetUrl = savedRequest.getRedirectUrl();
                logger.info("引发跳转的请求是：" + targetUrl);
                // 如果是html请求，跳转到登录页，否者返回一段json字符串
                if (StringUtils.endsWith(targetUrl, ".html")) {
                    redirectStrategy.sendRedirect(request, response, securityProperties.getBrowser().getLoginPage());
                }
            }
        }
        return new SimpleResponse("访问的服务需要身份认证");
    }

}
