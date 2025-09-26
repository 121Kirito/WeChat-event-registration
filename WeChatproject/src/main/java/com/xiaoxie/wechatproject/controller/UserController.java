package com.xiaoxie.wechatproject.controller;

import com.xiaoxie.wechatproject.dto.ApiResponse;
import com.xiaoxie.wechatproject.entity.User;
import com.xiaoxie.wechatproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 用户控制器
 */
@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    /**
     * 获取用户信息
     */
    @GetMapping("/{id}")
    public ApiResponse<User> getUserInfo(@PathVariable Long id) {
        try {
            User user = userService.getById(id);
            if (user != null) {
                return ApiResponse.success("查询成功", user);
            } else {
                return ApiResponse.notFound("用户不存在");
            }
        } catch (Exception e) {
            return ApiResponse.error("查询失败: " + e.getMessage());
        }
    }
    
    /**
     * 更新用户信息
     */
    @PutMapping("/{id}")
    public ApiResponse<User> updateUserInfo(@PathVariable Long id, @RequestBody User user) {
        try {
            // 先获取现有用户信息
            User existingUser = userService.getById(id);
            if (existingUser == null) {
                return ApiResponse.notFound("用户不存在");
            }
            
            // 只更新允许修改的字段，保留openid等关键字段
            existingUser.setNickname(user.getNickname());
            existingUser.setAvatarUrl(user.getAvatarUrl());
            existingUser.setPhone(user.getPhone());
            existingUser.setEmail(user.getEmail());
            existingUser.setBio(user.getBio());
            existingUser.setGender(user.getGender());
            existingUser.setCity(user.getCity());
            existingUser.setProvince(user.getProvince());
            existingUser.setCountry(user.getCountry());
            
            boolean result = userService.updateUserInfo(existingUser);
            if (result) {
                return ApiResponse.success("更新成功", existingUser);
            } else {
                return ApiResponse.error("更新失败");
            }
        } catch (Exception e) {
            return ApiResponse.error("更新失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取用户统计数据
     */
    @GetMapping("/{id}/stats")
    public ApiResponse<UserService.UserStats> getUserStats(@PathVariable Long id) {
        try {
            UserService.UserStats stats = userService.getUserStats(id);
            return ApiResponse.success("查询成功", stats);
        } catch (Exception e) {
            return ApiResponse.error("查询失败: " + e.getMessage());
        }
    }
}
