package com.atguigu.gmall.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * Author：张世平
 * Date：2022/9/1 20:36
 */
@SpringBootTest
public class TestThreadPool {
    @Autowired
    ThreadPoolExecutor poolExecutor;
    @Test
    public void test() {
        for (int i = 0; i < 20; i++) {
            poolExecutor.submit(()->{
                System.out.println(Thread.currentThread().getName());
            });
        }
    }
}
