# TabBar图标解决方案

## 问题说明

微信小程序的TabBar不支持Base64编码的SVG图标，需要使用本地图片文件。之前的配置会导致"未找到"错误。

## 当前解决方案

### 方案1: 使用Emoji图标（已采用）
在TabBar的text字段中直接使用Emoji表情符号作为图标：

```json
{
  "tabBar": {
    "list": [
      {
        "pagePath": "pages/index/index",
        "text": "🏠 活动列表"
      },
      {
        "pagePath": "pages/my-activities/my-activities", 
        "text": "📋 我的活动"
      },
      {
        "pagePath": "pages/profile/profile",
        "text": "👤 个人中心"
      }
    ]
  }
}
```

**优势**:
- ✅ 无需外部图片文件
- ✅ 兼容性好，所有设备都支持
- ✅ 加载速度快
- ✅ 维护简单

**劣势**:
- ❌ 图标样式相对简单
- ❌ 无法自定义颜色

## 其他解决方案

### 方案2: 使用本地图片文件

如果需要更专业的图标，可以：

1. **创建images目录**:
   ```
   mkdir images
   ```

2. **准备图标文件**:
   - 普通状态图标: 81x81px
   - 选中状态图标: 81x81px
   - 支持PNG、JPG格式
   - 建议使用PNG格式以获得更好的效果

3. **配置TabBar**:
   ```json
   {
     "tabBar": {
       "list": [
         {
           "pagePath": "pages/index/index",
           "text": "活动列表",
           "iconPath": "images/home.png",
           "selectedIconPath": "images/home-active.png"
         }
       ]
     }
   }
   ```

### 方案3: 使用在线图标服务

可以使用一些在线图标服务生成图标：

1. **IconFont**: https://www.iconfont.cn/
2. **Feather Icons**: https://feathericons.com/
3. **Heroicons**: https://heroicons.com/

下载后转换为小程序支持的格式。

## 推荐方案

对于当前项目，推荐使用**方案1（Emoji图标）**，因为：

1. **简单易用**: 无需管理图片文件
2. **性能优秀**: 无额外加载时间
3. **维护方便**: 代码简洁，易于修改
4. **兼容性好**: 所有设备都支持Emoji

## 图标说明

当前使用的Emoji图标：
- 🏠 活动列表 - 房子图标，表示首页/主页
- 📋 我的活动 - 剪贴板图标，表示列表/记录
- 👤 个人中心 - 用户图标，表示个人/用户

## 自定义建议

如果需要更换图标，可以选择以下Emoji：

### 活动列表备选图标
- 🏠 房子
- 🏡 带花园的房子
- 🏘️ 房屋群
- 🏢 办公楼
- 🏬 商场

### 我的活动备选图标
- 📋 剪贴板
- 📝 备忘录
- 📄 文档
- 📊 图表
- ✅ 勾选标记

### 个人中心备选图标
- 👤 用户
- 👥 用户群
- 👨‍💼 商务人士
- 👩‍💼 商务女性
- ⚙️ 设置

## 总结

当前使用Emoji图标的方案已经解决了TabBar图标问题，项目可以正常运行。如果后续需要更专业的图标设计，可以考虑使用本地图片文件的方案。
