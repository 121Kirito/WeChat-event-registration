package com.xiaoxie.wechatproject.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import javax.validation.constraints.NotBlank;

/**
 * 活动分类实体类
 */
@TableName("activity_category")
public class ActivityCategory extends BaseEntity {
    
    @NotBlank(message = "分类名称不能为空")
    private String name;
    
    private String description;
    
    private String icon;
    
    private Integer sortOrder = 0;
    
    private Integer status = 1; // 0-禁用，1-正常
    
    public ActivityCategory() {}
    
    public ActivityCategory(String name, String description, String icon) {
        this.name = name;
        this.description = description;
        this.icon = icon;
    }
    
    // Getters and Setters
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getIcon() {
        return icon;
    }
    
    public void setIcon(String icon) {
        this.icon = icon;
    }
    
    public Integer getSortOrder() {
        return sortOrder;
    }
    
    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }
    
    public Integer getStatus() {
        return status;
    }
    
    public void setStatus(Integer status) {
        this.status = status;
    }
    
    @Override
    public String toString() {
        return "ActivityCategory{" +
                "id=" + getId() +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", icon='" + icon + '\'' +
                ", sortOrder=" + sortOrder +
                ", status=" + status +
                ", createTime=" + getCreateTime() +
                ", updateTime=" + getUpdateTime() +
                '}';
    }
}
