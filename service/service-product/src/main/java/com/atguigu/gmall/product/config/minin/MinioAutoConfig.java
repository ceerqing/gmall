package com.atguigu.gmall.product.config.minin;

import io.minio.MinioClient;
import io.minio.errors.InvalidEndpointException;
import io.minio.errors.InvalidPortException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Author：张世平
 * Date：2022/8/28 12:55
 */
@Configuration
//@SpringBootConfiguration
public class MinioAutoConfig {
    @Autowired
    MinioProperties minioProperties;

    @Bean
    public MinioClient minioClient() throws Exception {
        MinioClient minioClient =
                new MinioClient(minioProperties.getEndpoint(), minioProperties.getAk(), minioProperties.getSk());

        String bucketName = minioProperties.getBucketName();
        if (!minioClient.bucketExists(bucketName)){
            minioClient.makeBucket(bucketName);
        }
        return minioClient;

    }
}
