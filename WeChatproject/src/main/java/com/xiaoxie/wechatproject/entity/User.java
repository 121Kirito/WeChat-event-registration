package com.xiaoxie.wechatproject.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

/**
 * 用户实体类
 */
@TableName("user")
public class User extends BaseEntity {
    
    private String openid;
    
    private String unionid;
    
    private String nickname;
    
    private String avatarUrl;
    
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;
    
    @Email(message = "邮箱格式不正确")
    private String email;
    
    private String bio;
    
    private Integer gender; // 0-未知，1-男，2-女
    
    private String city;
    
    private String province;
    
    private String country;
    
    private Integer level = 1; // 用户等级
    
    private Integer status = 1; // 0-禁用，1-正常
    
    public User() {}
    
    public User(String openid, String nickname, String avatarUrl) {
        this.openid = openid;
        this.nickname = nickname;
        this.avatarUrl = avatarUrl;
    }
    
    // Getters and Setters
    public String getOpenid() {
        return openid;
    }
    
    public void setOpenid(String openid) {
        this.openid = openid;
    }
    
    public String getUnionid() {
        return unionid;
    }
    
    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }
    
    public String getNickname() {
        return nickname;
    }
    
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    
    public String getAvatarUrl() {
        return avatarUrl;
    }
    
    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getBio() {
        return bio;
    }
    
    public void setBio(String bio) {
        this.bio = bio;
    }
    
    public Integer getGender() {
        return gender;
    }
    
    public void setGender(Integer gender) {
        this.gender = gender;
    }
    
    public String getCity() {
        return city;
    }
    
    public void setCity(String city) {
        this.city = city;
    }
    
    public String getProvince() {
        return province;
    }
    
    public void setProvince(String province) {
        this.province = province;
    }
    
    public String getCountry() {
        return country;
    }
    
    public void setCountry(String country) {
        this.country = country;
    }
    
    public Integer getLevel() {
        return level;
    }
    
    public void setLevel(Integer level) {
        this.level = level;
    }
    
    public Integer getStatus() {
        return status;
    }
    
    public void setStatus(Integer status) {
        this.status = status;
    }
    
    @Override
    public String toString() {
        return "User{" +
                "id=" + getId() +
                ", openid='" + openid + '\'' +
                ", unionid='" + unionid + '\'' +
                ", nickname='" + nickname + '\'' +
                ", avatarUrl='" + avatarUrl + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", bio='" + bio + '\'' +
                ", gender=" + gender +
                ", city='" + city + '\'' +
                ", province='" + province + '\'' +
                ", country='" + country + '\'' +
                ", level=" + level +
                ", status=" + status +
                ", createTime=" + getCreateTime() +
                ", updateTime=" + getUpdateTime() +
                '}';
    }
}
