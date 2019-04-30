package com.hand.web.config;

import com.hand.web.filter.TimeFilter;
import com.hand.web.interceptor.TimeInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author senlin.yang@com.hand-china.com
 * @version V1.0
 * @Date 2019-3-26
 * @description
 */

@Configuration
public class WebConfig extends WebMvcConfigurerAdapter{

    @Autowired
    private TimeInterceptor timeInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(timeInterceptor);
    }

    /**相当于在xml中配置了一个filter标签*/
    @Bean
    public FilterRegistrationBean timeFilter() {

        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();

        // 相当于filter标签下的属性值
        TimeFilter timeFilter = new TimeFilter();
        filterRegistrationBean.setFilter(timeFilter);

        // "/*"表示所有的请求都会被拦截
        List<String> urls = new ArrayList<>(1);
        urls.add("/*");
        filterRegistrationBean.setUrlPatterns(urls);

        return filterRegistrationBean;
    }

}
