// API接口配置和封装
const API_BASE_URL = 'http://localhost:8080/api';

// 请求封装
const request = (options) => {
  return new Promise((resolve, reject) => {
    wx.request({
      url: API_BASE_URL + options.url,
      method: options.method || 'GET',
      data: options.data || {},
      header: {
        'Content-Type': 'application/json',
        ...options.header
      },
      success: (res) => {
        if (res.statusCode === 200) {
          resolve(res.data);
        } else {
          reject(new Error(`请求失败: ${res.statusCode}`));
        }
      },
      fail: (err) => {
        reject(err);
      }
    });
  });
};

// 微信登录
const wechatLogin = (code, encryptedData, iv) => {
  return request({
    url: '/auth/wechat/login',
    method: 'POST',
    data: {
      code,
      encryptedData,
      iv
    }
  });
};

// 获取openid
const getOpenid = (code) => {
  return request({
    url: '/auth/wechat/openid',
    method: 'POST',
    data: { code }
  });
};

// 活动相关API
const activityApi = {
  // 分页查询活动列表
  getActivityPage: (params) => {
    return request({
      url: '/activities/page',
      method: 'GET',
      data: params
    });
  },
  
  // 获取活动详情
  getActivityDetail: (id) => {
    return request({
      url: `/activities/${id}`,
      method: 'GET'
    });
  },
  
  // 创建活动
  createActivity: (activity) => {
    return request({
      url: '/activities',
      method: 'POST',
      data: activity
    });
  },
  
  // 更新活动
  updateActivity: (id, activity) => {
    return request({
      url: `/activities/${id}`,
      method: 'PUT',
      data: activity
    });
  },
  
  // 删除活动
  deleteActivity: (id) => {
    return request({
      url: `/activities/${id}`,
      method: 'DELETE'
    });
  },
  
  // 搜索活动
  searchActivities: (keyword) => {
    return request({
      url: '/activities/search',
      method: 'GET',
      data: { keyword }
    });
  },
  
  // 根据分类查询活动
  getActivitiesByCategory: (categoryId) => {
    return request({
      url: `/activities/category/${categoryId}`,
      method: 'GET'
    });
  },
  
  // 获取推荐活动
  getFeaturedActivities: () => {
    return request({
      url: '/activities/featured',
      method: 'GET'
    });
  },
  
  // 获取即将开始的活动
  getUpcomingActivities: () => {
    return request({
      url: '/activities/upcoming',
      method: 'GET'
    });
  }
};

// 报名相关API
const registrationApi = {
  // 报名活动
  registerActivity: (activityId, userId, name, phone, remark) => {
    return request({
      url: '/registrations',
      method: 'POST',
      data: { activityId, userId, name, phone, remark }
    });
  },
  
  // 取消报名
  cancelRegistration: (data) => {
    return request({
      url: '/registrations',
      method: 'DELETE',
      data: data
    });
  },
  
  // 检查是否已报名
  checkRegistration: (activityId, userId) => {
    return request({
      url: '/registrations/check',
      method: 'GET',
      data: {
        activityId,
        userId
      }
    });
  },
  
  // 获取用户报名记录
  getUserRegistrations: (userId) => {
    return request({
      url: `/registrations/user/${userId}`,
      method: 'GET'
    });
  },
  
  // 获取用户即将参加的活动
  getUserUpcomingActivities: (userId) => {
    return request({
      url: `/registrations/user/${userId}/upcoming`,
      method: 'GET'
    });
  },
  
  // 获取用户已完成的活动
  getUserCompletedActivities: (userId) => {
    return request({
      url: `/registrations/user/${userId}/completed`,
      method: 'GET'
    });
  },
  
  // 获取活动报名者列表
  getActivityParticipants: (activityId) => {
    return request({
      url: `/registrations/activity/${activityId}`,
      method: 'GET'
    });
  },
  
  // 签到
  checkIn: (activityId, userId) => {
    return request({
      url: '/registrations/checkin',
      method: 'POST',
      data: {
        activityId,
        userId
      }
    });
  }
};

// 用户相关API
const userApi = {
  // 获取用户信息
  getUserInfo: (id) => {
    return request({
      url: `/users/${id}`,
      method: 'GET'
    });
  },
  
  // 更新用户信息
  updateUserInfo: (id, userInfo) => {
    return request({
      url: `/users/${id}`,
      method: 'PUT',
      data: userInfo
    });
  },
  
  // 获取用户统计数据
  getUserStats: (id) => {
    return request({
      url: `/users/${id}/stats`,
      method: 'GET'
    });
  }
};

// 活动分类相关API
const categoryApi = {
  // 获取所有活动分类
  getAllCategories: () => {
    return request({
      url: '/categories',
      method: 'GET'
    });
  },
  
  // 根据ID获取分类
  getCategoryById: (id) => {
    return request({
      url: `/categories/${id}`,
      method: 'GET'
    });
  }
};

// 导出API
module.exports = {
  wechatLogin,
  getOpenid,
  activityApi,
  registrationApi,
  userApi,
  categoryApi
};
