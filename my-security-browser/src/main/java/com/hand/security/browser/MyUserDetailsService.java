package com.hand.security.browser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * @author senlin.yang@hand-china.com
 * @version V1.0
 * @Date 2019-3-29
 * @description
 */

@Component
public class MyUserDetailsService implements UserDetailsService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 该方法接收用户的登录名称，根据username去数据库或者任何存储账号的地方，查找账号相关信息，
     * 放在一个user对象中，然后return这个对象。这个对象需要继承UserDetails，也就是该方法的返回类型
     * user对象可以自己写，也可以使用org.springframework.security.core.userdetails.User
     * 之后spring security会根据这个返回的user对象，从里面取出账号密码和登录时输入的账号密码相匹配。
     * user对象中(应该说是UserDetails)还包含了以下等信息：
     * 账号权限、账号是否没有被锁、是否没有过期、是否没有密码过期、是否激活等信息
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("登录用户名：" + username);
        // 假装我们从数据库取出的密码是加密的，注意，encode方法应该在用户注册时调用
        String password = passwordEncoder.encode("123456");
        logger.info("从数据库取出的密码是：" + password);
        return new User(username, password,
                true, true, true, true,
                AuthorityUtils.createAuthorityList("admin"));
    }
}
