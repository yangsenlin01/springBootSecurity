package com.hand.web.mapper;

import com.hand.web.entity.UserEntity;
import com.hand.web.mybatis.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author senlin.yang@hand-china.com
 * @version V1.0
 * @Date 2019-7-12
 * @description
 */
public interface UserMapper extends BaseMapper<UserEntity> {

    /**
     * 根据手机号修改密码
     * @param userEntity
     */
    void updatePasswordByPhone(UserEntity userEntity);
}
