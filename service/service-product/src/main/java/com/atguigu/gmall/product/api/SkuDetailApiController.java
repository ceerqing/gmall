package com.atguigu.gmall.product.api;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.product.SkuImage;
import com.atguigu.gmall.model.product.SkuInfo;
import com.atguigu.gmall.model.product.SpuSaleAttr;
import com.atguigu.gmall.model.to.CategoryViewTo;
import com.atguigu.gmall.model.to.SkuDetailTo;
import com.atguigu.gmall.product.mapper.SpuSaleAttrMapper;
import com.atguigu.gmall.product.service.BaseCategory3Service;
import com.atguigu.gmall.product.service.SkuImageService;
import com.atguigu.gmall.product.service.SkuInfoService;
import com.atguigu.gmall.product.service.SpuSaleAttrService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * Author：张世平
 * Date：2022/8/29 15:42
 */
@Api(tags = "sku详情信息。图片。对应的spu所拥有的sku")
@RestController
@RequestMapping("/api/inner/rpc/product")
public class SkuDetailApiController {
    @Autowired
    SkuInfoService skuInfoService;

    @Autowired
    BaseCategory3Service baseCategory3Service;

    @Autowired
    SpuSaleAttrService spuSaleAttrService;

    @Autowired
    SkuImageService skuImageService;

    /**
     * 总接口
     * @param skuId
     * @return
     */
    @GetMapping("/skudetail/{skuId}")
        public Result<SkuDetailTo> getSkuDetail(@PathVariable("skuId") Long skuId){
        //准备查询所有需要的数据
        SkuDetailTo skuDetailTo = skuInfoService.getSkuDetail(skuId);
        return Result.ok(skuDetailTo);
    }

    @GetMapping("/skudetail/category/{category3Id}")
    Result<CategoryViewTo> getCategoryView(@PathVariable("category3Id") Long category3Id){
        CategoryViewTo categoryViewTo= baseCategory3Service.getCategoryView(category3Id);
        return  Result.ok(categoryViewTo);
    }

    /**
     * 根据skuId获取一个sku的详细信息
     * @param skuId
     * @return
     */
    @GetMapping("/skudetail/info/{skuId}")
    public  Result<SkuInfo> getSkuInfo(@PathVariable("skuId")Long skuId){
       SkuInfo skuInfo= skuInfoService.getSkuInfo(skuId);
        return   Result.ok(skuInfo);
    }

    @GetMapping("/skudetail/price/{skuId}")
   public Result<BigDecimal> getPrice(@PathVariable("skuId") Long skuId){
       BigDecimal price= skuInfoService.getPrice(skuId);
       return  Result.ok(price);
    }

    /**
     * 获取一个spu的所有属性，并且当前选的时哪一个sku
     * @param spuId
     * @param skuId
     * @return
     */
    @GetMapping("/skudetail/saleattrvalues/{spuId}/{skuId}")
    public  Result<List<SpuSaleAttr>> getSkuSaleattrvalues(@PathVariable("spuId") Long spuId,
                                                   @PathVariable("skuId") Long skuId){

        List<SpuSaleAttr> saleAttrList = spuSaleAttrService.getSaleAttrAndValueMarkSku(spuId, skuId);
        return Result.ok(saleAttrList);
    }


    @GetMapping("/skudetail/images/{skuId}")
    public Result<List<SkuImage>> getSkuImages(@PathVariable("skuId")Long skuId){
        List<SkuImage> skuImage = skuImageService.getSkuImage(skuId);
        return   Result.ok(skuImage);
    }

    @GetMapping("/skudetail/json/{spuId}")
    Result<String> getValueJson(@PathVariable("spuId") Long supId){
        String spuJson = spuSaleAttrService.getSupAllSukIdAndValueId(supId);
        return Result.ok(spuJson);
    }
}
