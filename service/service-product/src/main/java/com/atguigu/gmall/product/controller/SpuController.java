package com.atguigu.gmall.product.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.product.SpuInfo;
import com.atguigu.gmall.product.service.SpuInfoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.ibatis.executor.ResultExtractor;
import org.apache.ibatis.io.ResolverUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Author：张世平
 * Date：2022/8/28 15:22
 */
//http://192.168.200.1/admin/product/1/10?category3Id=
@RestController
@RequestMapping("/admin/product")
@Api(tags = "spu相关api")
public class SpuController {

    @Autowired
    SpuInfoService spuInfoService;

    @GetMapping("/{pageNum}/{pageSize}")
    @ApiOperation("根据三级分类id分页查询商品的spu")
    public Result getSpuPage(
            @PathVariable("pageNum") Long pageNum,
            @PathVariable("pageSize") Long pageSize,
            @RequestParam("category3Id") Long category3Id){

        Page<SpuInfo> page = new Page<>(pageNum,pageSize);
        QueryWrapper<SpuInfo> query = new QueryWrapper<>();

        query.eq("category3_id",category3Id);
        spuInfoService.page(page,query);
        return Result.ok(page);
    }

    //http://192.168.200.1/admin/product/saveSpuInfo
    @ApiOperation("添加一个spu")
    @PostMapping("/saveSpuInfo")
    public Result saveSpuInfo(@RequestBody
                               @ApiParam("一个sup的对应的参数") SpuInfo spuInfo){
        spuInfoService.saveSpuInfo(spuInfo);
        return Result.ok();
    }



}
