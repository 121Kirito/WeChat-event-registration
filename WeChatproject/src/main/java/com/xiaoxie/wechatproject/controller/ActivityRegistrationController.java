package com.xiaoxie.wechatproject.controller;

import com.xiaoxie.wechatproject.dto.ApiResponse;
import com.xiaoxie.wechatproject.entity.ActivityRegistration;
import com.xiaoxie.wechatproject.mapper.ActivityRegistrationMapper;
import com.xiaoxie.wechatproject.service.ActivityRegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 活动报名控制器
 */
@RestController
@RequestMapping("/api/registrations")
@CrossOrigin(origins = "*")
public class ActivityRegistrationController {
    
    @Autowired
    private ActivityRegistrationService activityRegistrationService;
    @Autowired
    private ActivityRegistrationMapper activityRegistrationMapper;
    /**
     * 报名活动
     */
    public static class RegistrationRequest {
        public Long activityId;
        public Long userId;
        public String name;
        public String phone;
        public String remark;
    }

    @PostMapping
    public ApiResponse<String> registerActivity(@RequestBody RegistrationRequest req) {
        try {
            ActivityRegistration activityRegistration = activityRegistrationMapper.selectActivity_user(Math.toIntExact(req.activityId), Math.toIntExact(req.userId));
            if (activityRegistration != null) {
                activityRegistrationMapper.updateRegistration(Math.toIntExact(req.activityId), Math.toIntExact(req.userId));
                return ApiResponse.success("报名成功");
            }
            boolean result = activityRegistrationService.registerActivity(req.activityId, req.userId, req.name, req.phone, req.remark);

            if (result) {
                return ApiResponse.success("报名成功");
            } else {
                return ApiResponse.error("报名失败");
            }
        } catch (Exception e) {
            return ApiResponse.error("报名失败: " + "请勿重复报名");
        }
    }
    
    /**
     * 取消报名
     */
    public static class CancelRequest {
        public Long activityId;
        public Long userId;
    }

    @DeleteMapping
    public ApiResponse<String> cancelRegistration(@RequestBody CancelRequest req) {
        try {
            boolean result = activityRegistrationService.cancelRegistration(req.activityId, req.userId);
            if (result) {
                return ApiResponse.success("取消报名成功");
            } else {
                return ApiResponse.error("取消报名失败");
            }
        } catch (Exception e) {
            return ApiResponse.error("取消报名失败: " + e.getMessage());
        }
    }
    
    /**
     * 检查用户是否已报名
     */
    @GetMapping("/check")
    public ApiResponse<Boolean> checkRegistration(
            @RequestParam @NotNull Long activityId,
            @RequestParam @NotNull Long userId) {
        try {
            boolean isRegistered = activityRegistrationService.isUserRegistered(activityId, userId);
            return ApiResponse.success("查询成功", isRegistered);
        } catch (Exception e) {
            return ApiResponse.error("查询失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取用户报名记录
     */
    @GetMapping("/user/{userId}")
    public ApiResponse<List<ActivityRegistration>> getUserRegistrations(@PathVariable Long userId) {
        try {
            List<ActivityRegistration> registrations = activityRegistrationService.getUserRegistrations(userId);
            return ApiResponse.success("查询成功", registrations);
        } catch (Exception e) {
            return ApiResponse.error("查询失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取用户即将参加的活动
     */
    @GetMapping("/user/{userId}/upcoming")
    public ApiResponse<List<ActivityRegistration>> getUserUpcomingActivities(@PathVariable Long userId) {
        try {
            List<ActivityRegistration> registrations = activityRegistrationService.getUserUpcomingActivities(userId);
            System.out.println(registrations);
            return ApiResponse.success("查询成功", registrations);

        } catch (Exception e) {
            return ApiResponse.error("查询失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取用户已完成的活动
     */
    @GetMapping("/user/{userId}/completed")
    public ApiResponse<List<ActivityRegistration>> getUserCompletedActivities(@PathVariable Long userId) {
        try {
            List<ActivityRegistration> registrations = activityRegistrationService.getUserCompletedActivities(userId);
            return ApiResponse.success("查询成功", registrations);
        } catch (Exception e) {
            return ApiResponse.error("查询失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取活动报名者列表
     */
    @GetMapping("/activity/{activityId}")
    public ApiResponse<List<ActivityRegistration>> getActivityParticipants(@PathVariable Long activityId) {
        try {
            List<ActivityRegistration> participants = activityRegistrationService.getActivityParticipants(activityId);
            return ApiResponse.success("查询成功", participants);
        } catch (Exception e) {
            return ApiResponse.error("查询失败: " + e.getMessage());
        }
    }
    
    /**
     * 签到
     */
    @PostMapping("/checkin")
    public ApiResponse<String> checkIn(
            @RequestParam @NotNull Long activityId,
            @RequestParam @NotNull Long userId) {
        try {
            boolean result = activityRegistrationService.checkIn(activityId, userId);
            if (result) {
                return ApiResponse.success("签到成功");
            } else {
                return ApiResponse.error("签到失败");
            }
        } catch (Exception e) {
            return ApiResponse.error("签到失败: " + e.getMessage());
        }
    }
}
