package com.xiaoxie.wechatproject.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiaoxie.wechatproject.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 用户Mapper接口
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
    
    /**
     * 根据openid查询用户
     */
    @Select("SELECT * FROM user WHERE openid = #{openid} AND status = 1")
    User findByOpenid(@Param("openid") String openid);
    
    /**
     * 根据手机号查询用户
     */
    @Select("SELECT * FROM user WHERE phone = #{phone} AND status = 1")
    User findByPhone(@Param("phone") String phone);
}
