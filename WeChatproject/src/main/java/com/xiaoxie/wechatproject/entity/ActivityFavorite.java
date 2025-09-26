package com.xiaoxie.wechatproject.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import javax.validation.constraints.NotNull;

/**
 * 活动收藏实体类
 */
@TableName("activity_favorite")
public class ActivityFavorite extends BaseEntity {
    
    @NotNull(message = "活动ID不能为空")
    private Long activityId;
    
    @NotNull(message = "用户ID不能为空")
    private Long userId;
    
    // 关联对象（非数据库字段）
    @TableField(exist = false)
    private Activity activity;
    @TableField(exist = false)
    private User user;
    
    public ActivityFavorite() {}
    
    public ActivityFavorite(Long activityId, Long userId) {
        this.activityId = activityId;
        this.userId = userId;
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
    
    @Override
    public String toString() {
        return "ActivityFavorite{" +
                "id=" + getId() +
                ", activityId=" + activityId +
                ", userId=" + userId +
                ", createTime=" + getCreateTime() +
                '}';
    }
}
