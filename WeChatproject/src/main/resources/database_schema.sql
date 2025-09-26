-- 校园活动报名系统数据库设计
-- 创建数据库
CREATE DATABASE IF NOT EXISTS campus_activity DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE campus_activity;

-- 1. 用户表
CREATE TABLE `user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `openid` varchar(100) NOT NULL COMMENT '微信openid',
  `unionid` varchar(100) DEFAULT NULL COMMENT '微信unionid',
  `nickname` varchar(50) DEFAULT NULL COMMENT '昵称',
  `avatar_url` varchar(500) DEFAULT NULL COMMENT '头像URL',
  `phone` varchar(20) DEFAULT NULL COMMENT '手机号',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `bio` varchar(500) DEFAULT NULL COMMENT '个人简介',
  `gender` tinyint(1) DEFAULT 0 COMMENT '性别：0-未知，1-男，2-女',
  `city` varchar(50) DEFAULT NULL COMMENT '城市',
  `province` varchar(50) DEFAULT NULL COMMENT '省份',
  `country` varchar(50) DEFAULT NULL COMMENT '国家',
  `level` int(11) DEFAULT 1 COMMENT '用户等级',
  `status` tinyint(1) DEFAULT 1 COMMENT '状态：0-禁用，1-正常',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_openid` (`openid`),
  KEY `idx_phone` (`phone`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- 2. 活动分类表
CREATE TABLE `activity_category` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '分类ID',
  `name` varchar(50) NOT NULL COMMENT '分类名称',
  `description` varchar(200) DEFAULT NULL COMMENT '分类描述',
  `icon` varchar(200) DEFAULT NULL COMMENT '分类图标',
  `sort_order` int(11) DEFAULT 0 COMMENT '排序',
  `status` tinyint(1) DEFAULT 1 COMMENT '状态：0-禁用，1-正常',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_sort_order` (`sort_order`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='活动分类表';

-- 3. 活动表
CREATE TABLE `activity` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '活动ID',
  `title` varchar(100) NOT NULL COMMENT '活动标题',
  `description` text COMMENT '活动描述',
  `category_id` bigint(20) NOT NULL COMMENT '分类ID',
  `organizer_id` bigint(20) NOT NULL COMMENT '组织者用户ID',
  `start_date` date NOT NULL COMMENT '活动开始日期',
  `start_time` time NOT NULL COMMENT '活动开始时间',
  `end_date` date DEFAULT NULL COMMENT '活动结束日期',
  `end_time` time DEFAULT NULL COMMENT '活动结束时间',
  `location` varchar(200) NOT NULL COMMENT '活动地点',
  `max_participants` int(11) NOT NULL COMMENT '最大参与人数',
  `registered_count` int(11) DEFAULT 0 COMMENT '已报名人数',
  `deadline` datetime NOT NULL COMMENT '报名截止时间',
  `fee` decimal(10,2) DEFAULT 0.00 COMMENT '活动费用',
  `requirements` text COMMENT '参与要求',
  `schedule` text COMMENT '活动安排',
  `rewards` text COMMENT '奖励说明',
  `contact` text COMMENT '联系方式',
  `images` json DEFAULT NULL COMMENT '活动图片JSON数组',
  `tags` json DEFAULT NULL COMMENT '活动标签JSON数组',
  `status` tinyint(1) DEFAULT 1 COMMENT '状态：0-草稿，1-发布，2-已结束，3-已取消',
  `is_featured` tinyint(1) DEFAULT 0 COMMENT '是否推荐：0-否，1-是',
  `view_count` int(11) DEFAULT 0 COMMENT '浏览次数',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_category_id` (`category_id`),
  KEY `idx_organizer_id` (`organizer_id`),
  KEY `idx_start_date` (`start_date`),
  KEY `idx_status` (`status`),
  KEY `idx_create_time` (`create_time`),
  CONSTRAINT `fk_activity_category` FOREIGN KEY (`category_id`) REFERENCES `activity_category` (`id`),
  CONSTRAINT `fk_activity_organizer` FOREIGN KEY (`organizer_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='活动表';

-- 4. 活动报名表
CREATE TABLE `activity_registration` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '报名ID',
  `activity_id` bigint(20) NOT NULL COMMENT '活动ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `name` varchar(50) NOT NULL COMMENT '报名姓名',
  `phone` varchar(20) NOT NULL COMMENT '报名手机号',
  `remark` text COMMENT '备注信息',
  `status` tinyint(1) DEFAULT 1 COMMENT '状态：0-已取消，1-已报名，2-已签到',
  `register_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '报名时间',
  `checkin_time` datetime DEFAULT NULL COMMENT '签到时间',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_activity_user` (`activity_id`, `user_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_register_time` (`register_time`),
  CONSTRAINT `fk_registration_activity` FOREIGN KEY (`activity_id`) REFERENCES `activity` (`id`),
  CONSTRAINT `fk_registration_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='活动报名表';

-- 5. 活动收藏表
CREATE TABLE `activity_favorite` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '收藏ID',
  `activity_id` bigint(20) NOT NULL COMMENT '活动ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_activity_user` (`activity_id`, `user_id`),
  KEY `idx_user_id` (`user_id`),
  CONSTRAINT `fk_favorite_activity` FOREIGN KEY (`activity_id`) REFERENCES `activity` (`id`),
  CONSTRAINT `fk_favorite_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='活动收藏表';

-- 6. 活动评价表
CREATE TABLE `activity_rating` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '评价ID',
  `activity_id` bigint(20) NOT NULL COMMENT '活动ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `rating` tinyint(1) NOT NULL COMMENT '评分：1-5星',
  `comment` text COMMENT '评价内容',
  `images` json DEFAULT NULL COMMENT '评价图片JSON数组',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_activity_user` (`activity_id`, `user_id`),
  KEY `idx_activity_id` (`activity_id`),
  KEY `idx_user_id` (`user_id`),
  CONSTRAINT `fk_rating_activity` FOREIGN KEY (`activity_id`) REFERENCES `activity` (`id`),
  CONSTRAINT `fk_rating_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='活动评价表';

-- 7. 系统配置表
CREATE TABLE `system_config` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '配置ID',
  `config_key` varchar(100) NOT NULL COMMENT '配置键',
  `config_value` text COMMENT '配置值',
  `description` varchar(200) DEFAULT NULL COMMENT '配置描述',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_config_key` (`config_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统配置表';

-- 插入初始数据
-- 插入活动分类
INSERT INTO `activity_category` (`name`, `description`, `icon`, `sort_order`) VALUES
('体育运动', '篮球、足球、乒乓球等体育类活动', '🏀', 1),
('学术竞赛', '编程、数学、英语等学术类竞赛', '📚', 2),
('文艺活动', '音乐、舞蹈、绘画等文艺类活动', '🎨', 3),
('志愿服务', '公益、环保、支教等志愿服务', '❤️', 4),
('社会实践', '实习、调研、创业等实践活动', '💼', 5),
('创新创业', '创业比赛、创新项目等活动', '💡', 6),
('其他', '其他类型活动', '📋', 7);

-- 插入系统配置
INSERT INTO `system_config` (`config_key`, `config_value`, `description`) VALUES
('app_name', '校园活动报名', '应用名称'),
('app_version', '1.0.0', '应用版本'),
('max_upload_size', '5242880', '最大上传文件大小（字节）'),
('max_activity_images', '5', '活动最大图片数量'),
('max_activity_tags', '5', '活动最大标签数量'),
('default_avatar', 'https://i.pravatar.cc/150', '默认头像URL');

-- 插入测试用户数据
INSERT INTO `user` (`openid`, `nickname`, `avatar_url`, `phone`, `email`, `bio`) VALUES
('test_openid_001', '张三', 'https://i.pravatar.cc/150?img=1', '13800138001', 'zhangsan@example.com', '热爱运动，喜欢参加各种校园活动'),
('test_openid_002', '李四', 'https://i.pravatar.cc/150?img=2', '13800138002', 'lisi@example.com', '编程爱好者，喜欢技术交流'),
('test_openid_003', '王五', 'https://i.pravatar.cc/150?img=3', '13800138003', 'wangwu@example.com', '文艺青年，喜欢音乐和绘画');

-- 插入测试活动数据
INSERT INTO `activity` (`title`, `description`, `category_id`, `organizer_id`, `start_date`, `start_time`, `location`, `max_participants`, `deadline`, `fee`, `requirements`, `schedule`, `rewards`, `contact`, `images`, `tags`) VALUES
('校园篮球友谊赛', '欢迎所有篮球爱好者参加本次友谊赛！比赛分为男子组和女子组，采用循环赛制。', 1, 1, '2024-01-15', '14:00:00', '学校体育馆篮球场', 20, '2024-01-10 23:59:00', 0.00, '1. 身体健康，无心脏病等不适合剧烈运动的疾病\n2. 具备基本的篮球技能\n3. 自备运动装备\n4. 遵守比赛规则和体育道德', '14:00-14:30 开幕式\n14:30-16:30 小组赛\n16:30-17:00 休息\n17:00-18:30 决赛\n18:30-19:00 颁奖仪式', '冠军：奖金1000元 + 奖杯\n亚军：奖金500元 + 奖牌\n季军：奖金300元 + 奖牌\n所有参赛者：纪念品一份', '联系人：体育部张老师\n电话：13800138000\n微信：sports_zhang', '["https://picsum.photos/750/400?random=1", "https://picsum.photos/750/400?random=11"]', '["篮球", "运动", "比赛"]'),
('编程竞赛', 'ACM编程竞赛，面向全校学生开放。比赛采用在线编程形式，题目涵盖算法、数据结构、数学等多个领域。', 2, 2, '2024-01-20', '09:00:00', '计算机学院机房', 30, '2024-01-12 23:59:00', 50.00, '1. 具备基本的编程能力\n2. 熟悉至少一种编程语言（C/C++/Java/Python）\n3. 自备笔记本电脑\n4. 遵守考试纪律', '09:00-09:30 签到和准备\n09:30-12:30 正式比赛\n12:30-13:30 午餐休息\n13:30-14:00 成绩公布和颁奖', '一等奖：奖金2000元 + 证书\n二等奖：奖金1000元 + 证书\n三等奖：奖金500元 + 证书\n优秀奖：精美礼品 + 证书', '联系人：计算机学院李老师\n电话：13900139000\n邮箱：li@university.edu', '["https://picsum.photos/750/400?random=2"]', '["编程", "竞赛", "学习"]'),
('摄影作品展', '展示校园美景和同学们的生活瞬间，优秀作品将获得奖励。', 3, 3, '2024-01-25', '10:00:00', '艺术楼展厅', 25, '2024-01-08 23:59:00', 0.00, '1. 作品必须为原创\n2. 主题积极向上\n3. 提交电子版作品\n4. 作品格式为JPG或PNG', '10:00-10:30 开幕式\n10:30-12:00 作品展示\n12:00-13:00 午餐休息\n13:00-15:00 评委评分\n15:00-16:00 颁奖仪式', '一等奖：奖金800元 + 证书\n二等奖：奖金500元 + 证书\n三等奖：奖金300元 + 证书\n优秀奖：精美礼品 + 证书', '联系人：艺术学院王老师\n电话：13700137000\n微信：art_wang', '["https://picsum.photos/750/400?random=3"]', '["摄影", "艺术", "展览"]');
