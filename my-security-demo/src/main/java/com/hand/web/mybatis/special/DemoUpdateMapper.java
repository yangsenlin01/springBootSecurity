package com.hand.web.mybatis.special;

import com.hand.web.mybatis.provider.DemoUpdateProvider;
import org.apache.ibatis.annotations.UpdateProvider;

/**
 * @author senlin.yang@hand-china.com
 * @version V1.0
 * @Date 2019-7-15
 * @description
 */
public interface DemoUpdateMapper<T> {

    /**
     * 根据主键更新数据，忽视被UpdateIgnore注解标记的字段
     *
     * @param var1
     * @return
     */
    @UpdateProvider(
            type = DemoUpdateProvider.class,
            method = "dynamicSQL"
    )
    int updateWithoutIgnoreByPrimaryKey(T var1);

}
