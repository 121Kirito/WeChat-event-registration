package com.xiaoxie.wechatproject.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiaoxie.wechatproject.entity.ActivityCategory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 活动分类Mapper接口
 */
@Mapper
public interface ActivityCategoryMapper extends BaseMapper<ActivityCategory> {
    
    /**
     * 查询所有启用的分类
     */
    @Select("SELECT * FROM activity_category WHERE status = 1 ORDER BY sort_order ASC, create_time ASC")
    List<ActivityCategory> selectAllEnabled();
}
