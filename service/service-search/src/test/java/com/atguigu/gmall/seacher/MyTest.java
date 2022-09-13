package com.atguigu.gmall.seacher;

import org.junit.jupiter.api.Test;

/**
 * Author：张世平
 * Date：2022/9/13 11:37
 */
public class MyTest {
    @Test
    public void testStringSplit() {
        String str="trademark=4:小米";
        String[] split = str.split(":");
        System.out.println(str.split(":")[0]);
        for (String s : split) {
            System.out.println(s);
        }
    }
}
