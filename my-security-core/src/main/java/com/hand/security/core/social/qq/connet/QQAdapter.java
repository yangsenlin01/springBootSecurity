package com.hand.security.core.social.qq.connet;

import com.hand.security.core.social.qq.api.QQ;
import com.hand.security.core.social.qq.api.QQUserInfo;
import org.springframework.social.connect.ApiAdapter;
import org.springframework.social.connect.ConnectionValues;
import org.springframework.social.connect.UserProfile;

/**
 * @author senlin.yang@hand-china.com
 * @version V1.0
 * @Date 2019-6-11
 * @description 适配器，第三方服务商和自己应用之间用户信息格式存在差异，需要一个适配器
 */
public class QQAdapter implements ApiAdapter<QQ> {

    /**
     * 这个方法是用来测试的，我们直接返回true表示服务正常可用
     *
     * @param qq
     * @return
     */
    @Override
    public boolean test(QQ qq) {
        return true;
    }

    @Override
    public void setConnectionValues(QQ api, ConnectionValues connectionValues) {
        QQUserInfo userInfo = api.getUserInfo();
        // 昵称
        connectionValues.setDisplayName(userInfo.getNickname());
        // 头像
        connectionValues.setImageUrl(userInfo.getFigureurl_qq_1());
        // 个人主页url，QQ是没有的，直接给null
        connectionValues.setProfileUrl(null);
        // 服务商的用户ID，即QQ的openId
        connectionValues.setProviderUserId(userInfo.getOpenId());
    }

    @Override
    public UserProfile fetchUserProfile(QQ qq) {
        // todo 绑定解绑会用到
        return null;
    }

    @Override
    public void updateStatus(QQ qq, String s) {
        // QQ没有更新用户信息之类的提供
    }
}
