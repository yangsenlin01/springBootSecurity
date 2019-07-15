package com.hand.web.mybatis;

import com.hand.web.mybatis.special.DemoUpdateMapper;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * @author senlin.yang@hand-china.com
 * @version V1.0
 * @Date 2019-7-12
 * @description 通用基础mapper
 */
public interface BaseMapper<T> extends Mapper<T>, MySqlMapper<T>, DemoUpdateMapper<T> {
}
