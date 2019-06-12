package com.hand.security.core.social;

import com.hand.security.core.properties.SecurityProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.social.config.annotation.EnableSocial;
import org.springframework.social.config.annotation.SocialConfigurerAdapter;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.jdbc.JdbcUsersConnectionRepository;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.social.security.SpringSocialConfigurer;

import javax.sql.DataSource;

/**
 * @author senlin.yang@hand-china.com
 * @version V1.0
 * @Date 2019-6-11
 * @description 操作数据库的UserConnection表
 * 建表sql在org.springframework.social.connect.jdbc.JdbcUsersConnectionRepository.class文件下面的sql文件中
 */

@Configuration
@EnableSocial
public class SocialConfig extends SocialConfigurerAdapter {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private SecurityProperties securityProperties;

    /**
     * 自动注册，应用可以配置自动注册或者不配置，所以这个接口是不必须的
     */
    @Autowired(required = false)
    private ConnectionSignUp connectionSignUp;

    /**
     * 断点查看SocialAuthenticationProvider实例使用到的UsersConnectionRepository是InMemoryUsersConnectionRepository的实现
     * 所以这个方法返回的JdbcUsersConnectionRepository不起作用
     * 于是我在下边重新注册了一个UsersConnectionRepository的Bean,@Bean(name = "usersConnectionRepository")
     *
     * @param connectionFactoryLocator
     * @return
     */
    @Override
    public UsersConnectionRepository getUsersConnectionRepository(ConnectionFactoryLocator connectionFactoryLocator) {
        // Encryptors可以对存入的信息作加密，noOpText()表示不加密
        JdbcUsersConnectionRepository repository = new JdbcUsersConnectionRepository(dataSource, connectionFactoryLocator, Encryptors.noOpText());
        // 表明必须是以UserConnection结尾，可以加前缀或不加
        repository.setTablePrefix("demo_");
        if (connectionSignUp != null) {
            repository.setConnectionSignUp(connectionSignUp);
        }
        return repository;
    }

    /**
     * 别人用上面那个@Override的getUsersConnectionRepository方法返回一个JdbcUsersConnectionRepository实例就可以了
     * 不过在我本地跑的时候获取不到JdbcUsersConnectionRepository实例，使用的是默认的InMemoryUsersConnectionRepository
     * 所以在这里注入一个UsersConnectionRepository的Bean
     *
     * @param connectionFactoryLocator
     * @return
     */
    @Bean(name = "usersConnectionRepository")
    public UsersConnectionRepository usersConnectionRepository(ConnectionFactoryLocator connectionFactoryLocator) {
        // Encryptors可以对存入的信息作加密，noOpText()表示不加密
        JdbcUsersConnectionRepository repository = new JdbcUsersConnectionRepository(dataSource, connectionFactoryLocator, Encryptors.noOpText());
        // 表明必须是以UserConnection结尾，可以加前缀或不加
        repository.setTablePrefix("demo_");
        if (connectionSignUp != null) {
            repository.setConnectionSignUp(connectionSignUp);
        }
        return repository;
    }

    /**
     * 需要在登录链中引入这个bean，开启social过滤连
     *
     * @return
     */
    @Bean(name = "demoSocialSecurityConfig")
    public SpringSocialConfigurer demoSocialSecurityConfig() {
        String filterProcessesUrl = securityProperties.getSocial().getFilterProcessesUrl();
        DemoSpringSocialConfigurer configurer = new DemoSpringSocialConfigurer(filterProcessesUrl);
        configurer.signupUrl(securityProperties.getBrowser().getSignUpUrl());
        return configurer;
    }

    /**
     * 可以从这个bean里面拿到在第三方登录过后的用户信息
     *
     * @param connectionFactoryLocator
     * @return
     */
    @Bean
    public ProviderSignInUtils providerSignInUtils(ConnectionFactoryLocator connectionFactoryLocator) {
        return new ProviderSignInUtils(connectionFactoryLocator, getUsersConnectionRepository(connectionFactoryLocator)){};
    }
}
