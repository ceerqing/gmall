package com.atguigu.gmall.web.controller;

import com.atguigu.feigin.client.item.SkuDetailFeignClient;
import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.to.SkuDetailTo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Author：张世平
 * Date：2022/8/29 14:45
 */
@Api("商品详情")
@Controller
public class ItemController {

    @Autowired
    SkuDetailFeignClient skuDetailFeignClient;

    @ApiOperation("查询出在录入一个sku的详细信息")
    @GetMapping("/{skuId}.html")
    public String getSkuItem(@PathVariable("skuId") Long skuId,
                             Model model){
        Result<SkuDetailTo> result = skuDetailFeignClient.getSkuDetail(skuId);

        if(result.isOk()){
            SkuDetailTo skuDetailTo = result.getData();
            if (skuDetailTo==null||skuDetailTo.getSkuInfo()==null){
                return "item/404";
            }
            model.addAttribute("categoryView",skuDetailTo.getCategoryView());
            model.addAttribute("skuInfo",skuDetailTo.getSkuInfo());
            model.addAttribute("price",skuDetailTo.getPrice());
            model.addAttribute("spuSaleAttrList",skuDetailTo.getSpuSaleAttrList());//spu的销售属性列表
            model.addAttribute("valuesSkuJson",skuDetailTo.getValuesSkuJson());//json
        }
        return "item/index";
    }
}
