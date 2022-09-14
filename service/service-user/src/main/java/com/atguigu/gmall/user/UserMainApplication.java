package com.atguigu.gmall.user;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;

import org.springframework.cloud.client.SpringCloudApplication;

/**
 * Author：张世平
 * Date：2022/9/14 9:53
 */
@SpringCloudApplication
@MapperScan("com.atguigu.gmall.user.mapper")
public class UserMainApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserMainApplication.class,args);
    }
}
