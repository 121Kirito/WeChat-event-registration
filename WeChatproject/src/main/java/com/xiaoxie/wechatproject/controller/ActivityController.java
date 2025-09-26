package com.xiaoxie.wechatproject.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;


import com.xiaoxie.wechatproject.dto.ApiResponse;
import com.xiaoxie.wechatproject.entity.Activity;
import com.xiaoxie.wechatproject.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 活动控制器
 */
@RestController
@RequestMapping("/api/activities")
@CrossOrigin(origins = "*")
public class ActivityController {
    
    @Autowired
    private ActivityService activityService;
    
    /**
     * 分页查询活动列表
     */
    @GetMapping("/page")
    public ApiResponse<IPage<Activity>> getActivityPage(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String sortType) {
        try {
            Page<Activity> page = new Page<>(current, size);
            IPage<Activity> result = activityService.getActivityPage(page, keyword, categoryId, sortType);
            for (Activity activity : result.getRecords()) {
                System.out.println(activity.toString());
            }
            return ApiResponse.success("查询成功", result);
        } catch (Exception e) {
            return ApiResponse.error("查询失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取活动详情
     */
    @GetMapping("/{id}")
    public ApiResponse<Activity> getActivityDetail(@PathVariable Long id) {
        try {
            Activity activity = activityService.getActivityDetail(id);
            if (activity != null) {
                return ApiResponse.success("查询成功", activity);
            } else {
                return ApiResponse.notFound("活动不存在");
            }
        } catch (Exception e) {
            return ApiResponse.error("查询失败: " + e.getMessage());
        }
    }
    
    /**
     * 创建活动
     */
    @PostMapping
    public ApiResponse<Activity> createActivity(@RequestBody Activity activity) {
        try {
            boolean result = activityService.createActivity(activity);
            if (result) {
                return ApiResponse.success("创建成功", activity);
            } else {
                return ApiResponse.error("创建失败");
            }
        } catch (Exception e) {
            return ApiResponse.error("创建失败: " + e.getMessage());
        }
    }
    
    /**
     * 更新活动
     */
    @PutMapping("/{id}")
    public ApiResponse<Activity> updateActivity(@PathVariable Long id, @RequestBody Activity activity) {
        try {
            activity.setId(id);
            boolean result = activityService.updateActivity(activity);
            if (result) {
                return ApiResponse.success("更新成功", activity);
            } else {
                return ApiResponse.error("更新失败");
            }
        } catch (Exception e) {
            return ApiResponse.error("更新失败: " + e.getMessage());
        }
    }
    
    /**
     * 删除活动
     */
    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteActivity(@PathVariable Long id) {
        try {
            boolean result = activityService.deleteActivity(id);
            if (result) {
                return ApiResponse.success("删除成功");
            } else {
                return ApiResponse.error("删除失败");
            }
        } catch (Exception e) {
            return ApiResponse.error("删除失败: " + e.getMessage());
        }
    }
    
    /**
     * 搜索活动
     */
    @GetMapping("/search")
    public ApiResponse<List<Activity>> searchActivities(@RequestParam String keyword) {
        try {
            List<Activity> activities = activityService.searchActivities(keyword);
            return ApiResponse.success("搜索成功", activities);
        } catch (Exception e) {
            return ApiResponse.error("搜索失败: " + e.getMessage());
        }
    }
    
    /**
     * 根据分类查询活动
     */
    @GetMapping("/category/{categoryId}")
    public ApiResponse<List<Activity>> getActivitiesByCategory(@PathVariable Long categoryId) {
        try {
            List<Activity> activities = activityService.getActivitiesByCategory(categoryId);
            return ApiResponse.success("查询成功", activities);
        } catch (Exception e) {
            return ApiResponse.error("查询失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取推荐活动
     */
    @GetMapping("/featured")
    public ApiResponse<List<Activity>> getFeaturedActivities() {
        try {
            List<Activity> activities = activityService.getFeaturedActivities();
            return ApiResponse.success("查询成功", activities);
        } catch (Exception e) {
            return ApiResponse.error("查询失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取即将开始的活动
     */
    @GetMapping("/upcoming")
    public ApiResponse<List<Activity>> getUpcomingActivities() {
        try {
            List<Activity> activities = activityService.getUpcomingActivities();
            return ApiResponse.success("查询成功", activities);
        } catch (Exception e) {
            return ApiResponse.error("查询失败: " + e.getMessage());
        }
    }
}
