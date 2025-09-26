package com.xiaoxie.wechatproject.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaoxie.wechatproject.entity.ActivityRegistration;

import java.util.List;

/**
 * 活动报名服务接口
 */
public interface ActivityRegistrationService extends IService<ActivityRegistration> {
    
    /**
     * 用户报名活动
     */
    boolean registerActivity(Long activityId, Long userId, String name, String phone, String remark);
    
    /**
     * 取消报名
     */
    boolean cancelRegistration(Long activityId, Long userId);
    
    /**
     * 检查用户是否已报名
     */
    boolean isUserRegistered(Long activityId, Long userId);
    
    /**
     * 查询用户报名记录
     */
    List<ActivityRegistration> getUserRegistrations(Long userId);
    
    /**
     * 查询用户即将参加的活动
     */
    List<ActivityRegistration> getUserUpcomingActivities(Long userId);
    
    /**
     * 查询用户已完成的活动
     */
    List<ActivityRegistration> getUserCompletedActivities(Long userId);
    
    /**
     * 查询活动的所有报名者
     */
    List<ActivityRegistration> getActivityParticipants(Long activityId);
    
    /**
     * 签到
     */
    boolean checkIn(Long activityId, Long userId);
}
