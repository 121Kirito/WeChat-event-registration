package com.xiaoxie.wechatproject.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaoxie.wechatproject.entity.Activity;

import java.util.List;

/**
 * 活动服务接口
 */
public interface ActivityService extends IService<Activity> {
    
    /**
     * 分页查询活动列表
     */
    IPage<Activity> getActivityPage(Page<Activity> page, String keyword, Long categoryId, String sortType);
    
    /**
     * 根据ID查询活动详情
     */
    Activity getActivityDetail(Long id);
    
    /**
     * 创建活动
     */
    boolean createActivity(Activity activity);
    
    /**
     * 更新活动
     */
    boolean updateActivity(Activity activity);
    
    /**
     * 删除活动
     */
    boolean deleteActivity(Long id);
    
    /**
     * 搜索活动
     */
    List<Activity> searchActivities(String keyword);
    
    /**
     * 根据分类查询活动
     */
    List<Activity> getActivitiesByCategory(Long categoryId);
    
    /**
     * 查询用户发布的活动
     */
    List<Activity> getActivitiesByOrganizer(Long organizerId);
    
    /**
     * 查询推荐活动
     */
    List<Activity> getFeaturedActivities();
    
    /**
     * 查询即将开始的活动
     */
    List<Activity> getUpcomingActivities();
    
    /**
     * 增加浏览次数
     */
    boolean incrementViewCount(Long id);
    
    /**
     * 更新报名人数
     */
    boolean updateRegisteredCount(Long id, Integer increment);
}
