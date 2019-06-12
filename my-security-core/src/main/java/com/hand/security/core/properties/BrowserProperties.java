package com.hand.security.core.properties;

import lombok.Getter;
import lombok.Setter;

/**
 * @author senlin.yang@hand-china.com
 * @version V1.0
 * @Date 2019-4-30
 * @description
 */

@Getter
@Setter
public class BrowserProperties {

    /**
     * 默认的注册页面
     */
    private String signUpUrl = "hand-signUp.html";

    /**
     * 'hand.security.browser.loginPage'的属性会被读取到这里
     * 程序运行时如果没有读取到相应的配置，就会使用"/hand-login.html"作为默认值
     */
    private String loginPage = SecurityConstants.DEFAULT_LOGIN_PAGE_URL;

    /**
     * 请求类型，如果是JSON就返回一段json数据；如果是重定向就使用security默认的处理器
     */
    private LoginType loginType = LoginType.JSON;

    /**
     * 记住我有效期，默认一周
     */
    private int rememberMeSeconds = 60 * 60 * 24 * 7;

}
