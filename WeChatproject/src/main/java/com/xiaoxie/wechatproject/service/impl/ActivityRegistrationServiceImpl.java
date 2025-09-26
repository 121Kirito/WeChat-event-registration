package com.xiaoxie.wechatproject.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaoxie.wechatproject.entity.Activity;
import com.xiaoxie.wechatproject.entity.ActivityRegistration;
import com.xiaoxie.wechatproject.mapper.ActivityMapper;
import com.xiaoxie.wechatproject.mapper.ActivityRegistrationMapper;
import com.xiaoxie.wechatproject.service.ActivityRegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 活动报名服务实现类
 */
@Service
public class ActivityRegistrationServiceImpl extends ServiceImpl<ActivityRegistrationMapper, ActivityRegistration> implements ActivityRegistrationService {
    
    @Autowired
    private ActivityMapper activityMapper;
    
    @Override
    @Transactional
    public boolean registerActivity(Long activityId, Long userId, String name, String phone, String remark) {
        // 检查活动是否存在且可报名
        Activity activity = activityMapper.selectById(activityId);
        if (activity == null || activity.getStatus() != 1) {
            throw new RuntimeException("活动不存在或已结束");
        }
        
        // 检查是否已报名
        if (isUserRegistered(activityId, userId)) {
            throw new RuntimeException("您已报名此活动");
        }
        
        // 检查报名人数是否已满
        if (activity.getRegisteredCount() >= activity.getMaxParticipants()) {
            throw new RuntimeException("活动报名人数已满");
        }
        
        // 检查报名截止时间
        if (LocalDateTime.now().isAfter(activity.getDeadline())) {
            throw new RuntimeException("活动报名已截止");
        }
        
        // 创建报名记录
        ActivityRegistration registration = new ActivityRegistration(activityId, userId, name, phone);
        registration.setRemark(remark);
        registration.setRegisterTime(LocalDateTime.now());
        
        boolean result = save(registration);
        if (result) {
            // 更新活动报名人数
            activityMapper.updateRegisteredCount(activityId, 1);
        }
        
        return result;
    }
    
    @Override
    @Transactional
    public boolean cancelRegistration(Long activityId, Long userId) {
        ActivityRegistration registration = baseMapper.findByActivityIdAndUserId(activityId, userId);
        if (registration == null) {
            throw new RuntimeException("您未报名此活动");
        }
        
        if (registration.getStatus() != 1) {
            throw new RuntimeException("报名状态异常，无法取消");
        }
        
        // 更新报名状态为已取消
        registration.setStatus(0);
        boolean result = updateById(registration);
        
        if (result) {
            // 更新活动报名人数
            activityMapper.updateRegisteredCount(activityId, -1);
        }
        
        return result;
    }
    
    @Override
    public boolean isUserRegistered(Long activityId, Long userId) {
        ActivityRegistration registration = baseMapper.findByActivityIdAndUserId(activityId, userId);
        return registration != null && registration.getStatus() == 1;
    }
    
    @Override
    public List<ActivityRegistration> getUserRegistrations(Long userId) {
        return baseMapper.selectByUserId(userId);
    }
    
    @Override
    public List<ActivityRegistration> getUserUpcomingActivities(Long userId) {
        return baseMapper.selectUpcomingByUserId(userId);
    }
    
    @Override
    public List<ActivityRegistration> getUserCompletedActivities(Long userId) {
        return baseMapper.selectCompletedByUserId(userId);
    }
    
    @Override
    public List<ActivityRegistration> getActivityParticipants(Long activityId) {
        return baseMapper.selectByActivityId(activityId);
    }
    
    @Override
    @Transactional
    public boolean checkIn(Long activityId, Long userId) {
        ActivityRegistration registration = baseMapper.findByActivityIdAndUserId(activityId, userId);
        if (registration == null) {
            throw new RuntimeException("您未报名此活动");
        }
        
        if (registration.getStatus() != 1) {
            throw new RuntimeException("报名状态异常，无法签到");
        }
        
        registration.setStatus(2); // 2-已签到
        registration.setCheckinTime(LocalDateTime.now());
        
        return updateById(registration);
    }
}
