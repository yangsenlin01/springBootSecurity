package com.hand.security.core.social;

import org.springframework.web.servlet.view.AbstractView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author senlin.yang@hand-china.com
 * @version V1.0
 * @Date 2019-6-12
 * @description 绑定发起一个/connect/qq的post请求即可，解绑发起一个/connect/qq的delete请求即可
 */
public class DemoConnectView extends AbstractView {

    @Override
    protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request,
                                           HttpServletResponse response) throws Exception {

        response.setContentType("text/html;charset=UTF-8");
        if (model.get("connection") == null) {
            response.getWriter().write("<h3>绑定成功</h3>");
        } else {
            response.getWriter().write("<h3>解绑成功</h3>");
        }

    }

}