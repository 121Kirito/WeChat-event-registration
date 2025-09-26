package com.xiaoxie.wechatproject.service;

import com.xiaoxie.wechatproject.dto.WechatLoginRequest;
import com.xiaoxie.wechatproject.entity.User;

/**
 * 微信服务接口
 */
public interface WechatService {
    
    /**
     * 微信登录
     */
    User wechatLogin(WechatLoginRequest request);
    
    /**
     * 根据code获取微信用户信息
     */
    String getOpenidByCode(String code);
    
    /**
     * 解密微信用户信息
     */
    String decryptUserInfo(String encryptedData, String iv, String sessionKey);
}
