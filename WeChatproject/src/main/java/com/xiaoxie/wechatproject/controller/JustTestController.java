package com.xiaoxie.wechatproject.controller;

import com.xiaoxie.wechatproject.dto.ApiResponse;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@CrossOrigin("*")
@RequestMapping("/Test")
public class JustTestController {


    @GetMapping
    public ApiResponse<String> test() {
        return ApiResponse.success("success!");
    }
}
