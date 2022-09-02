package com.atguigu.gmall.item;

import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * Author：张世平
 * Date：2022/9/1 23:12
 */
public class CompleateTest {
    @Test
    public void testCompleate() throws Exception {
        System.out.println(Thread.currentThread().getName());
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 2;
        });


        Integer integer = future.get();
        System.out.println("heihei");
    }
}
