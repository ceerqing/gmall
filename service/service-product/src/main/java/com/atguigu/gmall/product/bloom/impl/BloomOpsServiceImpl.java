package com.atguigu.gmall.product.bloom.impl;

import com.atguigu.gmall.product.bloom.BloomDataService;
import com.atguigu.gmall.product.bloom.BloomOpsService;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Author：张世平
 * Date：2022/9/6 14:36
 */
@Service
public class BloomOpsServiceImpl implements BloomOpsService {
    @Autowired
    RedissonClient redissonClient;
    @Override
    public void rebuildBloom(String bloomName, BloomDataService bloomDataService) {

        RBloomFilter<Object> oldBloom = redissonClient.getBloomFilter(bloomName);

        //重建新的布隆过滤器
        RBloomFilter<Object> newBloom = redissonClient.getBloomFilter(bloomName + "New");
        newBloom.tryInit(5000000,0.00001);

        //查询数据
        List newBloomData = bloomDataService.getBloomData();
        newBloomData.forEach(data -> newBloom.add(data));

        oldBloom.rename("older");
        newBloom.rename(bloomName);

        oldBloom.deleteAsync();

    }
}
