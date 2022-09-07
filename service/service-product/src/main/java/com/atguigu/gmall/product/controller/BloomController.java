package com.atguigu.gmall.product.controller;

import com.atguigu.gmall.common.constant.SysRedisConstant;
import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.product.bloom.BloomOpsService;
import com.atguigu.gmall.product.bloom.impl.SkuBloomDataServiceImpl;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Author：张世平
 * Date：2022/9/6 14:29
 */
@RestController
@RequestMapping("/rebuild")
public class BloomController {

    @Autowired
    BloomOpsService bloomOpsService;

    @Autowired
    SkuBloomDataServiceImpl skuBloomDataService;

    @ApiOperation("重建sku布隆过滤器")
    @GetMapping("/sku")
    public Result rebuildSkuBloom(){
        String bloomName= SysRedisConstant.SKU_BLOOM;
        bloomOpsService.rebuildBloom(bloomName,skuBloomDataService);
        return Result.ok();
    }

    //重建订单布隆过滤器
}
