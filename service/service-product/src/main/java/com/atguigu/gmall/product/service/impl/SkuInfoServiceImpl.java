package com.atguigu.gmall.product.service.impl;

import com.atguigu.gmall.model.product.*;
import com.atguigu.gmall.model.to.CategoryViewTo;
import com.atguigu.gmall.model.to.SkuDetailTo;
import com.atguigu.gmall.product.mapper.BaseCategory3Mapper;
import com.atguigu.gmall.product.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.gmall.product.mapper.SkuInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
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

    @Resource
    BaseCategory3Mapper baseCategory3Mapper;

    @Autowired
    SpuSaleAttrService spuSaleAttrService;

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

    /**
     * 获取一个sku的详细信息
     * @param skuId
     * @return
     */
    @Override
    public SkuDetailTo getSkuDetail(Long skuId) {
        SkuDetailTo skuDetailTo = new SkuDetailTo();
        //详细信息
        SkuInfo skuInfo = skuInfoMapper.selectById(skuId);
        //查询图片
        skuDetailTo.setSkuInfo(skuInfo);
        List<SkuImage> images=skuImageService.getSkuImage(skuId);
        skuInfo.setSkuImageList(images);

        //查询层级分类信息
        CategoryViewTo categoryViewTo = baseCategory3Mapper.getCategoryView(skuInfo.getCategory3Id());
        skuDetailTo.setCategoryView(categoryViewTo);
        //查询商品的实时价格
        BigDecimal price = get1010Price(skuId);
        skuDetailTo.setPrice(price);
        //(√)4、商品（sku）所属的SPU当时定义的所有销售属性名值组合（固定好顺序）。
        //          spu_sale_attr、spu_sale_attr_value
        //          并标识出当前sku到底spu的那种组合，页面要有高亮框 sku_sale_attr_value
        //查询当前sku对应的spu定义的所有销售属性名和值（固定好顺序）并且标记好当前sku属于哪一种组合
        List<SpuSaleAttr> saleAttrList = spuSaleAttrService
                .getSaleAttrAndValueMarkSku(skuInfo.getSpuId(),skuId);

        skuDetailTo.setSpuSaleAttrList(saleAttrList);


        return skuDetailTo;
    }

    private BigDecimal get1010Price(Long skuId) {
        return skuInfoMapper.getNowPrice(skuId);
    }
}




