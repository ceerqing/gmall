package com.atguigu.gmall.cart;

import com.atguigu.gmall.common.handler.EnableAutoExceptionHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Author：张世平
 * Date：2022/9/14 20:57
 */
@EnableAutoExceptionHandler//开启全局异常自动处理
@SpringCloudApplication
@EnableFeignClients(basePackages = {
        "com.atguigu.feigin.client.product"
})
public class CartMainApplication {
    public static void main(String[] args) {
        SpringApplication.run(CartMainApplication.class,args);
    }
}
