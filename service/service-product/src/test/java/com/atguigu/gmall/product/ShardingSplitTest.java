package com.atguigu.gmall.product;

import com.atguigu.gmall.model.product.BaseTrademark;
import com.atguigu.gmall.product.mapper.BaseTrademarkMapper;
import org.apache.shardingsphere.infra.hint.HintManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Author：张世平
 * Date：2022/9/8 16:04
 */
@SpringBootTest
public class ShardingSplitTest {

    @Autowired
    BaseTrademarkMapper baseTrademarkMapper;

    @Test
    public void test() {
        BaseTrademark baseTrademark = baseTrademarkMapper.selectById(15);
        System.out.println("baseTrademark = " + baseTrademark);

        BaseTrademark baseTrademark2 = baseTrademarkMapper.selectById(15);
        System.out.println("baseTrademark2 = " + baseTrademark2);

        HintManager.getInstance().setWriteRouteOnly();//要求下面面的操作从主库中获取
    }
}
