package com.atguigu.gmall.product.api;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.to.SkuDetailTo;
import com.atguigu.gmall.product.service.SkuInfoService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Author：张世平
 * Date：2022/8/29 15:42
 */
@Api(tags = "spu详情")
@RestController
@RequestMapping("/api/inner/rpc/product")
public class SkuDetailApiController {
    @Autowired
    SkuInfoService skuInfoService;
    @GetMapping("/skuDetail/{skuId}")
        public Result<SkuDetailTo> getSkuDetail(@PathVariable("skuId") Long skuId){
        //准备查询所有需要的数据
        SkuDetailTo skuDetailTo = skuInfoService.getSkuDetail(skuId);
        return Result.ok(skuDetailTo);
    }
}
