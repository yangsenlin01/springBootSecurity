package com.hand.security;

import com.hand.exception.UserNotExistException;
import com.hand.web.entity.UserEntity;
import com.hand.web.mapper.UserMapper;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.social.security.SocialUser;
import org.springframework.social.security.SocialUserDetails;
import org.springframework.social.security.SocialUserDetailsService;
import org.springframework.stereotype.Component;

/**
 * @author senlin.yang@hand-china.com
 * @version V1.0
 * @Date 2019-3-29
 * @description
 */

@Component
public class MyUserDetailsService implements UserDetailsService, SocialUserDetailsService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserMapper userMapper;

    /**
     * 该方法接收用户的登录名称，根据username去数据库或者任何存储账号的地方，查找账号相关信息，
     * 放在一个user对象中，然后return这个对象。这个对象需要继承UserDetails，也就是该方法的返回类型
     * user对象可以自己写，也可以使用org.springframework.security.core.userdetails.User
     * 之后spring security会根据这个返回的user对象，从里面取出账号密码和登录时输入的账号密码相匹配。
     * user对象中(应该说是UserDetails)还包含了以下等信息：
     * 账号权限、账号是否没有被锁、是否没有过期、是否没有密码过期、是否激活等信息
     *
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("表单登录用户名:" + username);
        return buildUser(username, "1");
    }

    /**
     * 第三方登录
     *
     * @param userId
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public SocialUserDetails loadUserByUserId(String userId) throws UsernameNotFoundException {
        logger.info("社交登录用户Id:" + userId);
        return buildUser(userId, "2");
    }

    /**
     * @param userId
     * @param type   1->表单登录，2->第三方登录
     * @return
     */
    private SocialUserDetails buildUser(String userId, String type) {
        // 查找用户信息，查找到的用户信息判断用户是否被冻结
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(userId);
        userEntity = userMapper.selectOne(userEntity);
        if (userEntity == null) {
            logger.info("未查询到用户信息:" + userId);
            throw new UserNotExistException(userId, userId + "用户不存在");
        }

        String password = userEntity.getPassword();
        logger.info("成功获取用户信息:" + userId);
        return new SocialUser(userId, password,
                true, true, true, true,
                AuthorityUtils.commaSeparatedStringToAuthorityList("admin, ROLE_USER"));
    }
}
