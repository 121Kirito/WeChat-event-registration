package com.xiaoxie.wechatproject.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaoxie.wechatproject.entity.Activity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 活动Mapper接口
 */
@Mapper
public interface ActivityMapper extends BaseMapper<Activity> {
    
    /**
     * 分页查询活动列表（带关联信息）
     */
    @Select("SELECT a.*, c.name as category_name, c.icon as category_icon, " +
            "u.nickname as organizer_name, u.avatar_url as organizer_avatar " +
            "FROM activity a " +
            "LEFT JOIN activity_category c ON a.category_id = c.id " +
            "LEFT JOIN user u ON a.organizer_id = u.id " +
            "WHERE a.status = 1 " +
            "ORDER BY a.create_time DESC")
    IPage<Activity> selectActivityPageWithDetails(Page<Activity> page);
    
    /**
     * 根据分类查询活动
     */
    @Select("SELECT a.*, c.name as category_name, c.icon as category_icon, " +
            "u.nickname as organizer_name, u.avatar_url as organizer_avatar " +
            "FROM activity a " +
            "LEFT JOIN activity_category c ON a.category_id = c.id " +
            "LEFT JOIN user u ON a.organizer_id = u.id " +
            "WHERE a.status = 1 AND a.category_id = #{categoryId} " +
            "ORDER BY a.create_time DESC")
    List<Activity> selectByCategoryId(@Param("categoryId") Long categoryId);
    
    /**
     * 搜索活动
     */
    @Select("SELECT a.*, c.name as category_name, c.icon as category_icon, " +
            "u.nickname as organizer_name, u.avatar_url as organizer_avatar " +
            "FROM activity a " +
            "LEFT JOIN activity_category c ON a.category_id = c.id " +
            "LEFT JOIN user u ON a.organizer_id = u.id " +
            "WHERE a.status = 1 " +
            "AND (a.title LIKE CONCAT('%', #{keyword}, '%') " +
            "OR a.description LIKE CONCAT('%', #{keyword}, '%')) " +
            "ORDER BY a.create_time DESC")
    List<Activity> searchActivities(@Param("keyword") String keyword);
    
    /**
     * 查询用户发布的活动
     */
    @Select("SELECT a.*, c.name as category_name, c.icon as category_icon " +
            "FROM activity a " +
            "LEFT JOIN activity_category c ON a.category_id = c.id " +
            "WHERE a.organizer_id = #{organizerId} " +
            "ORDER BY a.create_time DESC")
    List<Activity> selectByOrganizerId(@Param("organizerId") Long organizerId);
    
    /**
     * 增加浏览次数
     */
    @Update("UPDATE activity SET view_count = view_count + 1 WHERE id = #{id}")
    int incrementViewCount(@Param("id") Long id);


    /**
     * 更新报名人数
     */
    @Update("UPDATE activity SET registered_count = registered_count + #{increment} WHERE id = #{id}")
    int updateRegisteredCount(@Param("id") Long id, @Param("increment") Integer increment);
    
    /**
     * 查询即将开始的活动
     */
    @Select("SELECT a.*, c.name as category_name, c.icon as category_icon, " +
            "u.nickname as organizer_name, u.avatar_url as organizer_avatar " +
            "FROM activity a " +
            "LEFT JOIN activity_category c ON a.category_id = c.id " +
            "LEFT JOIN user u ON a.organizer_id = u.id " +
            "WHERE a.status = 1 AND a.start_date >= CURDATE() " +
            "ORDER BY a.start_date ASC, a.start_time ASC")
    List<Activity> selectUpcomingActivities();
    
    /**
     * 查询推荐活动
     */
    @Select("SELECT a.*, c.name as category_name, c.icon as category_icon, " +
            "u.nickname as organizer_name, u.avatar_url as organizer_avatar " +
            "FROM activity a " +
            "LEFT JOIN activity_category c ON a.category_id = c.id " +
            "LEFT JOIN user u ON a.organizer_id = u.id " +
            "WHERE a.status = 1 AND a.is_featured = 1 " +
            "ORDER BY a.create_time DESC")
    List<Activity> selectFeaturedActivities();
}
