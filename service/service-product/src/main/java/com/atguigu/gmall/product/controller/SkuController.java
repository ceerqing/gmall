package com.atguigu.gmall.product.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.product.SkuInfo;
import com.atguigu.gmall.product.service.SkuInfoService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.executor.ResultExtractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Author：张世平
 * Date：2022/8/28 19:30
 */
@RestController
@RequestMapping("/admin/product")
@Api(tags = "一个spu所有的sku的操作")
public class SkuController {

    @Autowired
    SkuInfoService skuInfoService;
    @ApiOperation("分页查询sku")
    @GetMapping("/list/{pageNum}/{pageSize}")
    public Result getSkuList(@PathVariable("pageNum")Long pageNum,
                             @PathVariable("pageSize")Long pageSize){
        Page<SkuInfo> skuInfoPage = new Page<>(pageNum, pageSize);
        Page<SkuInfo> page = skuInfoService.page(skuInfoPage);
        return Result.ok(page);
    }
    ///saveSkuInfo
    @ApiOperation("保存一个spu对应的sku")
    @PostMapping("/saveSkuInfo")
    public Result saveSkuInfo(@RequestBody SkuInfo skuInfo){
        skuInfoService.saveSkuInfo(skuInfo);
        return Result.ok();
    }

    ///onSale/48
    @ApiOperation("上架商品")
    @GetMapping("/onSale/{skuId}")
    public Result  onSale(@PathVariable("skuId") Long skuId){
        skuInfoService.isSale(skuId,1);
        return Result.ok();
    }


    @ApiOperation("下架商品")
    @GetMapping("/cancelSale/{skuId}")
    public Result  cancelSale(@PathVariable("skuId") Long skuId){
        skuInfoService.isSale(skuId,0);
        return Result.ok();
    }

    @ApiOperation("根据id删除sku")
    @GetMapping("/delete/{skuId}")
    public Result deleteDSkuById(@PathVariable("skuId") Long skuId){
        skuInfoService.removeById(skuId);
        return Result.ok();
    }

}
