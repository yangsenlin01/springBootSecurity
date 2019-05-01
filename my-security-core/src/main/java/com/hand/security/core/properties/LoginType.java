package com.hand.security.core.properties;

/**
 * @author senlin.yang@hand-china.com
 * @version V1.0
 * @Date 2019-5-1
 * @description 请求类型
 * 如果是JSON就返回一段json数据
 * 如果是REDIRECT重定向就使用security默认的处理器
 */
public enum LoginType {

    REDIRECT,

    JSON

}
