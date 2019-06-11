package com.hand.security.core.social.qq.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.social.oauth2.AbstractOAuth2ApiBinding;
import org.springframework.social.oauth2.TokenStrategy;

/**
 * @author senlin.yang@hand-china.com
 * @version V1.0
 * @Date 2019-6-11
 * @description
 */
public class QQImpl extends AbstractOAuth2ApiBinding implements QQ {

    private static final Logger logger = LoggerFactory.getLogger(QQImpl.class);

    /**
     * 获取用户的openId
     */
    private static final String URL_GET_OPENID = "https://graph.qq.com/oauth2.0/me?access_token=%s";

    /**
     * 获取用户信息，其中access_token由父类AbstractOAuth2ApiBinding来传。
     */
    private static final String URL_GET_USERINFO = "https://graph.qq.com/user/get_user_info?oauth_consumer_key=%s&openid=%s";

    private String appId;

    private String openId;

    /**
     * 可以将字符串转换为对象com.fasterxml.jackson.databind.ObjectMapper
     */
    private ObjectMapper objectMapper = new ObjectMapper();

    public QQImpl(String accessToken, String appId) {
        // 父类AbstractOAuth2ApiBinding会帮我们自动拼接在url后面
        super(accessToken, TokenStrategy.ACCESS_TOKEN_PARAMETER);
        this.appId = appId;

        String url = String.format(URL_GET_OPENID, accessToken);
        String result = getRestTemplate().getForObject(url, String.class);
        logger.info("拿到的openId为：" + result);
        this.openId = StringUtils.substringBetween(result, "\"openid\":\"", "\"");
    }

    @Override
    public QQUserInfo getUserInfo() {
        String url = String.format(URL_GET_USERINFO, appId, openId);
        String result = getRestTemplate().getForObject(url, String.class);
        logger.info("拿到的用户信息为：" + result);
        try {
            return objectMapper.readValue(result, QQUserInfo.class);
        } catch (Exception e) {
            logger.info(e.getMessage());
            throw new RuntimeException("获取用户信息失败", e);
        }
    }
}
