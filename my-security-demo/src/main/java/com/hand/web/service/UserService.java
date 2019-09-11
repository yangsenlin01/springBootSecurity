package com.hand.web.service;

import com.hand.dto.User;
import com.hand.security.core.support.ResponseData;
import com.hand.web.entity.UserEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author senlin.yang@hand-china.com
 * @version V1.0
 * @Date 2019-7-12
 * @description
 */
public interface UserService {

    /**
     * 表单注册用户
     *
     * @param user
     * @param request
     * @return
     */
    ResponseData registForm(User user, HttpServletRequest request);

    /**
     * 第三方登录时找不到用户直接使用社交账号的nickName在本系统注册用户
     *
     * @param nickName
     * @return
     */
    UserEntity registSocial(String nickName);

    /**
     * 找回密码
     *
     * @param user
     * @return
     */
    ResponseData registFindPassword(User user);

    /**
     * 获取用户信息
     *
     * @param user
     * @return
     */
    List<UserEntity> getUserInfo(UserDetails user);

    /**
     * 修改用户信息
     *
     * @param userEntity
     * @return
     */
    ResponseData updateUserInfo(UserEntity userEntity);

    /**
     * 上传头像
     * @param file
     * @param id
     * @return
     */
    ResponseData uploadHeadImage(MultipartFile file, Integer id);

    /**
     * 获取用户头像
     *
     * @param user
     * @param request
     * @param response
     */
    void getUserHeadImage(UserDetails user, HttpServletRequest request, HttpServletResponse response);

    /**
     * 上传学习文件
     *
     * @param file
     * @param request
     * @return
     */
    ResponseData uploadMyFile(MultipartFile file, HttpServletRequest request);

    /**
     * 下载文件
     *
     * @param fileName
     * @param request
     * @param response
     */
    void downloadMyFile(String fileName, HttpServletRequest request, HttpServletResponse response);
}
