package com.atguigu.gmall.product.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.product.BaseSaleAttr;
import com.atguigu.gmall.model.product.SpuImage;
import com.atguigu.gmall.model.product.SpuSaleAttr;
import com.atguigu.gmall.product.service.BaseSaleAttrService;
import com.atguigu.gmall.product.service.SpuImageService;
import com.atguigu.gmall.product.service.SpuSaleAttrService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Author：张世平
 * Date：2022/8/28 18:46
 */
@RestController
@RequestMapping("/admin/product")
@Api(tags = "平台属性的操作")
public class BaseSaleAttrController {

    ///baseSaleAttrList
    //获取平台给商品赋予的所有属性名
    @Autowired
    BaseSaleAttrService baseSaleAttrService;

    @Autowired
    SpuSaleAttrService spuSaleAttrService;

    @Autowired
    SpuImageService spuImageService;

    @GetMapping("/baseSaleAttrList")
    @ApiOperation("获取spu可以设置的属性")
    public Result baseSaleAttrList(){
        List<BaseSaleAttr> attrList =baseSaleAttrService.list();
        return Result.ok(attrList);
    }

    @ApiOperation("根据supId获取该sup下所有的属性名和对应的属性值")

    @GetMapping("/spuSaleAttrList/{spuId}")
    public Result getAttrAndValueBySpuId(@PathVariable("spuId") Long supId){
       List<SpuSaleAttr> attrAndValueList=spuSaleAttrService.getAttrAndValueBySpuId(supId);
        return Result.ok(attrAndValueList);
    }

    @ApiOperation("根据supId获取对应spu的图片")
    @GetMapping("/spuImageList/{spuId}")
    public Result getAttrImageListBySpuID(@PathVariable("spuId") Long supId){
        QueryWrapper<SpuImage> wrapper = new QueryWrapper<>();
        wrapper.eq("spu_id",supId);
        List<SpuImage> spuImageList = spuImageService.list(wrapper);
        return Result.ok(spuImageList);
    }



}
