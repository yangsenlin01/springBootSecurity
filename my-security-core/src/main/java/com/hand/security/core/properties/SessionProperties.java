package com.hand.security.core.properties;

import lombok.Getter;
import lombok.Setter;

/**
 * @author senlin.yang@hand-china.com
 * @version V1.0
 * @Date 2019-6-12
 * @description
 */
@Getter
@Setter
public class SessionProperties {

    /**
     * 同一个用户在系统中的最大session数，默认1
     */
    private int maximumSessions = 1;
    /**
     * 达到最大session时是否阻止新的登录请求，默认为false，不阻止，新的登录会将老的登录失效掉
     * 阻止的话新的session登录默认会返回一段提示content	"Maximum sessions of 1 for this principal exceeded"
     */
    private boolean maxSessionsPreventsLogin = false;
    /**
     * session失效时跳转的地址
     */
    private String sessionInvalidUrl = SecurityConstants.DEFAULT_SESSION_INVALID_URL;

}
