package com.hand.security.core.social.qq.connet;

import com.hand.security.core.social.qq.api.QQ;
import com.hand.security.core.social.qq.api.QQImpl;
import org.springframework.social.oauth2.AbstractOAuth2ServiceProvider;
import org.springframework.social.oauth2.OAuth2Template;

/**
 * @author senlin.yang@hand-china.com
 * @version V1.0
 * @Date 2019-6-11
 * @description ServiceProvider，默认实现是AbstractOAuth2ServiceProvider
 * ServiceProvider需要OAuth2Operations和Api
 * OAuth2Operations默认实现是OAuth2Template
 * Api默认实现是AbstractOAuth2ApiBinding，我们这里用自己的实现QQImpl
 */
public class QQServiceProvider extends AbstractOAuth2ServiceProvider<QQ> {

    private String appId;

    /**
     * 获取授权码地址
     */
    private static final String URL_AUTHORIZE = "https://graph.qq.com/oauth2.0/authorize";

    /**
     * 根据授权码获取token地址
     */
    private static final String URL_ACCESS_TOKEN = "https://graph.qq.com/oauth2.0/token";

    public QQServiceProvider(String appId, String appSecret) {
        super(new QQOAuth2Template(appId, appSecret, URL_AUTHORIZE, URL_ACCESS_TOKEN));
        this.appId = appId;
    }

    @Override
    public QQ getApi(String accessToken) {
        return new QQImpl(accessToken, appId);
    }
}
