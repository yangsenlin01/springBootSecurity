package com.hand.security.core.social.qq.connet;

import com.hand.security.core.social.qq.api.QQ;
import org.springframework.social.connect.support.OAuth2ConnectionFactory;

/**
 * @author senlin.yang@hand-china.com
 * @version V1.0
 * @Date 2019-6-11
 * @description 连接工厂，需要ServiceProvider和ApiAdapter
 * ServiceProvider使用我们的QQServiceProvider
 * ApiAdapter使用我们的QQAdapter
 */
public class QQConnectionFactory extends OAuth2ConnectionFactory<QQ> {

    public QQConnectionFactory(String openId, String appId, String appSecret) {
        super(openId, new QQServiceProvider(appId, appSecret), new QQAdapter());
    }
}
