package com.xiaoxie.wechatproject.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

/**
 * 活动报名实体类
 */
@TableName("activity_registration")
public class ActivityRegistration extends BaseEntity {
    
    @NotNull(message = "活动ID不能为空")
    private Long activityId;
    
    @NotNull(message = "用户ID不能为空")
    private Long userId;
    
    @NotBlank(message = "报名姓名不能为空")
    private String name;
    
    @NotBlank(message = "报名手机号不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;
    
    private String remark;
    
    private Integer status = 1; // 0-已取消，1-已报名，2-已签到
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime registerTime;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime checkinTime;
    
    // 关联对象（非数据库字段）
    @TableField(exist = false)
    private Activity activity;
    @TableField(exist = false)
    private User user;
    
    // 查询结果映射字段（非数据库字段）
    @TableField(exist = false)
    private String title;
    @TableField(exist = false)
    private String description;
    @TableField(exist = false)
    private String startDate;
    @TableField(exist = false)
    private String startTime;
    @TableField(exist = false)
    private String location;
    @TableField(exist = false)
    private String images;
    @TableField(exist = false)
    private String categoryName;
    
    public ActivityRegistration() {}
    
    public ActivityRegistration(Long activityId, Long userId, String name, String phone) {
        this.activityId = activityId;
        this.userId = userId;
        this.name = name;
        this.phone = phone;
        this.registerTime = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getActivityId() {
        return activityId;
    }
    
    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public String getRemark() {
        return remark;
    }
    
    public void setRemark(String remark) {
        this.remark = remark;
    }
    
    public Integer getStatus() {
        return status;
    }
    
    public void setStatus(Integer status) {
        this.status = status;
    }
    
    public LocalDateTime getRegisterTime() {
        return registerTime;
    }
    
    public void setRegisterTime(LocalDateTime registerTime) {
        this.registerTime = registerTime;
    }
    
    public LocalDateTime getCheckinTime() {
        return checkinTime;
    }
    
    public void setCheckinTime(LocalDateTime checkinTime) {
        this.checkinTime = checkinTime;
    }
    
    public Activity getActivity() {
        return activity;
    }
    
    public void setActivity(Activity activity) {
        this.activity = activity;
    }
    
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    
    // 查询结果映射字段的getter和setter
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getStartDate() {
        return startDate;
    }
    
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }
    
    public String getStartTime() {
        return startTime;
    }
    
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
    
    public String getLocation() {
        return location;
    }
    
    public void setLocation(String location) {
        this.location = location;
    }
    
    public String getImages() {
        return images;
    }
    
    public void setImages(String images) {
        this.images = images;
    }
    
    public String getCategoryName() {
        return categoryName;
    }
    
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
    
    @Override
    public String toString() {
        return "ActivityRegistration{" +
                "id=" + getId() +
                ", activityId=" + activityId +
                ", userId=" + userId +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", remark='" + remark + '\'' +
                ", status=" + status +
                ", registerTime=" + registerTime +
                ", checkinTime=" + checkinTime +
                ", createTime=" + getCreateTime() +
                ", updateTime=" + getUpdateTime() +
                '}';
    }
}
