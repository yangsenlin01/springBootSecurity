package com.hand.security.browser;

import com.hand.security.browser.session.HandExpiredSessionStrategy;
import com.hand.security.core.authentication.mobile.SmsCodeAuthenticationSecurityConfig;
import com.hand.security.core.properties.SecurityConstants;
import com.hand.security.core.properties.SecurityProperties;
import com.hand.security.core.validate.code.ValidateCodeFilter;
import com.hand.security.core.validate.code.ValidateCodeProcessorHolder;
import com.hand.security.core.authentication.AbstractChannelSecurityConfig;
import com.hand.security.core.validate.code.ValidateCodeSecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.session.InvalidSessionStrategy;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;
import org.springframework.social.security.SpringSocialConfigurer;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

/**
 * @author senlin.yang@hand-china.com
 * @version V1.0
 * @Date 2019-3-29
 * @description
 */

@Component
public class BrowserSecurityConfig extends AbstractChannelSecurityConfig {

    @Autowired
    private SecurityProperties securityProperties;

    /**
     * 数据源，在demo配置文件中配置
     */
    @Autowired
    private DataSource dataSource;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private ValidateCodeSecurityConfig validateCodeSecurityConfig;

    @Autowired
    private SmsCodeAuthenticationSecurityConfig smsCodeAuthenticationSecurityConfig;

    @Autowired
    private SpringSocialConfigurer demoSocialSecurityConfig;

    @Autowired
    private SessionInformationExpiredStrategy sessionInformationExpiredStrategy;

    @Autowired
    private InvalidSessionStrategy invalidSessionStrategy;

    @Autowired
    private LogoutSuccessHandler logoutSuccessHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // 表单登录
        applyPasswordAuthenticationConfig(http);

        http.apply(smsCodeAuthenticationSecurityConfig)
                .and()
            .apply(validateCodeSecurityConfig)
                .and()
            .apply(demoSocialSecurityConfig)
                .and()
             // 记住我
            .rememberMe()
                // 获取JdbcTokenRepositoryImpl的实现
                .tokenRepository(persistentTokenRepository())
                // 默认有效期
                .tokenValiditySeconds(securityProperties.getBrowser().getRememberMeSeconds())
                // 添加认证信息
                .userDetailsService(userDetailsService)
                .and()
            .sessionManagement()
                // session失效时，访问服务跳转的地址
                //.invalidSessionUrl("/session/invalid")
                // session失效时，进行的处理。与invalidSessionUrl不能同时存在
                .invalidSessionStrategy(invalidSessionStrategy)
                // 同一个session同时登录最大数量
                .maximumSessions(securityProperties.getBrowser().getSession().getMaximumSessions())
                // 达到最大session时是否阻止新的登录请求，默认为false，不阻止，新的登录会将老的登录失效掉
                // 阻止的话新的session登录默认会返回一段提示content	"Maximum sessions of 1 for this principal exceeded"
                .maxSessionsPreventsLogin(securityProperties.getBrowser().getSession().isMaxSessionsPreventsLogin())
                // session并发时进行的处理，默认处理是挤掉之前的session
                .expiredSessionStrategy(sessionInformationExpiredStrategy)
                .and()
                .and()
            .logout()
                // 退出的url，默认是/logout，退出之后默认跳转到登录页面
                .logoutUrl("/signOut")
                // 退出之后跳转的url
                //.logoutSuccessUrl("")
                // 退出之后的处理，不能与logoutSuccessUrl同时存在
                .logoutSuccessHandler(logoutSuccessHandler)
                // 退出之后将浏览器中这个属性的值删除，即删除浏览器的cookies
                .deleteCookies("JSESSIONID")
                .and()
            // 请求授权
            .authorizeRequests()
                // 添加一个匹配器，匹配/demo-login.html这个url，指定不需要认证，注意这个需要放在anyRequest()之前
                // 如果不加这个，用户没登录就会跳转到/demo-login.html页面进行登陆，但是/demo-login.html也需要认证，又会跳转到/demo-login.html，这样会死循环下去
                //.antMatchers("/demo-login.html").permitAll()
                .antMatchers(SecurityConstants.DEFAULT_UNAUTHENTICATION_URL,
                        SecurityConstants.DEFAULT_LOGIN_PROCESSING_URL_MOBILE,
                        securityProperties.getBrowser().getLoginPage(),
                        SecurityConstants.DEFAULT_VALIDATE_CODE_URL_PREFIX + "/*",
                        securityProperties.getBrowser().getSignUpUrl(),
                        securityProperties.getBrowser().getSignOutUrl(),
                        "/user/regist")
                        .permitAll()
                // 所有的请求都需要,都需要身份认证
                .anyRequest()
                .authenticated()
                .and()
            // csrf是spring security默认的跨站请求伪造防护，我们现在关闭它，否则登录之后会报错
            .csrf().disable();
    }

    /**
     * 记住我
     *
     * @return
     */
    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource);
        // 下面这个语句是启动时自动执行这个SQL(也可以自己手动去数据库执行)：JdbcTokenRepositoryImpl.CREATE_TABLE_SQL，当然如果数据库已经存在这个表，启动项目会报错
        //tokenRepository.setCreateTableOnStartup(true);

        return tokenRepository;
    }

    /**激活密码加密验证*/
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
