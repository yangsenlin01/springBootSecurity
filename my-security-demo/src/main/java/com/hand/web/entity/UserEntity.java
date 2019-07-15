package com.hand.web.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hand.annotation.UpdateIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author senlin.yang@hand-china.com
 * @version V1.0
 * @Date 2019-7-12
 * @description
 */

@Getter
@Setter
@ToString
@Table(name = "demo_user")
public class UserEntity implements Serializable {

    private static final long serialVersionUID = 4937784473141764231L;
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 用户名，登录名
     */

    private String username;

    /**
     * 用户名称
     */
    private String fullName;

    /**
     * 密码
     */
    @JsonIgnore
    @UpdateIgnore
    private String password;

    /**
     * 电话
     */
    private String phone;

    /**
     * 性别(0:女,1:男)
     */
    private String sex;

    /**
     * 头像
     */
    private String headImage;

    /**
     * 生日
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birthday;

    /**
     * 所在地
     */
    private String address;

}
