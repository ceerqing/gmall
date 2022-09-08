package com.atguigu.gmall.product;

import com.atguigu.gmall.common.util.DateUtil;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.xml.crypto.Data;
import java.util.Date;

/**
 * Author：张世平
 * Date：2022/8/28 13:09
 */
//@SpringBootTest
public class MinioTest {
    @Test
    public void testDateTime() {
        String s = DateUtil.formatDate(new Date());
        System.out.println(s);
        String replace = DateUtil.formatDate(new Date()).replace("-", "/");
        System.out.println(replace);
    }
}
