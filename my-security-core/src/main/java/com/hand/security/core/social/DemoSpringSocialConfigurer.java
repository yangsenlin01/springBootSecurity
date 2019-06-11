package com.hand.security.core.social;

import org.springframework.social.security.SocialAuthenticationFilter;
import org.springframework.social.security.SpringSocialConfigurer;

/**
 * @author senlin.yang@hand-china.com
 * @version V1.0
 * @Date 2019-6-11
 * @description
 */
public class DemoSpringSocialConfigurer extends SpringSocialConfigurer {

    private String filterProcessesUrl;

    public DemoSpringSocialConfigurer(String filterProcessesUrl) {
        this.filterProcessesUrl = filterProcessesUrl;
    }

    @Override
    protected <T> T postProcess(T object) {
        SocialAuthenticationFilter filter = (SocialAuthenticationFilter) super.postProcess(object);
        filter.setFilterProcessesUrl(filterProcessesUrl);
        return (T)filter;
    }

}
