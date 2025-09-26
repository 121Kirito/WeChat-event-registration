// profile.js - 个人中心页面
Page({
  data: {
    userInfo: {
      id: '12345',
      name: '张三',
      avatar: 'https://i.pravatar.cc/150?img=0',
      phone: '13800138000',
      email: 'zhangsan@example.com',
      bio: '热爱运动，喜欢参加各种校园活动',
      joinTime: '2023-09-01'
    },
    userStats: {
      totalActivities: 12,
      completedActivities: 8,
      upcomingActivities: 3,
      favoriteActivities: 5,
      level: 3
    },
    showEditModal: false,
    editUserInfo: {}
  },

  onLoad() {
    this.loadUserInfo();
    this.loadUserStats();
  },

  onShow() {
    // 每次显示页面时刷新数据
    this.loadUserInfo();
    this.loadUserStats();
  },

  // 加载用户信息
  loadUserInfo() {
    const userId = wx.getStorageSync('userId');
    console.log('开始加载用户信息，用户ID:', userId);
    if (!userId) {
      // 未登录，使用默认信息
      this.setData({ userInfo: this.data.userInfo });
      return;
    }

    // 调用API获取用户信息
    const api = require('../../utils/api');
    api.userApi.getUserInfo(userId)
      .then(res => {
        console.log('用户信息API响应:', res);
        if (res.code === 200) {
          const userInfo = {
            id: res.data.id || userId,
            name: res.data.name || res.data.nickname || res.data.username || '微信用户',
            avatar: res.data.avatar || res.data.headimgurl || res.data.avatarUrl || 'https://picsum.photos/150?random=avatar',
            phone: res.data.phone || res.data.mobile || '未设置手机号',
            email: res.data.email || res.data.emailAddress || '未设置邮箱',
            bio: res.data.bio || res.data.introduction || res.data.description || '这个人很懒，什么都没有留下',
            joinTime: res.data.createTime ? res.data.createTime.split(' ')[0] : (res.data.registerTime ? res.data.registerTime.split(' ')[0] : '2023-09-01')
          };
          console.log('处理后的用户信息:', userInfo);
          this.setData({ userInfo });
          wx.setStorageSync('userInfo', userInfo);
        } else {
          console.error('API返回错误:', res);
          // API失败，使用本地存储或默认信息
          const userInfo = wx.getStorageSync('userInfo') || this.data.userInfo;
          this.setData({ userInfo });
        }
      })
      .catch(err => {
        console.error('加载用户信息失败:', err);
        // 失败时使用本地存储或默认信息
        const userInfo = wx.getStorageSync('userInfo') || this.data.userInfo;
        this.setData({ userInfo });
      });
  },

  // 加载用户统计数据
  loadUserStats() {
    const userId = wx.getStorageSync('userId');
    if (!userId) {
      // 未登录，使用默认统计
      const mockStats = {
        totalActivities: 0,
        completedActivities: 0,
        upcomingActivities: 0,
        favoriteActivities: 0,
        level: 1
      };
      this.setData({ userStats: mockStats });
      return;
    }

    // 调用API获取统计数据
    const api = require('../../utils/api');
    api.userApi.getUserStats(userId)
      .then(res => {
        console.log('用户统计数据API响应:', res);
        if (res.code === 200) {
          this.setData({ userStats: res.data });
        } else {
          // API失败，使用默认统计
          const mockStats = {
            totalActivities: 0,
            completedActivities: 0,
            upcomingActivities: 0,
            favoriteActivities: 0,
            level: 1
          };
          this.setData({ userStats: mockStats });
        }
      })
      .catch(err => {
        console.error('加载用户统计数据失败:', err);
        // 失败时使用默认统计
        const mockStats = {
          totalActivities: 0,
          completedActivities: 0,
          upcomingActivities: 0,
          favoriteActivities: 0,
          level: 1
        };
        this.setData({ userStats: mockStats });
      });
  },

  // 编辑资料
  onEditProfile() {
    this.setData({
      showEditModal: true,
      editUserInfo: { ...this.data.userInfo }
    });
  },

  // 关闭编辑弹窗
  onCloseEditModal() {
    this.setData({
      showEditModal: false,
      editUserInfo: {}
    });
  },

  // 选择头像
  onChooseAvatar() {
    wx.chooseMedia({
      count: 1,
      mediaType: ['image'],
      sourceType: ['album', 'camera'],
      success: (res) => {
        const tempFilePath = res.tempFiles[0].tempFilePath;
        this.setData({
          'editUserInfo.avatar': tempFilePath
        });
      },
      fail: (err) => {
        console.error('选择头像失败:', err);
        wx.showToast({
          title: '选择头像失败',
          icon: 'none'
        });
      }
    });
  },

  // 输入昵称
  onNameInput(e) {
    this.setData({
      'editUserInfo.name': e.detail.value
    });
  },

  // 输入手机号
  onPhoneInput(e) {
    this.setData({
      'editUserInfo.phone': e.detail.value
    });
  },

  // 输入邮箱
  onEmailInput(e) {
    this.setData({
      'editUserInfo.email': e.detail.value
    });
  },

  // 输入个人简介
  onBioInput(e) {
    this.setData({
      'editUserInfo.bio': e.detail.value
    });
  },

  // 保存资料
  onSaveProfile() {
    const { name, phone, email } = this.data.editUserInfo;
    
    // 验证必填信息
    if (!name || !name.trim()) {
      wx.showToast({
        title: '请输入昵称',
        icon: 'none'
      });
      return;
    }
    
    if (!phone || !phone.trim()) {
      wx.showToast({
        title: '请输入手机号',
        icon: 'none'
      });
      return;
    }
    
    // 验证手机号格式
    const phoneRegex = /^1[3-9]\d{9}$/;
    if (!phoneRegex.test(phone)) {
      wx.showToast({
        title: '请输入正确的手机号',
        icon: 'none'
      });
      return;
    }
    
    // 验证邮箱格式
    if (email && !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)) {
      wx.showToast({
        title: '请输入正确的邮箱',
        icon: 'none'
      });
      return;
    }
    
    wx.showLoading({
      title: '保存中...'
    });
    
    const userId = wx.getStorageSync('userId');
    if (!userId) {
      wx.hideLoading();
      wx.showToast({ title: '请先登录', icon: 'none' });
      return;
    }

    // 构建更新数据，确保字段名称匹配后端
    const payload = {
      nickname: this.data.editUserInfo.name,
      avatarUrl: this.data.editUserInfo.avatar,
      phone: this.data.editUserInfo.phone,
      email: this.data.editUserInfo.email,
      bio: this.data.editUserInfo.bio
    };
    
    const apiModule = require('../../utils/api');
    apiModule.userApi.updateUserInfo(userId, payload)
      .then(res => {
        wx.hideLoading();
        if (res.code === 200) {
          this.setData({ userInfo: { ...this.data.editUserInfo }, showEditModal: false });
          wx.setStorageSync('userInfo', this.data.userInfo);
          wx.showToast({ title: '保存成功', icon: 'success' });
        } else {
          throw new Error(res.message || '保存失败');
        }
      })
      .catch(err => {
        wx.hideLoading();
        console.error('保存失败:', err);
        wx.showToast({ title: err.message || '保存失败', icon: 'none' });
      });
  },

  // 查看我的活动
  onViewMyActivities() {
    wx.switchTab({
      url: '/pages/my-activities/my-activities'
    });
  },

  // 查看已完成活动
  onViewCompletedActivities() {
    wx.switchTab({
      url: '/pages/my-activities/my-activities'
    });
    // 可以通过全局变量或事件传递筛选条件
  },

  // 查看即将开始活动
  onViewUpcomingActivities() {
    wx.switchTab({
      url: '/pages/my-activities/my-activities'
    });
  },

  // 查看收藏活动
  onViewFavorites() {
    wx.showToast({
      title: '收藏功能开发中',
      icon: 'none'
    });
  },

  // 查看浏览历史
  onViewHistory() {
    wx.showToast({
      title: '浏览历史功能开发中',
      icon: 'none'
    });
  },

  // 发布活动
  onCreateActivity() {
    wx.navigateTo({
      url: '/pages/create-activity/create-activity'
    });
  },

  // 管理我的活动
  onManageActivities() {
    wx.showToast({
      title: '管理功能开发中',
      icon: 'none'
    });
  },

  // 设置
  onSettings() {
    wx.showToast({
      title: '设置功能开发中',
      icon: 'none'
    });
  },

  // 帮助与反馈
  onHelp() {
    wx.showModal({
      title: '帮助与反馈',
      content: '如有问题请联系客服：400-123-4567\n或发送邮件至：support@example.com',
      showCancel: false,
      confirmText: '知道了'
    });
  },

  // 关于我们
  onAbout() {
    wx.showModal({
      title: '关于我们',
      content: '校园活动报名小程序 v1.0.0\n\n让校园生活更精彩！',
      showCancel: false,
      confirmText: '知道了'
    });
  },

  // 退出登录
  onLogout() {
    wx.showModal({
      title: '确认退出',
      content: '确定要退出登录吗？',
      success: (res) => {
        if (res.confirm) {
          // 清除本地存储的用户信息
          wx.removeStorageSync('userInfo');
          wx.removeStorageSync('token');
          wx.removeStorageSync('userId');
          
          // 跳转到登录页面或首页
          wx.reLaunch({ url: '/pages/login/login' });
          
          wx.showToast({
            title: '已退出登录',
            icon: 'success'
          });
        }
      }
    });
  },

  // 图片错误处理
  onImageError(e) {
    console.log('图片加载失败:', e);
    // 可以设置默认头像
    this.setData({
      'userInfo.avatar': 'https://picsum.photos/150?random=avatar'
    });
  },

  // 阻止事件冒泡
  stopPropagation() {
    // 阻止点击弹窗内容时关闭弹窗
  }
})