// create-activity.js - 发布活动页面
const api = require('../../utils/api');

Page({
  data: {
    // 表单数据
    formData: {
      title: '',
      description: '',
      category: '',
      startDate: '',
      startTime: '',
      location: '',
      deadline: '',
      maxParticipants: '',
      fee: '0',
      requirements: '',
      schedule: '',
      rewards: '',
      contact: '',
      images: [],
      tags: []
    },
    
    // 分类选择
    categories: [
      { id: 1, name: '体育运动' },
      { id: 2, name: '学术竞赛' },
      { id: 3, name: '文艺活动' },
      { id: 4, name: '志愿服务' },
      { id: 5, name: '社会实践' },
      { id: 6, name: '创新创业' },
      { id: 7, name: '其他' }
    ],
    categoryIndex: -1,
    
    // 可用标签
    availableTags: [
      '篮球', '足球', '乒乓球', '羽毛球', '游泳', '跑步',
      '编程', '设计', '演讲', '辩论', '写作', '数学',
      '音乐', '舞蹈', '绘画', '摄影', '书法', '戏剧',
      '志愿者', '公益', '环保', '支教', '社区服务',
      '实习', '调研', '创业', '创新', '比赛', '展览'
    ],
    
    // 弹窗状态
    showPreviewModal: false
  },

  onLoad() {
    // 设置默认截止时间为活动开始前一天
    const tomorrow = new Date();
    tomorrow.setDate(tomorrow.getDate() + 1);
    const defaultDeadline = this.formatDate(tomorrow);
    
    this.setData({
      'formData.deadline': defaultDeadline
    });
  },

  // 格式化日期
  formatDate(date) {
    const year = date.getFullYear();
    const month = (date.getMonth() + 1).toString().padStart(2, '0');
    const day = date.getDate().toString().padStart(2, '0');
    return `${year}-${month}-${day}`;
  },

  // 输入活动标题
  onTitleInput(e) {
    this.setData({
      'formData.title': e.detail.value
    });
  },

  // 输入活动简介
  onDescriptionInput(e) {
    this.setData({
      'formData.description': e.detail.value
    });
  },

  // 选择分类
  onCategoryChange(e) {
    const index = parseInt(e.detail.value);
    const cat = this.data.categories[index];
    if (!cat) {
      wx.showToast({ title: '分类无效', icon: 'none' });
      return;
    }
    this.setData({
      categoryIndex: index,
      'formData.category': cat.name
    });
  },

  // 选择开始日期
  onStartDateChange(e) {
    this.setData({
      'formData.startDate': e.detail.value
    });
  },

  // 选择开始时间
  onStartTimeChange(e) {
    this.setData({
      'formData.startTime': e.detail.value
    });
  },

  // 输入活动地点
  onLocationInput(e) {
    this.setData({
      'formData.location': e.detail.value
    });
  },

  // 选择截止日期
  onDeadlineChange(e) {
    this.setData({
      'formData.deadline': e.detail.value
    });
  },

  // 输入最大参与人数
  onMaxParticipantsInput(e) {
    this.setData({
      'formData.maxParticipants': e.detail.value
    });
  },

  // 输入活动费用
  onFeeInput(e) {
    this.setData({
      'formData.fee': e.detail.value
    });
  },

  // 输入参与要求
  onRequirementsInput(e) {
    this.setData({
      'formData.requirements': e.detail.value
    });
  },

  // 输入活动安排
  onScheduleInput(e) {
    this.setData({
      'formData.schedule': e.detail.value
    });
  },

  // 输入奖励说明
  onRewardsInput(e) {
    this.setData({
      'formData.rewards': e.detail.value
    });
  },

  // 输入联系方式
  onContactInput(e) {
    this.setData({
      'formData.contact': e.detail.value
    });
  },

  // 选择图片
  onChooseImage() {
    const maxCount = 5 - this.data.formData.images.length;
    
    wx.chooseMedia({
      count: maxCount,
      mediaType: ['image'],
      sourceType: ['album', 'camera'],
      success: (res) => {
        const newImages = res.tempFiles.map(file => file.tempFilePath);
        this.setData({
          'formData.images': [...this.data.formData.images, ...newImages]
        });
      },
      fail: (err) => {
        console.error('选择图片失败:', err);
        wx.showToast({
          title: '选择图片失败',
          icon: 'none'
        });
      }
    });
  },

  // 删除图片
  onDeleteImage(e) {
    const index = e.currentTarget.dataset.index;
    const images = this.data.formData.images;
    images.splice(index, 1);
    this.setData({
      'formData.images': images
    });
  },

  // 切换标签选择
  onToggleTag(e) {
    const tag = e.currentTarget.dataset.tag;
    const tags = this.data.formData.tags;
    
    if (tags.includes(tag)) {
      // 移除标签
      const index = tags.indexOf(tag);
      tags.splice(index, 1);
    } else {
      // 添加标签（最多5个）
      if (tags.length < 5) {
        tags.push(tag);
      } else {
        wx.showToast({
          title: '最多选择5个标签',
          icon: 'none'
        });
        return;
      }
    }
    
    this.setData({
      'formData.tags': tags
    });
  },

  // 预览活动
  onPreview() {
    if (!this.validateForm()) {
      return;
    }
    
    this.setData({
      showPreviewModal: true
    });
  },

  // 关闭预览
  onClosePreview() {
    this.setData({
      showPreviewModal: false
    });
  },

  // 发布活动
  onPublish() {
    if (!this.validateForm()) {
      return;
    }
    
    wx.showModal({
      title: '确认发布',
      content: '确定要发布这个活动吗？',
      success: (res) => {
        if (res.confirm) {
          this.submitActivity();
        }
      }
    });
  },

  // 确认发布
  onConfirmPublish() {
    this.setData({
      showPreviewModal: false
    });
    this.submitActivity();
  },

  // 提交活动
  submitActivity() {
    wx.showLoading({ title: '发布中...' });

    const { formData, categoryIndex, categories } = this.data;
    const userId = wx.getStorageSync('userId');
    if (!userId) {
      wx.hideLoading();
      wx.showToast({ title: '请先登录', icon: 'none' });
      return;
    }
    const category = categories[categoryIndex];
    const payload = {
      title: formData.title.trim(),
      description: formData.description.trim(),
      categoryId: category ? category.id : null,
      organizerId: userId,
      startDate: formData.startDate,
      startTime: formData.startTime + ':00',
      endDate: null,
      endTime: null,
      location: formData.location.trim(),
      maxParticipants: parseInt(formData.maxParticipants, 10),
      registeredCount: 0,
      deadline: (formData.deadline ? (formData.deadline + ' 23:59:59') : ''),
      fee: parseFloat(formData.fee || '0'),
      requirements: formData.requirements,
      schedule: formData.schedule,
      rewards: formData.rewards,
      contact: formData.contact,
      images: formData.images || [],
      tags: formData.tags || []
    };

    api.activityApi.createActivity(payload)
      .then(res => {
        wx.hideLoading();
        if (res.code === 200) {
          wx.showToast({ title: '发布成功！', icon: 'success' });
          setTimeout(() => {
            wx.switchTab({ url: '/pages/index/index' });
          }, 1200);
        } else {
          throw new Error(res.message || '发布失败');
        }
      })
      .catch(err => {
        wx.hideLoading();
        console.error('发布失败:', err);
        wx.showToast({ title: err.message || '发布失败', icon: 'none' });
      });
  },

  // 表单验证
  validateForm() {
    const { formData } = this.data;
    
    if (!formData.title.trim()) {
      wx.showToast({
        title: '请输入活动标题',
        icon: 'none'
      });
      return false;
    }
    
    if (!formData.description.trim()) {
      wx.showToast({
        title: '请输入活动简介',
        icon: 'none'
      });
      return false;
    }
    
    if (this.data.categoryIndex === -1) {
      wx.showToast({
        title: '请选择活动分类',
        icon: 'none'
      });
      return false;
    }
    
    if (!formData.startDate) {
      wx.showToast({
        title: '请选择活动日期',
        icon: 'none'
      });
      return false;
    }
    
    if (!formData.startTime) {
      wx.showToast({
        title: '请选择活动时间',
        icon: 'none'
      });
      return false;
    }
    
    if (!formData.location.trim()) {
      wx.showToast({
        title: '请输入活动地点',
        icon: 'none'
      });
      return false;
    }
    
    if (!formData.deadline) {
      wx.showToast({
        title: '请选择报名截止时间',
        icon: 'none'
      });
      return false;
    }
    
    if (!formData.maxParticipants || parseInt(formData.maxParticipants) <= 0) {
      wx.showToast({
        title: '请输入正确的参与人数',
        icon: 'none'
      });
      return false;
    }
    
    // 验证截止时间不能晚于活动开始时间
    const startDateTime = new Date(`${formData.startDate} ${formData.startTime}`);
    const deadlineDate = new Date(formData.deadline);
    
    if (deadlineDate >= startDateTime) {
      wx.showToast({
        title: '报名截止时间应早于活动开始时间',
        icon: 'none'
      });
      return false;
    }
    
    return true;
  },

  // 图片错误处理
  onImageError(e) {
    console.log('图片加载失败:', e);
    const idx = e.currentTarget.dataset.index;
    if (idx !== undefined) {
      // 替换失败的图片
      const images = this.data.formData.images;
      if (images[idx]) {
        images[idx] = 'https://picsum.photos/750/400?blur=3';
        this.setData({ 'formData.images': images });
      }
    } else {
      // 可以设置默认图片或提示
      wx.showToast({
        title: '图片加载失败',
        icon: 'none'
      });
    }
  },

  // 阻止事件冒泡
  stopPropagation() {
    // 阻止点击弹窗内容时关闭弹窗
  }
})