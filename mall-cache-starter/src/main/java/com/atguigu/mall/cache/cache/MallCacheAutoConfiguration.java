package com.atguigu.mall.cache.cache;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * Author：张世平
 * Date：2022/9/7 16:10
 */
@Configuration
@EnableAspectJAutoProxy
@AutoConfigureAfter(RedisAutoConfiguration.class)
@ComponentScan(basePackages = "com.atguigu.mall.cache.cache")
public class MallCacheAutoConfiguration {

    @Autowired
    RedisProperties redisProperties;
    @Bean
    public RedissonClient redissonClient(){
        //1、创建一个配置
        Config config = new Config();
        String host = redisProperties.getHost();
        int port = redisProperties.getPort();
        String password = redisProperties.getPassword();
        //2、指定好redisson的配置项
        config.useSingleServer()
                .setAddress("redis://"+host+":"+port)
                .setPassword(password);

        //3、创建一个 RedissonClient
        RedissonClient client = Redisson.create(config);
        //Redis url should start with redis:// or rediss:// (for SSL connection)

        return client;
    }
}
