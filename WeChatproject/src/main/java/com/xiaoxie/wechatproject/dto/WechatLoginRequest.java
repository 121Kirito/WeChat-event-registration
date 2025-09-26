package com.xiaoxie.wechatproject.dto;

import javax.validation.constraints.NotBlank;

/**
 * 微信登录请求DTO
 */
public class WechatLoginRequest {
    
    @NotBlank(message = "微信code不能为空")
    private String code;
    
    private String encryptedData;
    
    private String iv;
    
    public WechatLoginRequest() {}
    
    public WechatLoginRequest(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return code;
    }
    
    public void setCode(String code) {
        this.code = code;
    }
    
    public String getEncryptedData() {
        return encryptedData;
    }
    
    public void setEncryptedData(String encryptedData) {
        this.encryptedData = encryptedData;
    }
    
    public String getIv() {
        return iv;
    }
    
    public void setIv(String iv) {
        this.iv = iv;
    }
    
    @Override
    public String toString() {
        return "WechatLoginRequest{" +
                "code='" + code + '\'' +
                ", encryptedData='" + encryptedData + '\'' +
                ", iv='" + iv + '\'' +
                '}';
    }
}
