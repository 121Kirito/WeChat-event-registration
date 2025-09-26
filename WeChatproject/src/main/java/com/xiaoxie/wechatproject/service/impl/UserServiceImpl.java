package com.xiaoxie.wechatproject.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaoxie.wechatproject.entity.User;
import com.xiaoxie.wechatproject.mapper.UserMapper;
import com.xiaoxie.wechatproject.service.ActivityRegistrationService;
import com.xiaoxie.wechatproject.service.ActivityFavoriteService;
import com.xiaoxie.wechatproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 用户服务实现类
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    
    @Autowired
    private ActivityRegistrationService activityRegistrationService;
    
    @Autowired
    private ActivityFavoriteService activityFavoriteService;
    
    @Override
    public User findByOpenid(String openid) {
        return baseMapper.findByOpenid(openid);
    }
    
    @Override
    public User findByPhone(String phone) {
        return baseMapper.findByPhone(phone);
    }
    
    @Override
    @Transactional
    public User createOrUpdateUser(User user) {
        User existingUser = findByOpenid(user.getOpenid());
        if (existingUser != null) {
            // 更新用户信息
            existingUser.setNickname(user.getNickname());
            existingUser.setAvatarUrl(user.getAvatarUrl());
            existingUser.setGender(user.getGender());
            existingUser.setCity(user.getCity());
            existingUser.setProvince(user.getProvince());
            existingUser.setCountry(user.getCountry());
            existingUser.setUpdateTime(LocalDateTime.now());
            updateById(existingUser);
            return existingUser;
        } else {
            // 创建新用户
            user.setStatus(1);
            user.setLevel(1);
            save(user);
            return user;
        }
    }
    
    @Override
    @Transactional
    public boolean updateUserInfo(User user) {
        user.setUpdateTime(LocalDateTime.now());
        return updateById(user);
    }
    
    @Override
    public UserStats getUserStats(Long userId) {
        UserStats stats = new UserStats();
        
        // 总参与活动数
        Long totalActivities = activityRegistrationService.count(
            new QueryWrapper<com.xiaoxie.wechatproject.entity.ActivityRegistration>()
                .eq("user_id", userId)
                .eq("status", 1)
        );
        stats.setTotalActivities(totalActivities);
        
        // 已完成活动数
        Long completedActivities = activityRegistrationService.count(
            new QueryWrapper<com.xiaoxie.wechatproject.entity.ActivityRegistration>()
                .eq("user_id", userId)
                .eq("status", 1)
                .inSql("activity_id", "SELECT id FROM activity WHERE start_date < CURDATE()")
        );
        stats.setCompletedActivities(completedActivities);
        
        // 即将开始活动数
        Long upcomingActivities = activityRegistrationService.count(
            new QueryWrapper<com.xiaoxie.wechatproject.entity.ActivityRegistration>()
                .eq("user_id", userId)
                .eq("status", 1)
                .inSql("activity_id", "SELECT id FROM activity WHERE start_date >= CURDATE()")
        );
        stats.setUpcomingActivities(upcomingActivities);
        
        // 收藏活动数
        Long favoriteActivities = activityFavoriteService.count(
            new QueryWrapper<com.xiaoxie.wechatproject.entity.ActivityFavorite>()
                .eq("user_id", userId)
        );
        stats.setFavoriteActivities(favoriteActivities);
        
        // 用户等级（根据参与活动数计算）
        User user = getById(userId);
        if (user != null) {
            stats.setLevel(user.getLevel());
        } else {
            stats.setLevel(1);
        }
        
        return stats;
    }
}
