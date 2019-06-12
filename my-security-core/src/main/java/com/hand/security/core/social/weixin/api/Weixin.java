package com.hand.security.core.social.weixin.api;

/**
 * @author senlin.yang@hand-china.com
 * @version V1.0
 * @Date 2019-5-25
 * @description 微信API调用接口
 */
public interface Weixin {

	/**
	 * api方法，获取用户信息
	 *
	 * @param openId
	 * @return
	 */
	WeixinUserInfo getUserInfo(String openId);
	
}
