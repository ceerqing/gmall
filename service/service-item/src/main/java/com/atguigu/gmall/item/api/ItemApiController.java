package com.atguigu.gmall.item.api;

import com.atguigu.gmall.common.result.Result;

import com.atguigu.gmall.item.service.SkuDetailService;
import com.atguigu.gmall.model.to.SkuDetailTo;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
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
    SkuDetailService skuDetailService;


     @GetMapping("/skudetail/{skuId}")
    public Result<SkuDetailTo> getSkuDetail(@PathVariable("skuId") Long skuId){
        //Result<SkuDetailTo> skuDetail = skuFeignDetail.getSkuDetail(skuId);

       SkuDetailTo skuDetailTo =  skuDetailService.getSkuDetail(skuId);;
        return Result.ok(skuDetailTo);
    }
}
