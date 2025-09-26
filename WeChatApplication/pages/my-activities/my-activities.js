// pages/my-activities/my-activities.js
const api = require('../../utils/api');

Page({
  data: {
    currentFilter: 'all',
    filterTabs: [
      { key: 'all', label: '全部' },
      { key: 'upcoming', label: '即将开始' },
      { key: 'ongoing', label: '进行中' },
      { key: 'completed', label: '已完成' },
      { key: 'cancelled', label: '已取消' }
    ],
    totalRegistered: 0,
    completedActivities: 0,
    upcomingActivities: 0,
    activities: [],
    filteredActivities: [],
    currentPage: 1,
    pageSize: 10,
    loading: false,
    noMoreData: false,
    showCancelModal: false,
    cancelActivity: {}
  },

  onLoad() {
    this.loadMyActivities();
  },

  onShow() {
    this.refreshData();
  },

  loadMyActivities() {
    if (this.data.loading) return;
    this.setData({ loading: true });
    const userId = wx.getStorageSync('userId');
    if (!userId) {
      wx.showToast({ title: '请先登录', icon: 'none' });
      this.setData({ loading: false });
      return;
    }

    const normalizeImage = (item) => {
      if (!item) return item;
      if (!item.image) {
        if (Array.isArray(item.images) && item.images.length > 0) {
          item.image = item.images[0];
        } else if (typeof item.images === 'string' && item.images) {
          try {
            const arr = JSON.parse(item.images);
            if (Array.isArray(arr) && arr.length > 0) item.image = arr[0];
          } catch (e) {
            item.image = item.images;
          }
        } else {
          item.image = 'https://picsum.photos/120/120?random=' + (item.id || Math.random());
        }
      }
      return item;
    };

    api.registrationApi.getUserRegistrations(userId)
      .then(res => {
        if (res.code === 200) {
          const now = new Date();
          const list = (res.data || []).map(r => {
            const activity = normalizeImage({
              id: r.activityId || r.id,
              title: r.title || '未知活动',
              description: r.description || '暂无描述',
              image: r.image,
              images: r.images,
              startDate: r.startDate || r.start_date || '未知日期',
              startTime: r.startTime || r.start_time || '未知时间',
              endDate: r.endDate || r.end_date || null,
              location: r.location || '未知地点',
              registerTime: r.registerTime || r.register_time,
              registerStatus: r.status === 0 ? '已取消' : '报名成功',
              categoryName: r.categoryName || r.category_name
            });

            const startDate = new Date(r.startDate || r.start_date);
            const endDate = r.endDate || r.end_date ? new Date(r.endDate || r.end_date) : null;

            // 修正状态逻辑
            if (r.status === 0) {
              activity.status = 'cancelled';
            } else {
              if (startDate > now) {
                activity.status = 'upcoming';
              } else if (endDate && now > endDate) {
                activity.status = 'completed';
              } else {
                activity.status = 'ongoing';
              }
            }

            return activity;
          });

          // 前端分页
          const start = (this.data.currentPage - 1) * this.data.pageSize;
          const end = this.data.currentPage * this.data.pageSize;
          const newActivities = list.slice(start, end);
          const activities = this.data.currentPage === 1 ? newActivities : [...this.data.activities, ...newActivities];

          this.setData({
            activities,
            filteredActivities: this.filterActivities(activities),
            loading: false,
            noMoreData: newActivities.length < this.data.pageSize
          });

          this.updateStats(activities);
        } else {
          throw new Error(res.message || '加载失败');
        }
      })
      .catch(err => {
        console.error('加载我的活动失败:', err);
        this.setData({ loading: false });
        wx.showToast({ title: '加载失败: ' + err.message, icon: 'none' });
      });
  },

  updateStats(activities) {
    const totalRegistered = activities.length;
    const completedActivities = activities.filter(item => item.status === 'completed').length;
    const upcomingActivities = activities.filter(item => item.status === 'upcoming').length;
    this.setData({ totalRegistered, completedActivities, upcomingActivities });
  },

  filterActivities(activities) {
    if (this.data.currentFilter === 'all') return activities;
    return activities.filter(activity => activity.status === this.data.currentFilter);
  },

  onFilterChange(e) {
    const filterKey = e.currentTarget.dataset.key;
    this.setData({
      currentFilter: filterKey,
      filteredActivities: this.filterActivities(this.data.activities)
    });
  },

  getEmptyText() {
    const filterMap = {
      'all': '暂无活动记录',
      'upcoming': '暂无即将开始的活动',
      'ongoing': '暂无进行中的活动',
      'completed': '暂无已完成的活动',
      'cancelled': '暂无已取消的活动'
    };
    return filterMap[this.data.currentFilter] || '暂无活动';
  },

  getStatusText(status) {
    const statusMap = {
      'upcoming': '即将开始',
      'ongoing': '进行中',
      'completed': '已完成',
      'cancelled': '已取消'
    };
    return statusMap[status] || '未知状态';
  },

  onActivityTap(e) {
    const activityId = e.currentTarget.dataset.id;
    wx.navigateTo({ url: `/pages/activity-detail/activity-detail?id=${activityId}` });
  },

  onViewDetail(e) {
    const activityId = e.currentTarget.dataset.id;
    wx.navigateTo({ url: `/pages/activity-detail/activity-detail?id=${activityId}` });
  },

  onCancelRegister(e) {
    const activityId = e.currentTarget.dataset.id;
    const activity = this.data.activities.find(item => item.id === activityId);
    if (activity) {
      this.setData({ showCancelModal: true, cancelActivity: activity });
    }
  },

  onCloseCancelModal() {
    this.setData({ showCancelModal: false, cancelActivity: {} });
  },

  onConfirmCancel() {
    const activityId = this.data.cancelActivity.id;
    const userId = wx.getStorageSync('userId');
    if (!userId) return wx.showToast({ title: '请先登录', icon: 'none' });

    wx.showLoading({ title: '处理中...' });

    api.registrationApi.cancelRegistration({ activityId, userId })
      .then(res => {
        wx.hideLoading();
        if (res.code === 200) {
          const activities = this.data.activities.map(item => {
            if (item.id === activityId) return { ...item, status: 'cancelled', registerStatus: '已取消' };
            return item;
          });
          this.setData({
            activities,
            filteredActivities: this.filterActivities(activities),
            showCancelModal: false,
            cancelActivity: {}
          });
          this.updateStats(activities);
          wx.showToast({ title: '取消成功', icon: 'success' });
        } else {
          throw new Error(res.message || '取消失败');
        }
      })
      .catch(err => {
        wx.hideLoading();
        wx.showToast({ title: err.message || '取消失败', icon: 'none' });
      });
  },

  onMyImageError(e) {
    const idx = e.currentTarget.dataset.index;
    const activities = this.data.activities;
    if (activities[idx]) {
      activities[idx].image = 'https://picsum.photos/750/400?blur=3';
      this.setData({ activities, filteredActivities: this.filterActivities(activities) });
    }
  },

  onLoadMore() {
    if (!this.data.loading && !this.data.noMoreData) {
      this.setData({ currentPage: this.data.currentPage + 1 });
      this.loadMyActivities();
    }
  },

  refreshData() {
    this.setData({ currentPage: 1, activities: [], filteredActivities: [], noMoreData: false });
    this.loadMyActivities();
  },

  onPullDownRefresh() {
    this.refreshData();
    wx.stopPullDownRefresh();
  },

  stopPropagation() {}
});
