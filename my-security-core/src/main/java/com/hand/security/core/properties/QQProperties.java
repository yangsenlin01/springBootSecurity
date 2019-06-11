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

    private String providerId = "qq";

}
