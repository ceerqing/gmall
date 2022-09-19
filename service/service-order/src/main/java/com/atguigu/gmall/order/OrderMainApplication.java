package com.atguigu.gmall.order;

import com.atguigu.gmall.common.config.feign.EnableAutoFeignInterceptor;
import com.atguigu.gmall.common.handler.EnableAutoExceptionHandler;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@EnableTransactionManagement
@MapperScan("com.atguigu.gmall.order.mapper")
@SpringCloudApplication
@EnableFeignClients(basePackages = {
        "com.atguigu.feigin.client.cart",
        "com.atguigu.feigin.client.user",
        "com.atguigu.feigin.client.product",
        "com.atguigu.feigin.client.ware"
})
@EnableAutoFeignInterceptor
@EnableAutoExceptionHandler//全局异常处理器
public class OrderMainApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderMainApplication.class,args);
    }
}
