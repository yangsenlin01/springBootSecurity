package com.hand.service.impl;

import com.hand.service.HelloService;
import org.springframework.stereotype.Service;

/**
 * @author senlin.yang@com.hand-china.com
 * @version V1.0
 * @Date 2019-1-29
 * @description
 */

@Service
public class HelloServiceImpl implements HelloService {
    @Override
    public String greeting(String name) {
        System.out.println("helloService : " + name);
        return "hello " + name;
    }
}
