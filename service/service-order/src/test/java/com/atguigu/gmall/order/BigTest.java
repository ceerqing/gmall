package com.atguigu.gmall.order;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Author：张世平
 * Date：2022/9/17 13:37
 */
public class BigTest {

    @Test
    public void test2() {
        List<BigDecimal> list=new ArrayList<>();
        list.add(new BigDecimal(1));
        list.add(new BigDecimal(1));
        list.add(new BigDecimal(1));
        BigDecimal bigDecimal = list.stream().reduce(BigDecimal::add).get();
        System.out.println(bigDecimal);
    }

}
