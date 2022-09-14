package com.atguigu.gmall.item.service.impl;

import com.alibaba.cloud.commons.lang.StringUtils;
import com.atguigu.feigin.client.product.SkuFeignDetail;
import com.atguigu.feigin.client.search.SearchFeignClient;
import com.atguigu.gmall.common.constant.SysRedisConstant;
import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.common.util.Jsons;



import com.atguigu.gmall.item.service.SkuDetailService;
import com.atguigu.gmall.model.product.SkuImage;
import com.atguigu.gmall.model.product.SkuInfo;
import com.atguigu.gmall.model.product.SpuSaleAttr;
import com.atguigu.gmall.model.to.CategoryViewTo;
import com.atguigu.gmall.model.to.SkuDetailTo;
import com.atguigu.mall.cache.cache.annotation.GmallCache;
import com.atguigu.mall.cache.cache.service.CacheOpsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Author：张世平
 * Date：2022/9/1 11:49
 */
@Service
@Slf4j
public class SkuDetailServiceImpl implements SkuDetailService {

    @Autowired
    SkuFeignDetail skuFeignDetail;

    @Autowired
    ThreadPoolExecutor executor;

    @Autowired
    StringRedisTemplate redisTemplate;

    @Autowired
    CacheOpsService cacheOpsService;

    @Autowired
    SearchFeignClient searchFeignClient;


    @Autowired
    StringRedisTemplate stringRedisTemplate;

    public SkuDetailTo getSkuDetailRpc(Long skuId) {
        SkuDetailTo skuDetailTo = new SkuDetailTo();

        AtomicBoolean flag= new AtomicBoolean(false);
        //查询s   ku的详细信心
        CompletableFuture<SkuInfo> skuInfoFuture = CompletableFuture.supplyAsync(() -> {
            Result<SkuInfo> skuInfo = skuFeignDetail.getSkuInfo(skuId);

            skuDetailTo.setSkuInfo(skuInfo.getData());
            return skuInfo.getData();
        }, executor);

        //三级分类
        CompletableFuture<Void> cateGoryFuture = skuInfoFuture.thenAcceptAsync(skuInfo -> {
            if (skuInfo==null){
                flag.set(true);
                return;
            }
            Result<CategoryViewTo> categoryView = skuFeignDetail
                    .getCategoryView(skuInfo.getCategory3Id());
            skuDetailTo.setCategoryView(categoryView.getData());
        }, executor);

        //查询图片
        CompletableFuture<Void> imageFuture = skuInfoFuture.thenAcceptAsync(skuInfo -> {
            if (skuInfo==null){
                return;
            }
            Result<List<SkuImage>> skuImages = skuFeignDetail.getSkuImages(skuId);
            skuInfo.setSkuImageList(skuImages.getData());
        }, executor);

        //根据三级id查询父级分类

        CompletableFuture<Void> spuAttrFuture= skuInfoFuture.thenAcceptAsync(skuInfo -> {
            if (skuInfo==null){
                return;
            }
            Result<List<SpuSaleAttr>> listAttr = skuFeignDetail.getSkuSaleattrvalues(skuInfo.getSpuId(), skuId);
            skuDetailTo.setSpuSaleAttrList(listAttr.getData());
        }, executor);

        //查询所有spu对应sku的skuId和属性值的id
        CompletableFuture<Void> valueJsonFuture = skuInfoFuture.thenAcceptAsync(skuInfo -> {
            if (skuInfo==null){
                return;
            }
            Result<String> valueJson = skuFeignDetail.getValueJson(skuInfo.getSpuId());
            skuDetailTo.setValuesSkuJson(valueJson.getData());
        }, executor);


        //查价格
        CompletableFuture<Void> priceFuture = CompletableFuture.runAsync(() -> {
            Result<BigDecimal> price = skuFeignDetail.getPrice(skuId);
            skuDetailTo.setPrice(price.getData());
        }, executor);



        CompletableFuture
                .allOf(priceFuture,valueJsonFuture,spuAttrFuture,imageFuture,cateGoryFuture)
                .join();

        if (flag.get()){
            return null;//不存在
        }
        return skuDetailTo;
    }

    @GmallCache(cacheKey = SysRedisConstant.SKU_INFO_CACHE_KEY+"#{#params[0]}",
            bloomName = SysRedisConstant.SKU_BLOOM,
            bloomValue = "#{#params[0]}",
            lockName = SysRedisConstant.SKU_LOCK+"#{#params[0]}",
            ttl = 60*60*24*7L,
            cls = SkuDetailTo.class
    )
    @Override
    public SkuDetailTo getSkuDetail(Long skuId) {

        SkuDetailTo skuDetailRpc = getSkuDetailRpc(skuId);
        return skuDetailRpc;
    }

    @Override
    public void updateHotScore(Long skuId) {
        Long increment = stringRedisTemplate.opsForValue()
                .increment(SysRedisConstant.SKU_HOTSCORE + skuId);

        if (increment % 100 == 0) {
            searchFeignClient.updateSkuHotScore(skuId, increment);
        }
    }


    public SkuDetailTo getSkuDetail2(Long skuId) {
        //查缓存
       SkuDetailTo skuDetailTo= cacheOpsService.getCacheData(skuId,SkuDetailTo.class);

       if (skuDetailTo == null){

           //不命中。先过布隆过滤器，在回源
           boolean isInBoolm = cacheOpsService.boolmContain(skuId);
           if (isInBoolm){
               //布隆过滤器判断可能存在加锁
               log.info("{}没有命中缓存要回源布隆过滤器判断存在，可能存在恶意攻击....",skuId);
               boolean isGetLock=cacheOpsService.tryLock(skuId);
               if (isGetLock){
                   log.info("id为{}商品正在回源操作....",skuId);
                   //回源,可能存在，可能不存在数据库中
                   SkuDetailTo skuDetailRpc = getSkuDetailRpc(skuId);
                   cacheOpsService.saveCacheData(skuId, skuDetailRpc);
                   //释放锁
                   cacheOpsService.unLock(skuId);
                   return skuDetailRpc;
               }else {
                   //没有得到锁
                   try {
                       Thread.sleep(1000);
                       return cacheOpsService.getCacheData(skuId,SkuDetailTo.class);
                   } catch (InterruptedException e) {
                       e.printStackTrace();
                   }
               }

           }
       }

        return skuDetailTo;
    }

    public SkuDetailTo getSkuDetailss(Long skuId) {
        //先查缓存。缓存不存在，回源查询
        String skuInfoTo = redisTemplate.opsForValue().get("sku:info:" + skuId);
        if ("x".equals(skuInfoTo)){
            //不存在这个商品
            return null;
        }
        //不存在
        if (StringUtils.isEmpty(skuInfoTo)){
            SkuDetailTo skuDetailRpc = getSkuDetailRpc(skuId);
            if (skuDetailRpc != null){
                //存入redis缓存
                redisTemplate.opsForValue().set("sku:info:"+skuId, Jsons.toStr(skuDetailRpc));
                return skuDetailRpc;
            }else {
                //回源查询发现不存在这个数据，防止缓存恶意的缓存穿透攻击
                redisTemplate.opsForValue().set("sku:info:"+skuId,"x");
                return null;
            }

        }else {
            //缓存中存在这个数据
            SkuDetailTo skuDetailTo = Jsons.toObject(skuInfoTo, SkuDetailTo.class);
            return skuDetailTo;
        }

    }
}
