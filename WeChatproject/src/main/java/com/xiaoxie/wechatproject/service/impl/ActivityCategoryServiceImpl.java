package com.xiaoxie.wechatproject.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaoxie.wechatproject.entity.ActivityCategory;
import com.xiaoxie.wechatproject.mapper.ActivityCategoryMapper;
import com.xiaoxie.wechatproject.service.ActivityCategoryService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 活动分类服务实现类
 */
@Service
public class ActivityCategoryServiceImpl extends ServiceImpl<ActivityCategoryMapper, ActivityCategory> implements ActivityCategoryService {
    
    @Override
    public List<ActivityCategory> getAllCategories() {
        return list();
    }
    
    @Override
    public ActivityCategory getCategoryById(Long id) {
        return getById(id);
    }
}
