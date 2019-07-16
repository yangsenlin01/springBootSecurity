package com.hand.web.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import com.hand.dto.User;
import com.hand.dto.UserQueryCondition;
import com.hand.security.core.support.ResponseData;
import com.hand.security.core.validate.code.sms.DefaultSmsCodeSender;
import com.hand.security.core.validate.code.sms.LimitSmsCodeSendTimes;
import com.hand.web.entity.UserEntity;
import com.hand.web.service.UserService;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author senlin.yang@com.hand-china.com
 * @version V1.0
 * @Date 2019-1-17
 * @description
 */

@RestController
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    private ProviderSignInUtils providerSignInUtils;

    /**
     * 使用这个转发请求
     */
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Autowired
    private UserService userService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private DefaultSmsCodeSender defaultSmsCodeSender;

    /**
     * 注册/绑定用户
     *
     * @param user
     * @return
     */
    @PostMapping("/regist")
    public void regist(User user, HttpServletRequest request) {
        String userId = user.getUsername();
        // 将注册之后获取到的userId返回给social，与第三方的信息一并存入dome_connectionuser表中
        providerSignInUtils.doPostSignUp(userId, new ServletWebRequest(request));
    }

    @PostMapping("/regist/form")
    public void formRegist(User user, HttpServletRequest request, HttpServletResponse response) throws IOException {
        ResponseData responseData = userService.registForm(user, request);
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");
        redirectStrategy.sendRedirect(request, response, "/to-demo-signIn?message=" + URLEncoder.encode(responseData.getMessage(), "UTF-8"));
    }

    @PostMapping("/find/password")
    public void findPassword(User user, HttpServletRequest request, HttpServletResponse response) throws IOException {
        ResponseData responseData = userService.registFindPassword(user);
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");
        redirectStrategy.sendRedirect(request, response, "/to-demo-signIn?message=" + URLEncoder.encode(responseData.getMessage(), "UTF-8"));
    }

    @GetMapping("/regist/form/sendmobile")
    public ResponseData formRegistSendSmsCode(@RequestParam("mobile") String mobile, HttpServletRequest request) {
        ResponseData responseData = new ResponseData();
        if (LimitSmsCodeSendTimes.validateTimes(request, redisTemplate, mobile)) {
            // 验证码5分钟有效
            String smsCode = RandomStringUtils.randomNumeric(6);
            redisTemplate.opsForValue().set("springsecurity:regist:sms:smsCode_" + mobile, smsCode, 300, TimeUnit.SECONDS);
            defaultSmsCodeSender.send(mobile, smsCode);

            responseData.setSuccess(Boolean.TRUE);
            responseData.setMessage("发送成功");
            return responseData;
        } else {
            responseData.setSuccess(Boolean.FALSE);
            responseData.setMessage("近一分钟已发送过手机验证码");
            return responseData;
        }
    }

    @GetMapping("/info")
    public ResponseData getUserInfo(@AuthenticationPrincipal UserDetails user) {
        return new ResponseData(userService.getUserInfo(user));
    }

    @PostMapping("/update")
    public ResponseData updateUserInfo(UserEntity userEntity) {
        return userService.updateUserInfo(userEntity);
    }

    @PostMapping("/uploadHeadImage")
    public ResponseData uploadHeadImage(@RequestParam("file") MultipartFile file,
                                        @RequestParam("id") Integer id) {
        ResponseData responseData = new ResponseData();
        if (file == null) {
            responseData.setMessage("请文件不能为空");
            responseData.setSuccess(Boolean.FALSE);
            return responseData;
        }
        if (id == null) {
            responseData.setMessage("用户ID不能为空");
            responseData.setSuccess(Boolean.FALSE);
            return responseData;
        }
        return userService.uploadHeadImage(file, id);
    }

    @RequestMapping("/headImage")
    public void getUserHeadImage(@AuthenticationPrincipal UserDetails user, HttpServletRequest request, HttpServletResponse response) {
        userService.getUserHeadImage(user,request, response);
    }

    /**
     * 获取认证信息
     *
     * @return
     */
    @GetMapping("/me")
    public Object getCurrentUserDetail() {
        // 从security上下文中获取认证信息
        return SecurityContextHolder.getContext().getAuthentication();

        /*
        参数形式获取认证信息1
        public Object getCurrentUserDetail(Authentication authentication) {
            return authentication;
        }
        */

        /*
        参数形式获取认证信息2
        public Object getCurrentUserDetail(@AuthenticationPrincipal UserDetails user) {
            return user;
        }
        */
    }

    @PostMapping
    public User create(@Valid @RequestBody User user, BindingResult errors) {

        if (errors.hasErrors()) {
            errors.getAllErrors().stream().forEach(error -> {
                FieldError fieldError = (FieldError) error;
                System.out.println(fieldError.getField() + " " + fieldError.getDefaultMessage());
            });
        }

        System.out.println(user.getId());
        System.out.println(user.getUsername());
        System.out.println(user.getPassword());
        System.out.println(user.getBirthday());

        user.setId("1");
        return user;
    }

    @PutMapping(value = "/{id:\\d+}")
    public User update(@Valid @RequestBody User user, BindingResult errors) {

        if (errors.hasErrors()) {
            errors.getAllErrors().stream().forEach(error -> {
                FieldError fieldError = (FieldError) error;
                System.out.println(fieldError.getField() + " " + fieldError.getDefaultMessage());
            });
        }

        System.out.println(user.getId());
        System.out.println(user.getUsername());
        System.out.println(user.getPassword());
        System.out.println(user.getBirthday());

        user.setId("1");
        return user;
    }

    @DeleteMapping(value = "/{id:\\d+}")
    public void delete(@PathVariable(value = "id") String id) {
        System.out.println("delete id " + id);
    }

    /**
     * 接收condition查询条件
     * Pageable是springMvc自带的分页方式
     *
     * @param condition 一个dto
     * @param pageable  分页参数
     * @return
     */
    @GetMapping
    @JsonView(User.UserSimpleView.class)
    public List<User> query(UserQueryCondition condition,
                            @PageableDefault(page = 1, size = 15, sort = "age,desc") Pageable pageable) {

        System.out.println(ReflectionToStringBuilder.toString(condition, ToStringStyle.MULTI_LINE_STYLE));

        System.out.println(pageable.getPageNumber());
        System.out.println(pageable.getPageSize());
        System.out.println(pageable.getSort());

        List<User> userList = new ArrayList<>();
        userList.add(new User());
        userList.add(new User());
        userList.add(new User());
        return userList;
    }

    /**
     * 一般正常使用的方式:@GetMapping(value = "/{id}")
     * 现在使用的是"/{id:\\d+}"，表示只接受数字
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/{id:\\d+}")
    @JsonView(User.UserDetailView.class)
    public User getInfo(@PathVariable(value = "id") String id) {
        //throw new RuntimeException("user is not exists");
        //throw new UserNotExistException(id);
        System.out.println("进入getInfo方法");
        User user = new User();
        user.setUsername("tom");
        return user;
    }

}
