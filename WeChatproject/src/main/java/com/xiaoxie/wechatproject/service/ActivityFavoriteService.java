package com.xiaoxie.wechatproject.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaoxie.wechatproject.entity.ActivityFavorite;

import java.util.List;

/**
 * 活动收藏服务接口
 */
public interface ActivityFavoriteService extends IService<ActivityFavorite> {
    
    /**
     * 收藏活动
     */
    boolean favoriteActivity(Long activityId, Long userId);
    
    /**
     * 取消收藏
     */
    boolean unfavoriteActivity(Long activityId, Long userId);
    
    /**
     * 检查用户是否已收藏
     */
    boolean isUserFavorited(Long activityId, Long userId);
    
    /**
     * 查询用户收藏的活动
     */
    List<ActivityFavorite> getUserFavorites(Long userId);
}
