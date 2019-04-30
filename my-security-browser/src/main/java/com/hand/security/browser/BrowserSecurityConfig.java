package com.hand.security.browser;

import com.hand.security.core.properties.SecurityProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * @author senlin.yang@hand-china.com
 * @version V1.0
 * @Date 2019-3-29
 * @description
 */

@Component
public class BrowserSecurityConfig extends WebSecurityConfigurerAdapter{

    @Autowired
    private SecurityProperties securityProperties;

    /**激活密码加密验证*/
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 默认的登录方式，也就是会出现一个弹出框，让我们输入账号密码
        // http.httpBasic()
        // 表单登陆方式
        http.formLogin()
                // 指定登陆页面，不指定的话会跳转到默认的spring boot登陆页面，/demo-login.html页面放在resources/resource/下
                //.loginPage("/demo-login.html")
                .loginPage("/authentication/require")
                // 指定登录的url，也就是form表单里面的那个action。
                // 默认的是/login，默认的可以在UsernamePasswordAuthenticationFilter.java中查看
                .loginProcessingUrl("/authentication/form")
                // 添加操作
                .and()
                // 表示下面的都是请求授权操作
                .authorizeRequests()
                // 添加一个匹配器，匹配/demo-login.html这个url，指定不需要认证，注意这个需要放在anyRequest()之前
                // 如果不加这个，用户没登录就会跳转到/demo-login.html页面进行登陆，但是/demo-login.html也需要认证，又会跳转到/demo-login.html，这样会死循环下去
                //.antMatchers("/demo-login.html").permitAll()
                .antMatchers("/authentication/require",
                        securityProperties.getBrowser().getLoginPage()).permitAll()
                // 所有的请求都需要,都需要身份认证
                .anyRequest().authenticated()
                // 添加一个操作
                .and()
                // csrf是spring security默认的跨站请求伪造防护，我们现在关闭它，否则登录之后会报错
                .csrf().disable();
    }
}
