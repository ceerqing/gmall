package com.atguigu.gmall.seacher;

import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Test
    public void testMap() {
        Map map=new HashMap();
        map.put(1,1);
        map.put(2,2);
        map.put(3,3);
        List list=new ArrayList<>();
        map.forEach((key,value)->{
           list.add(value);
        });
        System.out.println(list);
    }

    @Test
    public void testList() {
        List list=new ArrayList();
        list.add(1);
        list.add(1);
        System.out.println(list.get(0));
    }
}
