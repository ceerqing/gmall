package com.atguigu.gmall.product.config.minin;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Author：张世平
 * Date：2022/8/28 12:52
 */
@ConfigurationProperties(prefix = "app.minio")
@Component
@Data
public class MinioProperties {
    //ConfigurationProperties
    //容器属性的读取类。这个类会去读取配置文件中的属性，赋值到对应的属性上面.
    String endpoint;
    String ak;
    String sk;
    String bucketName;
}
