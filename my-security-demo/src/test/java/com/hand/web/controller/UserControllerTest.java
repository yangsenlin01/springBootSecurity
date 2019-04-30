package com.hand.web.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author senlin.yang@com.hand-china.com
 * @version V1.0
 * @Date 2019-1-17
 * @description
 *  @RunWith(SpringRunner.class):执行测试用例的声明
 *  @SpringBootTest:声明该类为测试类
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserControllerTest {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    /**
     * 每次执行前都会调用这个方法
     */
    @Before
    public void setUp() {
        // 初始化上下文
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void whenQuerySuccess() throws Exception{
       String result = mockMvc.perform(get("/user")
                .param("username", "tom").param("age", "18").param("ageTo", "20").param("other", "xxx")
                // 下面这三个是springMvc自带的分页参数，后台使用Pageable接收
//                .param("page", "2")
//                .param("size", "3")
//                .param("sort", "age")
                // 设置请求格式
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                // 期望返回状态码是200
                .andExpect(status().isOk())
                // 期望返回结果的长度是3
                .andExpect(jsonPath("$.length()").value(3))
                // 将返回结果转成String
                .andReturn().getResponse().getContentAsString();

        System.out.println(result);
    }

    @Test
    public void whenGetInfoSuccess() throws Exception{
        String result = mockMvc.perform(get("/user/1")
                // 设置请求格式
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                // 期望返回状态码是200
                .andExpect(status().isOk())
                // 期望返回结果中的usernmae是tom
                .andExpect(jsonPath("$.username").value("tom"))
                // 将返回结果转成String
                .andReturn().getResponse().getContentAsString();
        System.out.println(result);
    }

    @Test
    public void whenGetInfoFail() throws Exception{
        mockMvc.perform(get("/user/a")
                // 设置请求格式
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                // 期望返回状态码是200
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void whenCreateSuccess() throws Exception{
        Date date = new Date();
        System.out.println(date.getTime());
        String content = "{\"username\":\"tom\", \"password\":null, \"birthday\":" + date.getTime() + "}";
        String result = mockMvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(content))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andReturn().getResponse().getContentAsString();

        System.out.println(result);
    }

    @Test
    public void whenUpdateSuccess() throws Exception{
        // 给当前时间加上一年的时间，精确到毫秒
        Date date = new Date(LocalDateTime.now().plusYears(1).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        System.out.println(date.getTime());
        String content = "{\"id\":\"1\", \"username\":\"tom\", \"password\":null, \"birthday\":" + date.getTime() + "}";
        String result = mockMvc.perform(put("/user/1")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(content))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andReturn().getResponse().getContentAsString();

        System.out.println(result);
    }

    @Test
    public void deleteSuccess() throws Exception{
        mockMvc.perform(delete("/user/1").
                contentType(MediaType.APPLICATION_JSON_UTF8)).
                andExpect(status().isOk());
    }

    @Test
    public void whenUploadSuccess() throws Exception {
        String result = mockMvc.perform(fileUpload("/file")
                .file(new MockMultipartFile("file",
                        "test.txt",
                        "multipart/form-data",
                        "hello world".getBytes("UTF-8"))))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        System.out.println(result);
    }

}
