package com.xiaoxie.wechatproject.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiaoxie.wechatproject.entity.ActivityRegistration;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 活动报名Mapper接口
 */
@Mapper
public interface ActivityRegistrationMapper extends BaseMapper<ActivityRegistration> {
    
    /**
     * 查询用户是否已报名某个活动
     */
    @Select("SELECT * FROM activity_registration WHERE activity_id = #{activityId} AND user_id = #{userId} AND status = 1")
    ActivityRegistration findByActivityIdAndUserId(@Param("activityId") Long activityId, @Param("userId") Long userId);
    
    /**
     * 查询活动的所有报名者
     */
    @Select("SELECT ar.*, u.nickname, u.avatar_url " +
            "FROM activity_registration ar " +
            "LEFT JOIN user u ON ar.user_id = u.id " +
            "WHERE ar.activity_id = #{activityId} AND ar.status = 1 " +
            "ORDER BY ar.register_time DESC")
    List<ActivityRegistration> selectByActivityId(@Param("activityId") Long activityId);
    
    /**
     * 查询用户的所有报名记录
     */
    @Select("SELECT ar.*, a.title, a.description, a.start_date, a.start_time, a.location, a.images, " +
            "c.name as category_name " +
            "FROM activity_registration ar " +
            "LEFT JOIN activity a ON ar.activity_id = a.id " +
            "LEFT JOIN activity_category c ON a.category_id = c.id " +
            "WHERE ar.user_id = #{userId} " +
            "ORDER BY ar.register_time DESC")
    List<ActivityRegistration> selectByUserId(@Param("userId") Long userId);

    @Select("select * from activity_registration where activity_id=#{activityId} and user_id=#{userId}")
    ActivityRegistration selectActivity_user(Integer activityId, Integer userId);

    @Update("UPDATE activity_registration " +
            "SET status = 1 " +
            "WHERE activity_id = #{activityId} AND user_id = #{userId}")
    int updateRegistration(@Param("activityId") Integer activityId,
                           @Param("userId") Integer userId);

    /**
     * 统计活动的报名人数
     */
    @Select("SELECT COUNT(*) FROM activity_registration WHERE activity_id = #{activityId} AND status = 1")
    int countByActivityId(@Param("activityId") Long activityId);
    
    /**
     * 查询用户即将参加的活动
     */
    @Select("SELECT ar.*, a.title, a.description, a.start_date, a.start_time, a.location, a.images, " +
            "c.name as category_name " +
            "FROM activity_registration ar " +
            "LEFT JOIN activity a ON ar.activity_id = a.id " +
            "LEFT JOIN activity_category c ON a.category_id = c.id " +
            "WHERE ar.user_id = #{userId} AND ar.status = 1 " +
            "AND a.start_date >= CURDATE() " +
            "ORDER BY a.start_date ASC, a.start_time ASC")
    List<ActivityRegistration> selectUpcomingByUserId(@Param("userId") Long userId);
    
    /**
     * 查询用户已完成的活动
     */
    @Select("SELECT ar.*, a.title, a.description, a.start_date, a.start_time, a.location, a.images, " +
            "c.name as category_name " +
            "FROM activity_registration ar " +
            "LEFT JOIN activity a ON ar.activity_id = a.id " +
            "LEFT JOIN activity_category c ON a.category_id = c.id " +
            "WHERE ar.user_id = #{userId} AND ar.status = 1 " +
            "AND a.start_date < CURDATE() " +
            "ORDER BY a.start_date DESC, a.start_time DESC")
    List<ActivityRegistration> selectCompletedByUserId(@Param("userId") Long userId);
}
