package com.atguigu.gmall.order;

import com.atguigu.gmall.model.order.OrderInfo;
import com.atguigu.gmall.order.mapper.OrderInfoMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.sql.Wrapper;

/**
 * Author：张世平
 * Date：2022/9/17 10:16
 */
@SpringBootTest
public class ShardingTest {

    @Resource
    OrderInfoMapper orderInfoMapper;

    @Test
    public void testInsert() {
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setUserId(2l);
        orderInfo.setTotalAmount(new BigDecimal(70));
        orderInfoMapper.insert(orderInfo);
    }

    @Test
    public void testReadWriteSplit() {
        //先确定到哪一个库，再根据是查找还是增删改查进行读写分离
        QueryWrapper wrapper=new QueryWrapper<OrderInfo>();
        wrapper.eq("user_id",2l);
        OrderInfo orderInfo = orderInfoMapper.selectOne(wrapper);
        System.out.println(orderInfo);
    }
}
