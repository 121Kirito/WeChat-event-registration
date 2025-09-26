package com.xiaoxie.wechatproject.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaoxie.wechatproject.entity.Activity;
import com.xiaoxie.wechatproject.entity.ActivityCategory;
import com.xiaoxie.wechatproject.entity.User;
import com.xiaoxie.wechatproject.mapper.ActivityMapper;
import com.xiaoxie.wechatproject.service.ActivityService;
import com.xiaoxie.wechatproject.service.ActivityCategoryService;
import com.xiaoxie.wechatproject.mapper.UserMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 活动服务实现类
 */
@Service
public class ActivityServiceImpl extends ServiceImpl<ActivityMapper, Activity> implements ActivityService {
    
    @Autowired
    private ActivityCategoryService activityCategoryService;

    @Autowired
    private UserMapper userMapper;
    
    @Override
    public IPage<Activity> getActivityPage(Page<Activity> page, String keyword, Long categoryId, String sortType) {
        QueryWrapper<Activity> wrapper = new QueryWrapper<>();
        wrapper.eq("status", 1);
        
        // 关键词搜索
        if (StringUtils.isNotBlank(keyword) && !"undefined".equalsIgnoreCase(keyword)) {
            wrapper.and(w -> w.like("title", keyword)
                    .or().like("description", keyword)
                    .or().like("tags", keyword));
        }
        
        // 分类筛选
        if (categoryId != null) {
            wrapper.eq("category_id", categoryId);
        }
        
        // 排序
        if (StringUtils.isNotBlank(sortType)) {
            switch (sortType) {
                case "time":
                    wrapper.orderByDesc("create_time");
                    break;
                case "deadline":
                    wrapper.orderByAsc("deadline");
                    break;
                case "popular":
                    wrapper.orderByDesc("registered_count");
                    break;
                default:
                    wrapper.orderByDesc("create_time");
            }
        } else {
            wrapper.orderByDesc("create_time");
        }
        
        return page(page, wrapper);
    }

    @Override
    public Activity getActivityDetail(Long id) {
        Activity activity = getById(id);
        if (activity != null) {
            // 增加浏览次数
            incrementViewCount(id);

            // 查询分类信息
            if (activity.getCategoryId() != null) {
                ActivityCategory category = activityCategoryService.getById(activity.getCategoryId());
                if (category != null) {
                    activity.setCategory(category);
                }
            }

            // 查询组织者信息
            if (activity.getOrganizerId() != null) {
                User organizer = userMapper.selectById(activity.getOrganizerId());
                if (organizer != null) {
                    activity.setOrganizer(organizer);
                }
            }
        }
        return activity;
    }
    
    @Override
    @Transactional
    public boolean createActivity(Activity activity) {
        activity.setStatus(1);
        activity.setRegisteredCount(0);
        activity.setViewCount(0);
        activity.setIsFeatured(0);
        return save(activity);
    }
    
    @Override
    @Transactional
    public boolean updateActivity(Activity activity) {
        return updateById(activity);
    }
    
    @Override
    @Transactional
    public boolean deleteActivity(Long id) {
        // 软删除，将状态设为已取消
        Activity activity = getById(id);
        if (activity != null) {
            activity.setStatus(3); // 3-已取消
            return updateById(activity);
        }
        return false;
    }
    
    @Override
    public List<Activity> searchActivities(String keyword) {
        if (StringUtils.isBlank(keyword)) {
            return list();
        }
        return baseMapper.searchActivities(keyword);
    }
    
    @Override
    public List<Activity> getActivitiesByCategory(Long categoryId) {
        return baseMapper.selectByCategoryId(categoryId);
    }
    
    @Override
    public List<Activity> getActivitiesByOrganizer(Long organizerId) {
        return baseMapper.selectByOrganizerId(organizerId);
    }
    
    @Override
    public List<Activity> getFeaturedActivities() {
        return baseMapper.selectFeaturedActivities();
    }
    
    @Override
    public List<Activity> getUpcomingActivities() {
        return baseMapper.selectUpcomingActivities();
    }
    
    @Override
    @Transactional
    public boolean incrementViewCount(Long id) {
        return baseMapper.incrementViewCount(id) > 0;
    }
    
    @Override
    @Transactional
    public boolean updateRegisteredCount(Long id, Integer increment) {
        return baseMapper.updateRegisteredCount(id, increment) > 0;
    }
}
