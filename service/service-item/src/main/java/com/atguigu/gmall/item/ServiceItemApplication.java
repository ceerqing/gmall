package com.atguigu.gmall.item;

import com.atguigu.gmall.common.config.threadpoolconfig.EnableThreadPool;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Author：张世平
 * Date：2022/8/29 14:43
 */

@SpringCloudApplication
@EnableThreadPool
@EnableFeignClients(basePackages = {
        "com.atguigu.feigin.client.product",
         "com.atguigu.feigin.client.search"

})
public class ServiceItemApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceItemApplication.class,args);
    }
}
