// login.js - 微信登录页面
const api = require('../../utils/api');

Page({
  data: {
    loading: false
  },

  onLoad() {
    // 检查是否已登录
    this.checkLoginStatus();
  },

  // 检查登录状态
  checkLoginStatus() {
    const userInfo = wx.getStorageSync('userInfo');
    if (userInfo && userInfo.id) {
      // 已登录，跳转到首页
      wx.switchTab({
        url: '/pages/index/index'
      });
    }
  },

  // 微信登录
  onWechatLogin() {
    if (this.data.loading) return;
    
    this.setData({ loading: true });
    
    // 获取微信登录code
    wx.login({
      success: (res) => {
        if (res.code) {
          this.performLogin(res.code);
        } else {
          this.setData({ loading: false });
          wx.showToast({
            title: '获取登录凭证失败',
            icon: 'none'
          });
        }
      },
      fail: (err) => {
        this.setData({ loading: false });
        console.error('微信登录失败:', err);
        wx.showToast({
          title: '登录失败',
          icon: 'none'
        });
      }
    });
  },

  // 执行登录（先走基础登录，避免非手势触发限制）
  performLogin(code) {
    this.performBasicLogin(code);
  },

  // 基础登录（不获取用户详细信息）
  performBasicLogin(code) {
    api.wechatLogin(code)
      .then(res => {
        this.setData({ loading: false });
        
        if (res.code === 200) {
          // 保存用户信息到本地存储
          wx.setStorageSync('userInfo', res.data);
          wx.setStorageSync('userId', res.data.id);
          
          wx.showToast({
            title: '登录成功',
            icon: 'success'
          });
          
          // 跳转到首页
          setTimeout(() => {
            wx.switchTab({
              url: '/pages/index/index'
            });
          }, 1500);
        } else {
          throw new Error(res.message || '登录失败');
        }
      })
      .catch(err => {
        this.setData({ loading: false });
        console.error('登录失败:', err);
        wx.showToast({
          title: err.message || '登录失败',
          icon: 'none'
        });
      });
  }
});
