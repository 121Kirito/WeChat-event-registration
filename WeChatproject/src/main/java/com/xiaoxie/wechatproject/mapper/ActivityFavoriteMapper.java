package com.xiaoxie.wechatproject.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiaoxie.wechatproject.entity.ActivityFavorite;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 活动收藏Mapper接口
 */
@Mapper
public interface ActivityFavoriteMapper extends BaseMapper<ActivityFavorite> {
    
    /**
     * 查询用户是否已收藏某个活动
     */
    @Select("SELECT * FROM activity_favorite WHERE activity_id = #{activityId} AND user_id = #{userId}")
    ActivityFavorite findByActivityIdAndUserId(@Param("activityId") Long activityId, @Param("userId") Long userId);
    
    /**
     * 查询用户收藏的活动
     */
    @Select("SELECT af.*, a.title, a.description, a.start_date, a.start_time, a.location, " +
            "a.registered_count, a.max_participants, a.fee, a.images, a.tags, " +
            "c.name as category_name, c.icon as category_icon, " +
            "u.nickname as organizer_name, u.avatar_url as organizer_avatar " +
            "FROM activity_favorite af " +
            "LEFT JOIN activity a ON af.activity_id = a.id " +
            "LEFT JOIN activity_category c ON a.category_id = c.id " +
            "LEFT JOIN user u ON a.organizer_id = u.id " +
            "WHERE af.user_id = #{userId} AND a.status = 1 " +
            "ORDER BY af.create_time DESC")
    List<ActivityFavorite> selectByUserId(@Param("userId") Long userId);
}
