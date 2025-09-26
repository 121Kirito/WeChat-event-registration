package com.xiaoxie.wechatproject.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaoxie.wechatproject.entity.User;

/**
 * 用户服务接口
 */
public interface UserService extends IService<User> {
    
    /**
     * 根据openid查询用户
     */
    User findByOpenid(String openid);
    
    /**
     * 根据手机号查询用户
     */
    User findByPhone(String phone);
    
    /**
     * 创建或更新用户
     */
    User createOrUpdateUser(User user);
    
    /**
     * 更新用户信息
     */
    boolean updateUserInfo(User user);
    
    /**
     * 获取用户统计数据
     */
    UserStats getUserStats(Long userId);
    
    /**
     * 用户统计信息内部类
     */
    class UserStats {
        private Long totalActivities;
        private Long completedActivities;
        private Long upcomingActivities;
        private Long favoriteActivities;
        private Integer level;
        
        public UserStats() {}
        
        public UserStats(Long totalActivities, Long completedActivities, Long upcomingActivities, Long favoriteActivities, Integer level) {
            this.totalActivities = totalActivities;
            this.completedActivities = completedActivities;
            this.upcomingActivities = upcomingActivities;
            this.favoriteActivities = favoriteActivities;
            this.level = level;
        }
        
        // Getters and Setters
        public Long getTotalActivities() {
            return totalActivities;
        }
        
        public void setTotalActivities(Long totalActivities) {
            this.totalActivities = totalActivities;
        }
        
        public Long getCompletedActivities() {
            return completedActivities;
        }
        
        public void setCompletedActivities(Long completedActivities) {
            this.completedActivities = completedActivities;
        }
        
        public Long getUpcomingActivities() {
            return upcomingActivities;
        }
        
        public void setUpcomingActivities(Long upcomingActivities) {
            this.upcomingActivities = upcomingActivities;
        }
        
        public Long getFavoriteActivities() {
            return favoriteActivities;
        }
        
        public void setFavoriteActivities(Long favoriteActivities) {
            this.favoriteActivities = favoriteActivities;
        }
        
        public Integer getLevel() {
            return level;
        }
        
        public void setLevel(Integer level) {
            this.level = level;
        }
    }
}
