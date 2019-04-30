package com.hand.dto;

import com.fasterxml.jackson.annotation.JsonView;
import com.hand.validator.MyConstraint;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Past;
import java.util.Date;

/**
 * @author senlin.yang@com.hand-china.com
 * @version V1.0
 * @Date 2019-1-17
 * @description
 */
public class User {

    public interface UserSimpleView {};

    public interface UserDetailView extends UserSimpleView{};

    private String id;

    @MyConstraint(message = "name上的自定义注解")
    private String username;

    @NotBlank(message = "密码不能为空")
    private String password;

    @Past(message = "生日不能为未来时间")
    private Date birthday;

    @JsonView(value = UserSimpleView.class)
    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    @JsonView(value = UserSimpleView.class)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @JsonView(value = UserSimpleView.class)
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @JsonView(value = UserDetailView.class)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
