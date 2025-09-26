package com.xiaoxie.wechatproject.controller;


import com.xiaoxie.wechatproject.dto.ApiResponse;
import com.xiaoxie.wechatproject.entity.ActivityCategory;
import com.xiaoxie.wechatproject.mapper.ActivityCategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@CrossOrigin(origins = "*")
public class CategoryController {

    @Autowired
    private ActivityCategoryMapper activityCategoryMapper;
    @GetMapping
    public ApiResponse<List<ActivityCategory>> queryAll(){
       try {
           List<ActivityCategory> activityCategories = activityCategoryMapper.selectAllEnabled();
           if(activityCategories!=null){
               return  ApiResponse.success("获取成功",activityCategories);
           }else {
               return ApiResponse.error("获取失败");
           }
       }catch (Exception e){
           return ApiResponse.error("获取失败"+e.getMessage());
       }
    }

}
