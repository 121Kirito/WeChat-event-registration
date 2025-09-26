package com.xiaoxie.wechatproject;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.xiaoxie.wechatproject.mapper")
public class WeChatprojectApplication {

    public static void main(String[] args) {
        SpringApplication.run(WeChatprojectApplication.class, args);
    }

}
