package com.atguigu.gmall.item.api;

import com.atguigu.gmall.common.config.RedisConfig;
import com.atguigu.gmall.common.constant.SysRedisConstant;
import com.atguigu.gmall.common.result.Result;

import com.atguigu.gmall.item.service.SkuDetailService;
import com.atguigu.gmall.model.to.SkuDetailTo;
import io.lettuce.core.RedisClient;
import io.swagger.annotations.Api;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.sleuth.instrument.reactor.ReactorSleuth;
import org.springframework.web.bind.annotation.*;


/**
 * Author：张世平
 * Date：2022/8/29 14:44
 */
@RestController
@RequestMapping("/api/inner/rpc/item")
@Api("商品详情相关api")
public class ItemApiController {

    //调用service-product的服务功能
//    @Autowired
//    SkuFeignDetail skuFeignDetail;

    @Autowired
    RedissonClient redissonClient;
    @Autowired
    SkuDetailService skuDetailService;


    @GetMapping("/skudetail/{skuId}")
    public Result<SkuDetailTo> getSkuDetail(@PathVariable("skuId") Long skuId) {
        //Result<SkuDetailTo> skuDetail = skuFeignDetail.getSkuDetail(skuId);

        SkuDetailTo skuDetailTo = skuDetailService.getSkuDetail(skuId);

        skuDetailService.updateHotScore(skuId);
        return Result.ok(skuDetailTo);
    }

    @GetMapping("/bloom/get/{skuId}")
    public Result bloomTest(@PathVariable("skuId")Long skuId){
        RBloomFilter<Object> bloomFilter = redissonClient.getBloomFilter(SysRedisConstant.SKU_BLOOM);
        boolean contains = bloomFilter.contains(skuId);
        return Result.ok(skuId+"存在嘛"+contains);
    }
}
