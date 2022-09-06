package com.atguigu.gmall.product.init;

import com.atguigu.gmall.common.constant.SysRedisConstant;
import com.atguigu.gmall.product.service.SkuInfoService;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.LICENSE;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * Author：张世平
 * Date：2022/9/6 11:20
 */
@Service
@Slf4j
public class BloomSkuInfo {
    @Autowired
    RedissonClient redissonClient;

    @Autowired
    SkuInfoService skuInfoService;

    @PostConstruct
    public void initSkuInfoBloom(){
        //查询出所有sku的所有id设置到布隆过滤器中
        log.info("sku布隆过滤器初始化");
        List<Long> ids = skuInfoService.findAllSkuId();

        RBloomFilter<Object> skuBloom = redissonClient.getBloomFilter(SysRedisConstant.SKU_BLOOM);

        boolean exists = skuBloom.isExists();
        if (!exists) {
            skuBloom.tryInit(5000000, 0.00001);
        }

        ids.forEach(id -> skuBloom.add(id));

        log.info("布隆过滤器初始化完成");
    }
}
