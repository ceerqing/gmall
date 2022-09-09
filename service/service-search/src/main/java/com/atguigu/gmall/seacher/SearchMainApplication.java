package com.atguigu.gmall.seacher;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

/**
 * Author：张世平
 * Date：2022/9/9 8:35
 */
@EnableElasticsearchRepositories //开启es的自动仓库功能
@SpringCloudApplication
public class SearchMainApplication {
    public static void main(String[] args) {
        SpringApplication.run(SearchMainApplication.class,args);
    }
}
