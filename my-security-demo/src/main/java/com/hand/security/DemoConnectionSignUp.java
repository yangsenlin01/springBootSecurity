package com.hand.security;

import com.hand.web.entity.UserEntity;
import com.hand.web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.stereotype.Component;

/**
 * @author senlin.yang@hand-china.com
 * @version V1.0
 * @Date 2019-6-12
 * @description 第三方登录找不到用户时自动注册
 */
@Component
public class DemoConnectionSignUp implements ConnectionSignUp {

    @Autowired
    private UserService userService;

    @Override
    public String execute(Connection<?> connection) {
        // 根据社交用户信息创建用户并返回一个userId
        UserEntity userEntity = userService.registSocial(connection.getDisplayName());
        return userEntity.getUsername();
    }
}
