package com.hand.security.browser;

import com.hand.security.browser.session.HandExpiredSessionStrategy;
import com.hand.security.browser.session.HandInvalidSessionStrategy;
import com.hand.security.core.properties.SecurityProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.session.InvalidSessionStrategy;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;

/**
 * @author senlin.yang@hand-china.com
 * @version V1.0
 * @Date 2019-6-12
 * @description session失效/并发处理类的两个Bean
 */
@Configuration
public class BrowserSecurityBeanConfig {

    @Autowired
    private SecurityProperties securityProperties;

    @Bean
    @ConditionalOnMissingBean(InvalidSessionStrategy.class)
    public InvalidSessionStrategy invalidSessionStrategy() {
        return new HandInvalidSessionStrategy(securityProperties.getBrowser().getSession().getSessionInvalidUrl());
    }

    @Bean
    @ConditionalOnMissingBean(SessionInformationExpiredStrategy.class)
    public SessionInformationExpiredStrategy sessionInformationExpiredStrategy() {
        return new HandExpiredSessionStrategy(securityProperties.getBrowser().getSession().getSessionInvalidUrl());
    }

}
