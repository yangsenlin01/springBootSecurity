package com.hand.security.app;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;

/**
 * @author senlin.yang@hand-china.com
 * @version V1.0
 * @Date 2019-6-13
 * @description
 * @EnableAuthorizationServer 表示开启认证服务器
 */

@Configuration
@EnableAuthorizationServer
public class HandAuthorizationServerConfig {
}
