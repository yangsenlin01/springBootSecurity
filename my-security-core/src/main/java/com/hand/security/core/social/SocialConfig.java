package com.hand.security.core.social;

import com.hand.security.core.properties.SecurityProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.social.config.annotation.EnableSocial;
import org.springframework.social.config.annotation.SocialConfigurerAdapter;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.jdbc.JdbcUsersConnectionRepository;
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

    @Override
    public UsersConnectionRepository getUsersConnectionRepository(ConnectionFactoryLocator connectionFactoryLocator) {
        // Encryptors可以对存入的信息作加密，noOpText()表示不加密
        JdbcUsersConnectionRepository repository = new JdbcUsersConnectionRepository(dataSource, connectionFactoryLocator, Encryptors.noOpText());
        // 表明必须是以UserConnection结尾，可以加前缀或不加
        repository.setTablePrefix("demo_");
        return repository;
    }

    /**
     * 需要在登录链中引入这个bean，开启social过滤连
     *
     * @return
     */
    @Bean
    public SpringSocialConfigurer demoSocialSecurityConfig() {
        String filterProcessesUrl = securityProperties.getSocial().getFilterProcessesUrl();
        return new DemoSpringSocialConfigurer(filterProcessesUrl);
    }
}
