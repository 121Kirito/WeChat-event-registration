package com.xiaoxie.wechatproject.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

/**
 * 活动实体类
 */
@TableName(value = "activity", autoResultMap = true)
public class Activity extends BaseEntity {
    
    @NotBlank(message = "活动标题不能为空")
    @Size(max = 100, message = "活动标题长度不能超过100个字符")
    private String title;
    
    @NotBlank(message = "活动描述不能为空")
    private String description;
    
    @NotNull(message = "活动分类不能为空")
    private Long categoryId;

    @NotNull(message = "组织者不能为空")
    private Long organizerId;
    
    @NotNull(message = "活动开始日期不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    
    @NotNull(message = "活动开始时间不能为空")
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime startTime;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;
    
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime endTime;
    
    @NotBlank(message = "活动地点不能为空")
    @Size(max = 200, message = "活动地点长度不能超过200个字符")
    private String location;
    
    @NotNull(message = "最大参与人数不能为空")
    @Min(value = 1, message = "最大参与人数必须大于0")
    private Integer maxParticipants;
    
    private Integer registeredCount = 0;
    
    @NotNull(message = "报名截止时间不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime deadline;
    
    @DecimalMin(value = "0.0", message = "活动费用不能为负数")
    private BigDecimal fee = BigDecimal.ZERO;
    
    private String requirements;
    
    private String schedule;
    
    private String rewards;
    
    private String contact;
    
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> images; // JSON数组存储图片URL
    
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> tags; // JSON数组存储标签
    
    private Integer status = 1; // 0-草稿，1-发布，2-已结束，3-已取消
    
    private Integer isFeatured = 0; // 0-否，1-是
    
    private Integer viewCount = 0;
    
    // 关联对象（非数据库字段）
    @TableField(exist = false)
    private ActivityCategory category;
    @TableField(exist = false)
    private User organizer;
    
    public Activity() {}
    
    // Getters and Setters
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
    
    public Long getCategoryId() {
        return categoryId;
    }
    
    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }
    
    public Long getOrganizerId() {
        return organizerId;
    }
    
    public void setOrganizerId(Long organizerId) {
        this.organizerId = organizerId;
    }
    
    public LocalDate getStartDate() {
        return startDate;
    }
    
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }
    
    public LocalTime getStartTime() {
        return startTime;
    }
    
    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }
    
    public LocalDate getEndDate() {
        return endDate;
    }
    
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
    
    public LocalTime getEndTime() {
        return endTime;
    }
    
    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }
    
    public String getLocation() {
        return location;
    }
    
    public void setLocation(String location) {
        this.location = location;
    }
    
    public Integer getMaxParticipants() {
        return maxParticipants;
    }
    
    public void setMaxParticipants(Integer maxParticipants) {
        this.maxParticipants = maxParticipants;
    }
    
    public Integer getRegisteredCount() {
        return registeredCount;
    }
    
    public void setRegisteredCount(Integer registeredCount) {
        this.registeredCount = registeredCount;
    }
    
    public LocalDateTime getDeadline() {
        return deadline;
    }
    
    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }
    
    public BigDecimal getFee() {
        return fee;
    }
    
    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }
    
    public String getRequirements() {
        return requirements;
    }
    
    public void setRequirements(String requirements) {
        this.requirements = requirements;
    }
    
    public String getSchedule() {
        return schedule;
    }
    
    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }
    
    public String getRewards() {
        return rewards;
    }
    
    public void setRewards(String rewards) {
        this.rewards = rewards;
    }
    
    public String getContact() {
        return contact;
    }
    
    public void setContact(String contact) {
        this.contact = contact;
    }
    
    public List<String> getImages() {
        return images;
    }
    
    public void setImages(List<String> images) {
        this.images = images;
    }
    
    public List<String> getTags() {
        return tags;
    }
    
    public void setTags(List<String> tags) {
        this.tags = tags;
    }
    
    public Integer getStatus() {
        return status;
    }
    
    public void setStatus(Integer status) {
        this.status = status;
    }
    
    public Integer getIsFeatured() {
        return isFeatured;
    }
    
    public void setIsFeatured(Integer isFeatured) {
        this.isFeatured = isFeatured;
    }
    
    public Integer getViewCount() {
        return viewCount;
    }
    
    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }
    
    public ActivityCategory getCategory() {
        return category;
    }
    
    public void setCategory(ActivityCategory category) {
        this.category = category;
    }
    
    public User getOrganizer() {
        return organizer;
    }
    
    public void setOrganizer(User organizer) {
        this.organizer = organizer;
    }
    
    @Override
    public String toString() {
        return "Activity{" +
                "id=" + getId() +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", categoryId=" + categoryId +
                ", organizerId=" + organizerId +
                ", startDate=" + startDate +
                ", startTime=" + startTime +
                ", endDate=" + endDate +
                ", endTime=" + endTime +
                ", location='" + location + '\'' +
                ", maxParticipants=" + maxParticipants +
                ", registeredCount=" + registeredCount +
                ", deadline=" + deadline +
                ", fee=" + fee +
                ", requirements='" + requirements + '\'' +
                ", schedule='" + schedule + '\'' +
                ", rewards='" + rewards + '\'' +
                ", contact='" + contact + '\'' +
                ", images=" + images +
                ", tags=" + tags +
                ", status=" + status +
                ", isFeatured=" + isFeatured +
                ", viewCount=" + viewCount +
                ", createTime=" + getCreateTime() +
                ", updateTime=" + getUpdateTime() +
                '}';
    }
}
