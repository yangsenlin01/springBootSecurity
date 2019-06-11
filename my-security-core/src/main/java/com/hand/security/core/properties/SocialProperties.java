package com.hand.security.core.properties;

import lombok.Getter;
import lombok.Setter;

/**
 * @author senlin.yang@hand-china.com
 * @version V1.0
 * @Date 2019-6-11
 * @description
 */

@Getter
@Setter
public class SocialProperties {

    private String filterProcessesUrl = "/auth";

    private QQProperties qq = new QQProperties();

}
