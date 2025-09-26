package com.xiaoxie.wechatproject.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaoxie.wechatproject.entity.ActivityFavorite;
import com.xiaoxie.wechatproject.mapper.ActivityFavoriteMapper;
import com.xiaoxie.wechatproject.service.ActivityFavoriteService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 活动收藏服务实现类
 */
@Service
public class ActivityFavoriteServiceImpl extends ServiceImpl<ActivityFavoriteMapper, ActivityFavorite> implements ActivityFavoriteService {
    
    @Override
    @Transactional
    public boolean favoriteActivity(Long activityId, Long userId) {
        // 检查是否已收藏
        if (isUserFavorited(activityId, userId)) {
            return true;
        }
        
        ActivityFavorite favorite = new ActivityFavorite(activityId, userId);
        return save(favorite);
    }
    
    @Override
    @Transactional
    public boolean unfavoriteActivity(Long activityId, Long userId) {
        QueryWrapper<ActivityFavorite> wrapper = new QueryWrapper<>();
        wrapper.eq("activity_id", activityId).eq("user_id", userId);
        return remove(wrapper);
    }
    
    @Override
    public boolean isUserFavorited(Long activityId, Long userId) {
        ActivityFavorite favorite = baseMapper.findByActivityIdAndUserId(activityId, userId);
        return favorite != null;
    }
    
    @Override
    public List<ActivityFavorite> getUserFavorites(Long userId) {
        return baseMapper.selectByUserId(userId);
    }
}
