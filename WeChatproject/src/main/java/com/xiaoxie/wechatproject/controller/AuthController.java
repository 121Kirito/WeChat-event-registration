package com.xiaoxie.wechatproject.controller;

import com.xiaoxie.wechatproject.dto.ApiResponse;
import com.xiaoxie.wechatproject.dto.WechatLoginRequest;
import com.xiaoxie.wechatproject.entity.User;
import com.xiaoxie.wechatproject.service.WechatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 认证控制器
 */
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {
    
    @Autowired
    private WechatService wechatService;
    
    /**
     * 微信登录
     */
    @PostMapping("/wechat/login")
    public ApiResponse<User> wechatLogin(@Valid @RequestBody WechatLoginRequest request) {
        try {
            User user = wechatService.wechatLogin(request);
            return ApiResponse.success("登录成功", user);
        } catch (Exception e) {
            return ApiResponse.error("登录失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取微信openid
     */
    @PostMapping("/wechat/openid")
    public ApiResponse<String> getOpenid(@RequestParam String code) {
        try {
            String openid = wechatService.getOpenidByCode(code);
            if (openid != null) {
                return ApiResponse.success("获取成功", openid);
            } else {
                return ApiResponse.error("获取openid失败");
            }
        } catch (Exception e) {
            return ApiResponse.error("获取openid失败: " + e.getMessage());
        }
    }
}
