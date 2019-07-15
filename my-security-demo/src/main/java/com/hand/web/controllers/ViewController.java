package com.hand.web.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * @author senlin.yang@hand-china.com
 * @version V1.0
 * @Date 2019-7-12
 * @description
 */

@Controller
public class ViewController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @RequestMapping("/to-demo-signIn")
    public String toSignIn(HttpServletRequest request, @RequestParam("message") String message) throws UnsupportedEncodingException {

        message = URLDecoder.decode(message, "UTF-8");

        logger.info("paramMessage：" + message);

        request.setAttribute("message", message);
        return "demo-signIn";
    }

}
