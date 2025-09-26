package com.xiaoxie.wechatproject.service.impl;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaUserInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xiaoxie.wechatproject.dto.WechatLoginRequest;
import com.xiaoxie.wechatproject.entity.User;
import com.xiaoxie.wechatproject.service.UserService;
import com.xiaoxie.wechatproject.service.WechatService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 微信服务实现类
 */
@Service
public class WechatServiceImpl implements WechatService {
    
    private static final Logger logger = LoggerFactory.getLogger(WechatServiceImpl.class);
    
    @Autowired
    private WxMaService wxMaService;
    
    @Autowired
    private UserService userService;
    
    @Value("${wechat.miniapp.appid}")
    private String appId;
    
    @Value("${wechat.miniapp.secret}")
    private String secret;
    
    @Override
    public User wechatLogin(WechatLoginRequest request) {
        try {
            // 1. 通过code获取openid和session_key
            String openid = getOpenidByCode(request.getCode());
            if (StringUtils.isBlank(openid)) {
                throw new RuntimeException("获取openid失败");
            }
            
            // 2. 查询用户是否已存在
            User user = userService.findByOpenid(openid);
            if (user != null) {
                return user;
            }
            
            // 3. 如果提供了加密数据，则解密获取用户信息
            if (StringUtils.isNotBlank(request.getEncryptedData()) && StringUtils.isNotBlank(request.getIv())) {
                WxMaJscode2SessionResult sessionResult = wxMaService.getUserService().getSessionInfo(request.getCode());
                String sessionKey = sessionResult.getSessionKey();
                
                WxMaUserInfo userInfo = wxMaService.getUserService().getUserInfo(sessionKey, request.getEncryptedData(), request.getIv());
                
                // 4. 创建新用户
                user = new User();
                user.setOpenid(openid);
                user.setNickname(userInfo.getNickName());
                user.setAvatarUrl(userInfo.getAvatarUrl());
                user.setGender(Integer.valueOf(userInfo.getGender()));
                user.setCity(userInfo.getCity());
                user.setProvince(userInfo.getProvince());
                user.setCountry(userInfo.getCountry());
                
                user = userService.createOrUpdateUser(user);
            } else {
                // 5. 仅通过openid创建用户
                user = new User();
                user.setOpenid(openid);
                user.setNickname("微信用户");
                user.setAvatarUrl("https://i.pravatar.cc/150");
                
                user = userService.createOrUpdateUser(user);
            }
            
            return user;
            
        } catch (Exception e) {
            logger.error("微信登录失败", e);
            throw new RuntimeException("微信登录失败: " + e.getMessage());
        }
    }
    
    @Override
    public String getOpenidByCode(String code) {
        try {
            WxMaJscode2SessionResult sessionResult = wxMaService.getUserService().getSessionInfo(code);
            return sessionResult.getOpenid();
        } catch (Exception e) {
            logger.error("获取openid失败", e);
            return null;
        }
    }
    
    @Override
    public String decryptUserInfo(String encryptedData, String iv, String sessionKey) {
        try {
            WxMaUserInfo userInfo = wxMaService.getUserService().getUserInfo(sessionKey, encryptedData, iv);
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(userInfo);
        } catch (Exception e) {
            logger.error("解密用户信息失败", e);
            return null;
        }
    }
}
