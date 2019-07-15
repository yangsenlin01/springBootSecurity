package com.hand.web.service.impl;

import com.hand.dto.User;
import com.hand.security.core.support.ResponseData;
import com.hand.web.entity.Attachment;
import com.hand.web.entity.File;
import com.hand.web.entity.UserEntity;
import com.hand.web.mapper.AttachmentMapper;
import com.hand.web.mapper.FileMapper;
import com.hand.web.mapper.UserMapper;
import com.hand.web.service.UserService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author senlin.yang@hand-china.com
 * @version V1.0
 * @Date 2019-7-12
 * @description
 */

@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private FileMapper fileMapper;

    @Autowired
    private AttachmentMapper attachmentMapper;

    @Value("${user.head.image.path}")
    private String filePath;

    @Override
    public ResponseData registForm(User user) {
        ResponseData responseData = new ResponseData(Boolean.TRUE);

        // 校验当前注册用户名是否已存在
        if (validateUsername(user.getUsername())) {
            responseData.setSuccess(Boolean.FALSE);
            responseData.setMessage("当前用户名已被注册");
            return responseData;
        }

        // 校验当前注册手机号是否已存在
        if (validateMobile(user.getPhone())) {
            responseData.setSuccess(Boolean.FALSE);
            responseData.setMessage("当前手机号已被注册");
            return responseData;
        }

        // 验证码校验
        responseData = validateSmsCode(responseData, user.getPhone(), user.getSmsCode());
        if (!responseData.isSuccess()) {
            return responseData;
        }

        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(user, userEntity);
        userEntity.setFullName(user.getUsername());
        if (StringUtils.isBlank(userEntity.getPassword())) {
            userEntity.setPassword("123456");
        }
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        if (userMapper.insertUseGeneratedKeys(userEntity) == 1) {
            responseData.setMessage("账号：" + userEntity.getUsername() + "已注册成功");
        }
        return responseData;
    }

    @Override
    public UserEntity registSocial(String nickName) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(nickName);

        List<UserEntity> userEntityList = userMapper.select(userEntity);
        if (CollectionUtils.isNotEmpty(userEntityList)) {
            userEntity.setUsername(userEntity.getUsername() + (userEntityList.size() + 1));
        }
        userEntity.setFullName(nickName);
        // 默认密码123456
        userEntity.setPassword(passwordEncoder.encode("123456"));
        userMapper.insertUseGeneratedKeys(userEntity);

        return userMapper.selectOne(userEntity);
    }

    @Override
    public ResponseData registFindPassword(User user) {
        ResponseData responseData = new ResponseData(Boolean.TRUE);

        // 验证码校验
        responseData = validateSmsCode(responseData, user.getPhone(), user.getSmsCode());
        if (!responseData.isSuccess()) {
            return responseData;
        }

        UserEntity userEntity = new UserEntity();
        userEntity.setPhone(user.getPhone());

        userEntity = userMapper.selectOne(userEntity);
        if (userEntity == null) {
            responseData.setMessage("手机号" + user.getPhone() + "未注册");
            responseData.setSuccess(Boolean.FALSE);
            return responseData;
        }

        userEntity.setPassword(passwordEncoder.encode(user.getPassword()));
        userMapper.updatePasswordByPhone(userEntity);

        responseData.setMessage(userEntity.getPhone() + "(" + userEntity.getUsername() + ")找回密码成功");
        return responseData;
    }

    @Override
    public List<UserEntity> getUserInfo(UserDetails user) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(user.getUsername());
        userEntity = userMapper.selectOne(userEntity);

        List<UserEntity> userEntityList = new ArrayList<>(1);
        userEntityList.add(userEntity);
        return userEntityList;
    }

    @Override
    public ResponseData updateUserInfo(UserEntity userEntity) {
        ResponseData responseData = new ResponseData(Boolean.TRUE);
        if (userMapper.updateWithoutIgnoreByPrimaryKey(userEntity) == 1) {
            responseData.setMessage("更新信息成功");
        } else {
            responseData.setSuccess(Boolean.FALSE);
            responseData.setMessage("更新信息失败");
        }
        return responseData;
    }

    @Override
    public ResponseData uploadHeadImage(MultipartFile file, Integer id) {
        ResponseData responseData = new ResponseData(Boolean.TRUE);

        if (!checkFile(file)) {
            responseData.setSuccess(Boolean.FALSE);
            responseData.setMessage("请上传正确的头像文件");
            return responseData;
        }

        Attachment attachment = new Attachment();
        attachment.setSourceType("user_head_key");
        attachment.setSourceKey(id.toString());
        attachment = attachmentMapper.selectOne(attachment);
        Integer attaId = null;
        if (attachment == null) {
            Attachment attachmentInsert = new Attachment();
            attachmentInsert.setName("用户头像");
            attachmentInsert.setSourceType("user_head_key");
            attachmentInsert.setSourceKey(id.toString());
            attachmentInsert.setStatus("1");
            attachmentInsert.setStartActiveDate(new Date());
            attachmentMapper.insert(attachmentInsert);
            attaId = attachmentInsert.getAttachmentId();
        } else {
            attaId = attachment.getAttachmentId();
        }

        String fileName = file.getOriginalFilename();
        String fileType = StringUtils.substringAfterLast(fileName, ".");

        File userFile = new File();
        userFile.setAttachmentId(attaId);
        userFile = fileMapper.selectOne(userFile);
        String oldFileName = null;
        if (userFile == null) {
            File userFileInsert = new File();
            userFileInsert.setAttachmentId(attaId);
            userFileInsert.setFileName(fileName);
            userFileInsert.setFilePath(filePath + fileName);
            userFileInsert.setFileSize(String.valueOf(file.getSize()));
            userFileInsert.setFileType(fileType);
            userFileInsert.setUploadDate(new Date());
            fileMapper.insert(userFileInsert);
        } else {

            // 需要删除之前的头像文件
            oldFileName = userFile.getFileName();

            userFile.setFileName(fileName);
            userFile.setFilePath(filePath + fileName);
            userFile.setFileSize(String.valueOf(file.getSize()));
            userFile.setFileType(fileType);
            userFile.setUploadDate(new Date());
            fileMapper.updateByPrimaryKey(userFile);
        }

        try {
            uploadFile(file.getBytes(), filePath, file.getOriginalFilename(), oldFileName);
        } catch (IOException e) {
            logger.info("上传文件出错");
            responseData.setSuccess(Boolean.FALSE);
            responseData.setMessage("上传文件出错");
            return responseData;
        }
        responseData.setMessage("上传成功");
        return responseData;
    }

    @Override
    public void getUserHeadImage(UserDetails user, HttpServletRequest request, HttpServletResponse response) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(user.getUsername());
        userEntity = userMapper.selectOne(userEntity);

        Attachment attachment = new Attachment();
        attachment.setSourceKey(userEntity.getId().toString());
        attachment.setSourceType("user_head_key");
        attachment = attachmentMapper.selectOne(attachment);
        if (attachment != null) {
            File file = new File();
            file.setAttachmentId(attachment.getAttachmentId());
            file = fileMapper.selectOne(file);
            if (file != null) {
                String filePath = file.getFilePath();
                response.setContentType("multipart/form-data");
                try (FileInputStream ips = new FileInputStream(new java.io.File(filePath));
                     ServletOutputStream out= response.getOutputStream()) {
                    //读取文件流
                    int len = 0;
                    byte[] buffer = new byte[1024 * 10];
                    while ((len = ips.read(buffer)) != -1){
                        out.write(buffer,0,len);
                    }
                    out.flush();
                } catch (IOException e) {
                    logger.info("获取头像信息出错");
                }
            }
        }
    }

    /**
     * 检查上传的头像格式是否正确
     *
     * @param origFile
     * @return
     */
    private boolean checkFile(MultipartFile origFile) {

        String fileName = origFile.getOriginalFilename();
        String prefix = fileName.substring(fileName.lastIndexOf("."));
        Long fileSize = origFile.getSize() / 1024;
        logger.info("上传图片大小为" + fileSize + "KB");

        try {
            // 创建临时文件
            java.io.File file = java.io.File.createTempFile(prefix, String.valueOf(System.currentTimeMillis()));
            FileUtils.copyInputStreamToFile(origFile.getInputStream(), file);
            // 通过临时文件获取图片流
            BufferedImage bufferedImage = ImageIO.read(file);
            if (bufferedImage == null) {
                return false;
            }
            // 通过图片流获取图片宽度
            // Integer width = bufferedImage.getWidth();
            // 通过图片流获取图片高度
            // Integer height = bufferedImage.getHeight();
            // 省略逻辑判断
        } catch (Exception e) {
            logger.info("获取头像文件出错");
        }

        return true;
    }

    /**
     * 上传头像
     *
     * @param file
     * @param filePath
     * @param fileName
     * @param oldFileName
     * @return
     */
    private boolean uploadFile(byte[] file, String filePath, String fileName, String oldFileName) {
        java.io.File targetFile = new java.io.File(filePath);
        if (!targetFile.exists()) {
            targetFile.mkdirs();
        }
        try (FileOutputStream out = new FileOutputStream(filePath + fileName)) {
            out.write(file);
            out.flush();
        } catch (IOException e) {
            logger.info("上传文件出错");
        }

        java.io.File oldFile = new java.io.File(filePath + oldFileName);
        if (oldFile.exists()) {
            oldFile.delete();
        }
        return true;
    }

    /**
     * 当前用户名是否已经被注册
     *
     * @param username
     * @return
     */
    private Boolean validateUsername(String username) {
        UserEntity validateUsername = new UserEntity();
        validateUsername.setUsername(username);
        validateUsername = userMapper.selectOne(validateUsername);
        return validateUsername == null ? Boolean.FALSE : Boolean.TRUE;
    }

    /**
     * 当前手机号是否已经被注册
     *
     * @param mobile
     * @return
     */
    private Boolean validateMobile(String mobile) {
        UserEntity validateMobile = new UserEntity();
        validateMobile.setPhone(mobile);
        validateMobile = userMapper.selectOne(validateMobile);
        return validateMobile == null ? Boolean.FALSE : Boolean.TRUE;
    }

    private ResponseData validateSmsCode(ResponseData responseData, String phone, String code) {
        Object validateSmsCode = redisTemplate.opsForValue().get("springsecurity:regist:sms:smsCode_" + phone);
        if (validateSmsCode == null) {
            responseData.setSuccess(Boolean.FALSE);
            responseData.setMessage("验证码不存在或已失效");
            return responseData;
        } else {
            String mobile = (String) validateSmsCode;
            if (!StringUtils.equals(mobile, code)) {
                responseData.setSuccess(Boolean.FALSE);
                responseData.setMessage("验证码错误");
                return responseData;
            } else {
                redisTemplate.delete("springsecurity:regist:sms:smsCode_" + phone);
                return responseData;
            }
        }
    }
}
