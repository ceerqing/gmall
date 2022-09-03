package com.atguigu.gmall.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

/**
 * Author：张世平
 * Date：2022/9/3 14:28
 */
@SpringBootTest
public class RedisTest {

    @Autowired
    StringRedisTemplate redisTemplate;

    @Test
    void saveTestRedis(){
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        ops.set("name","张三");
        String name = ops.get("name");
        System.out.println(name);
    }
}
