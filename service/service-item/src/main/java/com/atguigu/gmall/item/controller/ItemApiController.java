package com.atguigu.gmall.item.controller;

import com.atguigu.gmall.common.result.Result;

import com.atguigu.gmall.item.feign.SkuFeignDetail;
import com.atguigu.gmall.model.to.SkuDetailTo;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * Author：张世平
 * Date：2022/8/29 14:44
 */
@RestController
@RequestMapping("/api/inner/rpc/item")
@Api("商品详情相关api")
public class ItemApiController {

    @Autowired
    SkuFeignDetail skuFeignDetail;

    @GetMapping("/skuDetail/{skuId}")
    public Result<SkuDetailTo> getSkuDetail(@PathVariable("skuId") Long skuId){
        Result<SkuDetailTo> skuDetail = skuFeignDetail.getSkuDetail(skuId);
        return Result.ok(skuDetail.getData());
    }
}
