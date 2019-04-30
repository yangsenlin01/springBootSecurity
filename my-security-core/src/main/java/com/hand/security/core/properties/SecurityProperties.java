package com.hand.security.core.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author senlin.yang@hand-china.com
 * @version V1.0
 * @Date 2019-4-30
 * @description @ConfigurationProperties注解会读取所有配置文件以'hand.security.xxx'开头的属性值
 */

@ConfigurationProperties(prefix = "hand.security")
public class SecurityProperties {

    /**
     * 所有'hand.security.browser.xxx'的属性都会被读取到这个类里面
     */
    private BrowserProperties browser = new BrowserProperties();

    public BrowserProperties getBrowser() {
        return browser;
    }

    public void setBrowser(BrowserProperties browser) {
        this.browser = browser;
    }
}
