package com.atguigu.gmall.product.service.impl;

import com.atguigu.gmall.model.product.SkuAttrValue;
import com.atguigu.gmall.model.product.SkuImage;
import com.atguigu.gmall.model.product.SkuInfo;
import com.atguigu.gmall.model.product.SkuSaleAttrValue;
import com.atguigu.gmall.product.service.SkuAttrValueService;
import com.atguigu.gmall.product.service.SkuImageService;
import com.atguigu.gmall.product.service.SkuSaleAttrValueService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.gmall.product.service.SkuInfoService;
import com.atguigu.gmall.product.mapper.SkuInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
* @author 张世平哒
* @description 针对表【sku_info(库存单元表)】的数据库操作Service实现
* @createDate 2022-08-27 09:53:41
*/
@Service
public class SkuInfoServiceImpl extends ServiceImpl<SkuInfoMapper, SkuInfo>
    implements SkuInfoService{

    @Autowired
    SkuAttrValueService skuAttrValueService;

    @Autowired
    SkuSaleAttrValueService skuSaleAttrValueService;

    @Autowired
    SkuImageService skuImageService;

    @Resource
    SkuInfoMapper skuInfoMapper;

    @Override
    public void saveSkuInfo(SkuInfo skuInfo) {
        //保存到sku_info表中
        save(skuInfo);

        Long skuId = skuInfo.getId();
        //保存sku对应平台属性名和属性值
        List<SkuAttrValue> skuAttrValueList = skuInfo.getSkuAttrValueList();

        for (SkuAttrValue skuAttrValue : skuAttrValueList) {
            skuAttrValue.setSkuId(skuId);
        }

        skuAttrValueService.saveBatch(skuAttrValueList);

        //sku对应销售属性对应的属性名和属性值

        List<SkuSaleAttrValue> saleAttrValueList = skuInfo.getSkuSaleAttrValueList();
        for (SkuSaleAttrValue skuSaleAttrValue : saleAttrValueList) {
            skuSaleAttrValue.setSkuId(skuId);
            skuSaleAttrValue.setSpuId(skuInfo.getSpuId());
        }

        skuSaleAttrValueService.saveBatch(saleAttrValueList);
        //保存sku对应的图片

        List<SkuImage> skuImageList = skuInfo.getSkuImageList();
        for (SkuImage skuImage : skuImageList) {
            skuImage.setSkuId(skuId);
        }

        skuImageService.saveBatch(skuImageList);

    }

    @Override
    public void isSale(Long skuId, int flag) {
        skuInfoMapper.isSale(skuId,flag );
    }
}




