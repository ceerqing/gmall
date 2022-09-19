package com.atguigu.gmall.web;

import com.atguigu.gmall.common.config.feign.EnableAutoFeignInterceptor;
import org.springframework.boot.SpringApplication;

import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Author：张世平
 * Date：2022/8/29 1:25
 */
@SpringCloudApplication
@EnableFeignClients(basePackages = {
        "com.atguigu.feigin.client"
})
@EnableAutoFeignInterceptor

public class WebAllMainApplication {
    public static void main(String[] args) {
        SpringApplication.run(WebAllMainApplication.class,args);
    }
}
