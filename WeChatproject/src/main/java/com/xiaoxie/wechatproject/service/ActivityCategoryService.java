package com.xiaoxie.wechatproject.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaoxie.wechatproject.entity.ActivityCategory;

import java.util.List;

/**
 * 活动分类服务接口
 */
public interface ActivityCategoryService extends IService<ActivityCategory> {
    
    /**
     * 获取所有分类
     */
    List<ActivityCategory> getAllCategories();
    
    /**
     * 根据ID获取分类
     */
    ActivityCategory getCategoryById(Long id);
}
