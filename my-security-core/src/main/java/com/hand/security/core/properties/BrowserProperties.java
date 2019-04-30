package com.hand.security.core.properties;

/**
 * @author senlin.yang@hand-china.com
 * @version V1.0
 * @Date 2019-4-30
 * @description
 */
public class BrowserProperties {

    /**
     * 'hand.security.browser.loginPage'的属性会被读取到这里
     * 程序运行时如果没有读取到相应的配置，就会使用"/hand-login.html"作为默认值
     */
    private String loginPage = "/hand-login.html";

    public String getLoginPage() {
        return loginPage;
    }

    public void setLoginPage(String loginPage) {
        this.loginPage = loginPage;
    }
}
