package com.hand;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author senlin.yang@com.hand-china.com
 * @version V1.0
 * @Date 2019-1-16
 * @description
 * @SpringBootApplication:声明这是一个springBoot的启动类
 * @RestController:将类该声明为一个comtroller
 */

@SpringBootApplication
@RestController
@MapperScan("com.hand.web.mapper")
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @GetMapping(value = "/hello")
    public String sayHello() {
        return "hello spring security";
    }
}
