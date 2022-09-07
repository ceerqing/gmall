package com.atguigu.gmall.web;

import org.springframework.boot.SpringApplication;

import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Author：张世平
 * Date：2022/8/29 1:25
 */
@SpringCloudApplication
@EnableFeignClients(basePackages = {"com.atguigu.feigin.client.item",
        "com.atguigu.feigin.client.product"})

public class WebAllMainApplication {
    public static void main(String[] args) {
        SpringApplication.run(WebAllMainApplication.class,args);
    }
}
