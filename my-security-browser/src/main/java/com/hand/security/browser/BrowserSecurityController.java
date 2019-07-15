package com.hand.security.browser;

import com.hand.security.core.support.SimpleResponse;
import com.hand.security.browser.support.SocialUserInfo;
import com.hand.security.core.properties.SecurityConstants;
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
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;

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

    @Autowired
    private ProviderSignInUtils providerSignInUtils;

    /**
     * 需要身份认证时，跳转到这里
     *
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    @RequestMapping(SecurityConstants.DEFAULT_UNAUTHENTICATION_URL)
    @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
    public SimpleResponse requireAuthentication(HttpServletRequest request, HttpServletResponse response) throws IOException {

        // 从缓存里拿到http信息
        SavedRequest savedRequest = requestCache.getRequest(request, response);
        if (savedRequest != null) {
            {
                // 拿到引发跳转的请求
                String targetUrl = savedRequest.getRedirectUrl();
                logger.info("引发跳转的请求是：" + targetUrl);
                Integer port = request.getServerPort();
                logger.info("请求端口号为：" + port);
                // 如果是html请求，跳转到登录页，否者返回一段json字符串
                if (StringUtils.endsWith(targetUrl, ".html")
                        || StringUtils.equals(targetUrl, "http://security.theboyaply.cn/")
                        || StringUtils.equals(targetUrl, "http://localhost/")) {
                    redirectStrategy.sendRedirect(request, response, securityProperties.getBrowser().getLoginPage());
                }
            }
        }
        return new SimpleResponse("访问的服务需要身份认证");
    }

    /**
     * 提供的一个获取第三方登录用户信息的api，可在注册时展示给前端
     *
     * @param request
     * @return
     */
    @GetMapping("/social/user")
    public SocialUserInfo getSocialUserInfo(HttpServletRequest request) {
        SocialUserInfo socialUserInfo = new SocialUserInfo();
        Connection<?> connection = providerSignInUtils.getConnectionFromSession(new ServletWebRequest(request));
        socialUserInfo.setProviderId(connection.getKey().getProviderId());
        socialUserInfo.setProviderUserId(connection.getKey().getProviderUserId());
        socialUserInfo.setNickname(connection.getDisplayName());
        socialUserInfo.setHeadimg(connection.getImageUrl());
        return socialUserInfo;
    }

}
