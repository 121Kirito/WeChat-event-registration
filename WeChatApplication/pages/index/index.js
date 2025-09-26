// index.js - 活动列表首页
const api = require('../../utils/api');

Page({
  data: {
    // 轮播图数据
    bannerImages: [
      '/images/轮播图1.jpg',
      'https://picsum.photos/750/300?random=101',
      'https://picsum.photos/750/300?random=102',
      'https://picsum.photos/750/300?random=103'
    ],

    // 搜索相关
    searchKeyword: '',

    // 排序相关
    sortType: 'time', // time, deadline, popular

    // 筛选相关
    selectedType: '', // 选中的活动类型
    selectedTag: '',  // 选中的标签
    typeIndex: 0, // 分类选择器索引
    activityTypes: [], // 活动类型列表（从后端获取）
    pickerTypes: ['全部'], // 下拉框选项（包含"全部"）
    activityTags: [], // 活动标签列表（动态生成）

    // 活动数据
    activities: [],
    filteredActivities: [],

    // 分页相关
    currentPage: 1,
    pageSize: 10,
    loading: false,
    noMoreData: false,
  },

  onLoad() {
    // 确保分类加载完成后再加载活动
    this.loadCategories().then(() => {
      this.loadActivities();
    });
  },

  onShow() {
    this.refreshData();
  },

  /** ================== 分类加载 ================== */
  loadCategories() {
    return new Promise((resolve) => {
      api.categoryApi.getAllCategories()
        .then(res => {
          if (res.code === 200 && res.data) {
            const categories = res.data.map(category => category.name);
            this.setData({
              activityTypes: categories,
              pickerTypes: ['全部', ...categories]
            }, resolve); // 确保 setData 后再 resolve
          } else {
            const categories = ['体育运动', '学术竞赛', '文艺活动', '志愿服务', '社会实践', '创新创业', '其他'];
            this.setData({
              activityTypes: categories,
              pickerTypes: ['全部', ...categories]
            }, resolve);
          }
        })
        .catch(err => {
          console.error('获取分类数据失败:', err);
          const categories = ['体育运动', '学术竞赛', '文艺活动', '志愿服务', '社会实践', '创新创业', '其他'];
          this.setData({
            activityTypes: categories,
            pickerTypes: ['全部', ...categories]
          }, resolve);
        });
    });
  },

  /** ================== 活动加载 ================== */
  loadActivities() {
    if (this.data.loading) return;
    this.setData({ loading: true });

    const params = {
      current: this.data.currentPage,
      size: this.data.pageSize,
      sortType: this.data.sortType
    };
    if (this.data.searchKeyword && this.data.searchKeyword.trim()) {
      params.keyword = this.data.searchKeyword.trim();
    }

    api.activityApi.getActivityPage(params)
      .then(res => {
        if (res.code === 200) {
          // 构建 categoryId -> name 的映射表
          const categoryMap = {};
          this.data.activityTypes.forEach((name, index) => {
            categoryMap[index + 1] = name; // 后端分类ID是从1开始
          });

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
              }
            }
            return item;
          };

          const newActivities = (res.data.records || []).map(activity => {
            const normalized = normalizeImage(activity);

            // 根据截止时间判断活动状态
            const now = new Date();
            const deadline = new Date(normalized.deadline);
            normalized.status = deadline < now ? 'expired' : 'active';

            // 处理分类
            if (normalized.category && typeof normalized.category === 'object' && normalized.category.name) {
              normalized.category = normalized.category.name;
            } else if (typeof normalized.category === 'string' && normalized.category) {
              normalized.category = normalized.category;
            } else if (normalized.categoryId) {
              normalized.category = categoryMap[normalized.categoryId] || '其他';
            } else {
              normalized.category = '其他';
            }

            return normalized;
          });

          const activities = this.data.currentPage === 1
            ? newActivities
            : [...this.data.activities, ...newActivities];

          this.setData({
            activities: activities
          }, () => {
            this.setData({
              filteredActivities: this.filterAndSortActivities(activities),
              loading: false,
              noMoreData: newActivities.length < this.data.pageSize
            });
            this.updateActivityTags(activities);
          });
        } else {
          throw new Error(res.message || '加载失败');
        }
      })
      .catch(err => {
        console.error('加载活动失败:', err);
        this.setData({ loading: false });
      });
  },

  /** ================== 搜索 ================== */
  onSearchInput(e) {
    this.setData({
      searchKeyword: e.detail.value
    });
  },

  onSearch() {
    this.setData({
      filteredActivities: this.filterAndSortActivities(this.data.activities)
    });
  },

  /** ================== 排序 ================== */
  onSortChange(e) {
    const sortType = e.currentTarget.dataset.type;
    this.setData({
      sortType: sortType
    }, () => {
      this.setData({
        filteredActivities: this.filterAndSortActivities(this.data.activities)
      });
    });
  },

  /** ================== 类型筛选 ================== */
  onTypePickerChange(e) {
    const typeIndex = parseInt(e.detail.value);
    const selectedType = typeIndex === 0 ? '' : this.data.activityTypes[typeIndex - 1];

    // 使用 setData 回调保证筛选立即生效
    this.setData({
      typeIndex,
      selectedType
    }, () => {
      this.setData({
        filteredActivities: this.filterAndSortActivities(this.data.activities)
      });
    });
  },

  /** ================== 标签筛选 ================== */
  onTagFilter(e) {
    const selectedTag = e.currentTarget.dataset.tag;

    // 使用 setData 回调保证筛选立即生效
    this.setData({
      selectedTag
    }, () => {
      this.setData({
        filteredActivities: this.filterAndSortActivities(this.data.activities)
      });
    });
  },

  /** ================== 过滤和排序 ================== */
  filterAndSortActivities(activities) {
    let filtered = activities;

    // 搜索过滤
    if (this.data.searchKeyword) {
      const keyword = this.data.searchKeyword.toLowerCase();
      filtered = activities.filter(activity =>
        activity.title.toLowerCase().includes(keyword) ||
        activity.description.toLowerCase().includes(keyword) ||
        (activity.tags && activity.tags.some(tag => tag.toLowerCase().includes(keyword)))
      );
    }

    // 分类过滤
    if (this.data.selectedType) {
      filtered = filtered.filter(activity => activity.category === this.data.selectedType);
    }

    // 标签过滤
    if (this.data.selectedTag) {
      filtered = filtered.filter(activity =>
        activity.tags && activity.tags.includes(this.data.selectedTag)
      );
    }

    // 时间解析
    const parseDate = (val) => {
      if (!val) return 0;
      if (val instanceof Date) return val.getTime();
      if (typeof val === 'number') return val;
      let s = String(val).trim();
      if (/^\d{4}-\d{2}-\d{2}\s+\d{2}:\d{2}:\d{2}$/.test(s)) {
        s = s.replace(' ', 'T');
      }
      let t = Date.parse(s);
      if (isNaN(t)) {
        s = s.replace(/-/g, '/');
        t = Date.parse(s);
      }
      return isNaN(t) ? 0 : t;
    };

    // 排序
    filtered.sort((a, b) => {
      switch (this.data.sortType) {
        case 'time':
          return parseDate(b.createTime) - parseDate(a.createTime);
        case 'deadline':
          return parseDate(a.deadline) - parseDate(b.deadline);
        case 'popular':
          return (b.registeredCount || 0) - (a.registeredCount || 0);
        default:
          return 0;
      }
    });

    return filtered;
  },

  /** ================== 标签更新 ================== */
  updateActivityTags(activities) {
    const allTags = new Set();
    activities.forEach(activity => {
      if (activity.tags && Array.isArray(activity.tags)) {
        activity.tags.forEach(tag => allTags.add(tag));
      }
    });
    const sortedTags = Array.from(allTags).sort();
    this.setData({
      activityTags: sortedTags
    }, () => {
      // 如果当前选中的标签不在新的标签列表中，清除选择
      if (this.data.selectedTag && !sortedTags.includes(this.data.selectedTag)) {
        this.setData({
          selectedTag: '',
          filteredActivities: this.filterAndSortActivities(activities)
        });
      }
    });
  },

  /** ================== 刷新和加载更多 ================== */
  onLoadMore() {
    if (!this.data.loading && !this.data.noMoreData) {
      this.setData({
        currentPage: this.data.currentPage + 1
      }, () => {
        this.loadActivities();
      });
    }
  },

  refreshData() {
    this.setData({
      currentPage: 1,
      activities: [],
      filteredActivities: [],
      noMoreData: false,
      selectedType: '',
      selectedTag: '',
      typeIndex: 0,
      pickerTypes: ['全部']
    }, () => {
      this.loadCategories().then(() => {
        this.loadActivities();
      });
    });
  },

  onPullDownRefresh() {
    this.refreshData();
    wx.stopPullDownRefresh();
  },

  /** ================== 图片处理 ================== */
  onImageError(e) {
    const idx = e.currentTarget.dataset.index;
    const activities = this.data.activities;
    if (activities[idx]) {
      activities[idx].image = 'https://picsum.photos/750/400?blur=3';
      this.setData({
        activities,
        filteredActivities: this.filterAndSortActivities(activities)
      });
    }
  },

  onBannerImageError(e) {
    const idx = e.currentTarget.dataset.index;
    const imgs = this.data.bannerImages;
    imgs[idx] = 'https://picsum.photos/750/300?blur=3';
    this.setData({ bannerImages: imgs });
  },

  /** ================== 活动详情跳转 ================== */
  onActivityTap(e) {
    const activityId = e.currentTarget.dataset.id;
    wx.navigateTo({
      url: `/pages/activity-detail/activity-detail?id=${activityId}`
    });
  }
});
