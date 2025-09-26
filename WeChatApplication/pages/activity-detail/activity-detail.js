// activity-detail.js - 活动详情页面
const api = require('../../utils/api');

Page({
  data: {
    activityId: null,
    activity: {},
    participants: [],
    isRegistered: false,
    showRegisterModal: false,
    userInfo: {
      name: '',
      phone: '',
      remark: ''
    },
    canRegister: false,
    registerReason: ''
  },

  onLoad(options) {
    const activityId = options.id;
    if (activityId) {
      this.setData({ activityId });
      this.loadActivityDetail(activityId);
      this.loadParticipants(activityId);
      this.checkRegistrationStatus(activityId);
    }
  },

  // 加载活动详情
  loadActivityDetail(activityId) {
    console.log('开始加载活动详情，活动ID:', activityId);
    api.activityApi.getActivityDetail(activityId)
      .then(res => {
        console.log('活动详情API响应:', res);
        if (res.code === 200) {
          const act = res.data || {};
          // 统一补齐 images 与首图
          if (!Array.isArray(act.images)) {
            if (typeof act.images === 'string' && act.images) {
              try {
                const arr = JSON.parse(act.images);
                act.images = Array.isArray(arr) ? arr : [];
              } catch (e) {
                act.images = act.images ? [act.images] : [];
              }
            } else {
              act.images = [];
            }
          }
          if (!act.image && act.images.length > 0) {
            act.image = act.images[0];
          }
          
          // 根据截止时间判断活动状态
          const now = new Date();
          const deadline = new Date(act.deadline);
          console.log('当前时间:', now);
          console.log('截止时间:', deadline);
          if (deadline < now) {
            act.status = 'expired';
          } else {
            act.status = 'active';
          }
          console.log('活动状态:', act.status);
          
          this.setData({ activity: act }, () => this.updateRegisterState());
        } else {
          throw new Error(res.message || '加载失败');
        }
      })
      .catch(err => {
        console.error('加载活动详情失败:', err);
        wx.showToast({
          title: '加载失败',
          icon: 'none'
        });
      });
  },

  // 统一更新报名可用状态
  updateRegisterState() {
    const act = this.data.activity || {};
    const now = Date.now();
    const parseDate = (s) => {
      if (!s) return 0;
      s = String(s).trim();
      if (/^\d{4}-\d{2}-\d{2}\s+\d{2}:\d{2}:\d{2}$/.test(s)) s = s.replace(' ', 'T');
      let t = Date.parse(s);
      if (isNaN(t)) t = Date.parse(s.replace(/-/g, '/'));
      return isNaN(t) ? 0 : t;
    };
    const ddlTs = parseDate(act.deadline);
    let can = true;
    let reason = '';
    
    // 如果已经报名，不能再次报名
    if (this.data.isRegistered) {
      can = false; reason = '您已报名此活动';
    } else if (!ddlTs) {
      can = false; reason = '报名截止时间无效';
    } else if (now > ddlTs) {
      can = false; reason = '报名已截止';
    } else if (act.maxParticipants && (act.registeredCount || 0) >= act.maxParticipants) {
      can = false; reason = '名额已满';
    }
    this.setData({ canRegister: can, registerReason: reason });
  },

  // 详情页图片兜底
  onDetailImageError(e) {
    const idx = e.currentTarget.dataset.index;
    const act = this.data.activity;
    if (Array.isArray(act.images) && act.images[idx]) {
      act.images[idx] = 'https://picsum.photos/750/400?blur=3';
      if (!act.image) act.image = act.images[idx];
      this.setData({ activity: act });
    }
  },

  // 通用图片错误处理
  onImageError(e) {
    const idx = e.currentTarget.dataset.index;
    const act = this.data.activity;
    if (Array.isArray(act.images) && act.images[idx]) {
      act.images[idx] = 'https://picsum.photos/750/400?blur=3';
      if (!act.image) act.image = act.images[idx];
      this.setData({ activity: act });
    }
  },

  // 获取模拟活动详情数据
  getMockActivityDetail(activityId) {
    const activities = {
      '1': {
        id: 1,
        title: '校园篮球友谊赛',
        description: '欢迎所有篮球爱好者参加本次友谊赛！比赛分为男子组和女子组，采用循环赛制。\n\n比赛规则：\n1. 每队5人，比赛时间40分钟\n2. 采用国际篮联最新规则\n3. 比赛期间请遵守体育道德\n\n奖品设置：\n- 冠军：奖金1000元 + 奖杯\n- 亚军：奖金500元 + 奖牌\n- 季军：奖金300元 + 奖牌',
        images: ['https://picsum.photos/750/400?random=1', 'https://picsum.photos/750/400?random=11'],
        startDate: '2024-01-15',
        startTime: '14:00',
        deadline: '2024-01-10 23:59',
        location: '学校体育馆篮球场',
        registeredCount: 15,
        maxParticipants: 20,
        status: 'active',
        tags: ['篮球', '运动', '比赛'],
        fee: 0,
        requirements: '1. 身体健康，无心脏病等不适合剧烈运动的疾病\n2. 具备基本的篮球技能\n3. 自备运动装备\n4. 遵守比赛规则和体育道德',
        schedule: '14:00-14:30 开幕式\n14:30-16:30 小组赛\n16:30-17:00 休息\n17:00-18:30 决赛\n18:30-19:00 颁奖仪式',
        rewards: '冠军：奖金1000元 + 奖杯\n亚军：奖金500元 + 奖牌\n季军：奖金300元 + 奖牌\n所有参赛者：纪念品一份',
        contact: '联系人：体育部张老师\n电话：13800138000\n微信：sports_zhang'
      },
      '2': {
        id: 2,
        title: '编程竞赛',
        description: 'ACM编程竞赛，面向全校学生开放。比赛采用在线编程形式，题目涵盖算法、数据结构、数学等多个领域。\n\n比赛形式：\n- 个人赛，3小时\n- 在线提交代码\n- 实时排名\n\n题目类型：\n- 算法题：动态规划、图论、贪心等\n- 数据结构：树、图、哈希表等\n- 数学题：数论、组合数学等',
        images: ['https://picsum.photos/750/400?random=2'],
        startDate: '2024-01-20',
        startTime: '09:00',
        deadline: '2024-01-12 23:59',
        location: '计算机学院机房',
        registeredCount: 8,
        maxParticipants: 30,
        status: 'active',
        tags: ['编程', '竞赛', '学习'],
        fee: 50,
        requirements: '1. 具备基本的编程能力\n2. 熟悉至少一种编程语言（C/C++/Java/Python）\n3. 自备笔记本电脑\n4. 遵守考试纪律',
        schedule: '09:00-09:30 签到和准备\n09:30-12:30 正式比赛\n12:30-13:30 午餐休息\n13:30-14:00 成绩公布和颁奖',
        rewards: '一等奖：奖金2000元 + 证书\n二等奖：奖金1000元 + 证书\n三等奖：奖金500元 + 证书\n优秀奖：精美礼品 + 证书',
        contact: '联系人：计算机学院李老师\n电话：13900139000\n邮箱：li@university.edu'
      }
    };
    
    return activities[activityId] || activities['1'];
  },

  // 加载报名者列表
  loadParticipants(activityId) {
    api.registrationApi.getActivityParticipants(activityId)
      .then(res => {
        if (res.code === 200) {
          this.setData({ participants: res.data || [] });
        } else {
          console.error('加载报名者失败:', res.message);
        }
      })
      .catch(err => {
        console.error('加载报名者失败:', err);
      });
  },

  // 检查报名状态
  checkRegistrationStatus(activityId) {
    // 获取当前用户ID（实际应该从登录状态获取）
    const userId = wx.getStorageSync('userId');
    if (!userId) return;
    
    api.registrationApi.checkRegistration(activityId, userId)
      .then(res => {
        if (res.code === 200) {
          this.setData({ isRegistered: res.data });
        }
      })
      .catch(err => {
        console.error('检查报名状态失败:', err);
      });
  },

  // 点击报名按钮
  onRegister() {
    // 以截止时间/名额判断是否可报名
    const deadline = this.data.activity.deadline;
    const parseDate = (s) => {
      if (!s) return 0;
      s = String(s).trim();
      if (/^\d{4}-\d{2}-\d{2}\s+\d{2}:\d{2}:\d{2}$/.test(s)) s = s.replace(' ', 'T');
      let t = Date.parse(s);
      if (isNaN(t)) t = Date.parse(s.replace(/-/g, '/'));
      return isNaN(t) ? 0 : t;
    };
    const nowTs = Date.now();
    const ddlTs = parseDate(deadline);
    if (!ddlTs) {
      wx.showToast({ title: '截止时间无效', icon: 'none' });
      return;
    }
    if (nowTs > ddlTs) {
      wx.showToast({ title: '报名已截止', icon: 'none' });
      return;
    }
    if (this.data.activity.maxParticipants && this.data.activity.registeredCount >= this.data.activity.maxParticipants) {
      wx.showToast({ title: '名额已满', icon: 'none' });
      return;
    }
    
    if (this.data.isRegistered) {
      wx.showToast({
        title: '您已报名此活动',
        icon: 'none'
      });
      return;
    }
    
    this.setData({ showRegisterModal: true });
  },

  // 关闭报名弹窗
  onCloseModal() {
    this.setData({ showRegisterModal: false });
  },

  // 阻止事件冒泡
  stopPropagation() {
    // 阻止点击弹窗内容时关闭弹窗
  },

  // 输入姓名
  onNameInput(e) {
    this.setData({
      'userInfo.name': e.detail.value
    });
  },

  // 输入手机号
  onPhoneInput(e) {
    this.setData({
      'userInfo.phone': e.detail.value
    });
  },

  // 输入备注
  onRemarkInput(e) {
    this.setData({
      'userInfo.remark': e.detail.value
    });
  },

  // 确认报名
  onConfirmRegister() {
    const { name, phone, remark } = this.data.userInfo;
    
    // 验证必填信息
    if (!name.trim()) {
      wx.showToast({
        title: '请输入姓名',
        icon: 'none'
      });
      return;
    }
    
    if (!phone.trim()) {
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
    
    // 显示加载状态
    wx.showLoading({
      title: '报名中...'
    });
    
    // 获取当前用户ID
    const userId = wx.getStorageSync('userId');
    if (!userId) {
      wx.hideLoading();
      wx.showToast({
        title: '请先登录',
        icon: 'none'
      });
      return;
    }
    
    // 调用报名API
    api.registrationApi.registerActivity(this.data.activityId, userId, name, phone, remark)
      .then(res => {
        wx.hideLoading();
        
        if (res.code === 200) {
          this.setData({
            isRegistered: true,
            showRegisterModal: false,
            'activity.registeredCount': this.data.activity.registeredCount + 1
          }, () => this.updateRegisterState());
          
          wx.showToast({
            title: '报名成功！',
            icon: 'success'
          });
          
          // 重新加载报名者列表
          this.loadParticipants(this.data.activityId);
        } else {
          throw new Error(res.message || '报名失败');
        }
      })
      .catch(err => {
        wx.hideLoading();
        console.error('报名失败:', err);
        wx.showToast({
          title: err.message || '报名失败',
          icon: 'none'
        });
      });
  },

  // 格式化时间
  formatTime(date) {
    const year = date.getFullYear();
    const month = (date.getMonth() + 1).toString().padStart(2, '0');
    const day = date.getDate().toString().padStart(2, '0');
    const hour = date.getHours().toString().padStart(2, '0');
    const minute = date.getMinutes().toString().padStart(2, '0');
    
    return `${year}-${month}-${day} ${hour}:${minute}`;
  },

  // 分享活动
  onShareAppMessage() {
    return {
      title: this.data.activity.title,
      path: `/pages/activity-detail/activity-detail?id=${this.data.activityId}`,
      imageUrl: this.data.activity.images ? this.data.activity.images[0] : ''
    };
  }
})