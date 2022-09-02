package com.atguigu.gmall.item.feign;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.product.SkuImage;
import com.atguigu.gmall.model.product.SkuInfo;
import com.atguigu.gmall.model.product.SpuSaleAttr;

import com.atguigu.gmall.model.to.CategoryViewTo;
import com.atguigu.gmall.model.to.SkuDetailTo;

import org.springframework.cloud.openfeign.FeignClient;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.util.List;

/**
 * Author：张世平
 * Date：2022/8/29 15:20
 */
@FeignClient("service-product")
@RequestMapping("/api/inner/rpc/product")
public interface SkuFeignDetail {


   @GetMapping("/skudetail/detail/{skuId}")  //不要用这个超级接口
   Result<SkuDetailTo> getSkuDetail(@PathVariable("skuId") Long skuId);


   @GetMapping("/skudetail/category/{category3Id}")
   Result<CategoryViewTo> getCategoryView(@PathVariable("category3Id") Long category3Id);

   @GetMapping("/skudetail/info/{skuId}")
   Result<SkuInfo> getSkuInfo(@PathVariable("skuId")Long skuId);

   @GetMapping("/skudetail/price/{skuId}")
   Result<BigDecimal> getPrice(@PathVariable("skuId") Long skuId);

   @GetMapping("/skudetail/saleattrvalues/{spuId}/{skuId}")
   Result<List<SpuSaleAttr>> getSkuSaleattrvalues(@PathVariable("spuId") Long spuId,
                                                  @PathVariable("skuId") Long skuId);


   @GetMapping("/skudetail/images/{skuId}")
   Result<List<SkuImage>> getSkuImages(@PathVariable("skuId")Long skuId);



   @GetMapping("/skudetail/json/{spuId}")
   Result<String> getValueJson(@PathVariable("spuId") Long supId);



}
