package com.hand.security.core.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.social.SocialProperties;

/**
 * @author senlin.yang@hand-china.com
 * @version V1.0
 * @Date 2019-6-11
 * @description
 */

@Getter
@Setter
public class QQProperties extends SocialProperties {

    /**
     * 第三方id，用来决定发起第三方登录的url，默认是 qq
     */
    private String providerId = "qq";

}
