package com.hand.security.browser.session;

import org.springframework.security.web.session.SessionInformationExpiredEvent;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;

import javax.servlet.ServletException;
import java.io.IOException;

/**
 * @author senlin.yang@hand-china.com
 * @version V1.0
 * @Date 2019-6-12
 * @description session并发时进行的处理
 */
public class HandExpiredSessionStrategy extends AbstractSessionStrategy implements SessionInformationExpiredStrategy {

    public HandExpiredSessionStrategy(String invalidSessionUrl) {
        super(invalidSessionUrl);
    }

    @Override
    public void onExpiredSessionDetected(SessionInformationExpiredEvent event) throws IOException, ServletException {
        onSessionInvalid(event.getRequest(), event.getResponse());
    }

    @Override
    protected boolean isConcurrency() {
        return true;
    }

}
