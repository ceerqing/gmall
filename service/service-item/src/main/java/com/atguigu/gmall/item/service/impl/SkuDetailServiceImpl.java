package com.atguigu.gmall.item.service.impl;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.item.feign.SkuFeignDetail;
import com.atguigu.gmall.item.service.SkuDetailService;
import com.atguigu.gmall.model.product.SkuImage;
import com.atguigu.gmall.model.product.SkuInfo;
import com.atguigu.gmall.model.product.SpuSaleAttr;
import com.atguigu.gmall.model.to.CategoryViewTo;
import com.atguigu.gmall.model.to.SkuDetailTo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Author：张世平
 * Date：2022/9/1 11:49
 */
@Service
public class SkuDetailServiceImpl implements SkuDetailService {

    @Autowired
    SkuFeignDetail skuFeignDetail;

    @Autowired
    ThreadPoolExecutor executor;

    @Override
    public SkuDetailTo getSkuDetail(Long skuId) {
        SkuDetailTo skuDetailTo = new SkuDetailTo();

        //查询s   ku的详细信心
        CompletableFuture<SkuInfo> skuInfoFuture = CompletableFuture.supplyAsync(() -> {
            Result<SkuInfo> skuInfo = skuFeignDetail.getSkuInfo(skuId);
            skuDetailTo.setSkuInfo(skuInfo.getData());
            return skuInfo.getData();
        }, executor);

        //三级分类
        CompletableFuture<Void> cateGoryFuture = skuInfoFuture.thenAcceptAsync(skuInfo -> {
            Result<CategoryViewTo> categoryView = skuFeignDetail
                    .getCategoryView(skuInfo.getCategory3Id());
            skuDetailTo.setCategoryView(categoryView.getData());
        }, executor);

        //查询图片
        CompletableFuture<Void> imageFuture = skuInfoFuture.thenAcceptAsync(skuInfo -> {
            Result<List<SkuImage>> skuImages = skuFeignDetail.getSkuImages(skuId);
            skuInfo.setSkuImageList(skuImages.getData());
        }, executor);

        //根据三级id查询父级分类

        CompletableFuture<Void> spuAttrFuture= skuInfoFuture.thenAcceptAsync(skuInfo -> {
            Result<List<SpuSaleAttr>> listAttr = skuFeignDetail.getSkuSaleattrvalues(skuInfo.getSpuId(), skuId);
            skuDetailTo.setSpuSaleAttrList(listAttr.getData());
        }, executor);

        //查询所有spu对应sku的skuId和属性值的id
        CompletableFuture<Void> valueJsonFuture = skuInfoFuture.thenAcceptAsync(skuInfo -> {
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

        return skuDetailTo;
    }
}
