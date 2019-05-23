package com.hand.web.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import com.hand.dto.User;
import com.hand.dto.UserQueryCondition;
import com.hand.exception.UserNotExistException;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * @author senlin.yang@com.hand-china.com
 * @version V1.0
 * @Date 2019-1-17
 * @description
 */

@RestController
@RequestMapping(value = "/user")
public class UserController {

    /**
     * 从security上下文中获取认证信息
     * @return
     */
    @GetMapping("/me")
    public Object getCurrentUserDetail() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * 参数形式获取认证信息
     *
     * @param authentication
     * @return
     */
    @GetMapping("/me2")
    public Object getCurrentUserDetail2(Authentication authentication) {
        return authentication;
    }

    /**
     * 获取认证信息，只返回用户信息
     * @param user
     * @return
     */
    @GetMapping("/me/user")
    public Object getCurrentUser(@AuthenticationPrincipal UserDetails user) {
        return user;
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
     * @param condition 一个dto
     * @param pageable 分页参数
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
     *  一般正常使用的方式:@GetMapping(value = "/{id}")
     *  现在使用的是"/{id:\\d+}"，表示只接受数字
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
