package com.atguigu.gmall.product;

import com.atguigu.gmall.common.config.RedissonAutoConfiguration;
import com.atguigu.gmall.common.config.Swagger2Config;
import jdk.nashorn.internal.ir.annotations.Immutable;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Import;

/**
 * Author：张世平
 * Date：2022/8/26 0:01
 */
@MapperScan("com.atguigu.gmall.product.mapper")
@SpringCloudApplication
@Import({Swagger2Config.class, RedissonAutoConfiguration.class})
@EnableFeignClients
public class ProductMainApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProductMainApplication.class,args);
    }
}
