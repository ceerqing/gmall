package com.atguigu.gmall.product.service.impl;

import com.atguigu.feigin.client.search.SearchFeignClient;
import com.atguigu.gmall.common.constant.SysRedisConstant;
import com.atguigu.gmall.model.list.Goods;
import com.atguigu.gmall.model.list.SearchAttr;
import com.atguigu.gmall.model.product.*;
import com.atguigu.gmall.model.to.CategoryViewTo;
import com.atguigu.gmall.model.to.SkuDetailTo;
import com.atguigu.gmall.product.mapper.BaseCategory3Mapper;
import com.atguigu.gmall.product.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.gmall.product.mapper.SkuInfoMapper;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
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

    @Autowired
    RedissonClient redissonClient;

    @Autowired
    BaseTrademarkService baseTrademarkService;

    @Autowired
    SearchFeignClient searchFeignClient;

    @Override

    //每添加一个sku就像布隆过滤器中添加
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

//        RBloomFilter<Object> bloomFilter = redissonClient.getBloomFilter(SysRedisConstant.SKU_BLOOM);
//        //添加到布隆过滤器中
//        if (bloomFilter.isExists()){
//            bloomFilter.add(skuId);
//        }

        skuImageService.saveBatch(skuImageList);

    }

    @Override
    public void isSale(Long skuId, int flag) {
        //1 上架 0下架

        if (flag==1){
            Goods goods=getGoodInfo(skuId);
            //添加到es中
            searchFeignClient.saveGoods(goods);
        }else {
            //从es中删除
            searchFeignClient.deleteGoods(skuId);
        }
        skuInfoMapper.isSale(skuId,flag );
    }

    /**
     * 查询到页面共es检索
     * @return
     */
    private Goods getGoodInfo(Long skuId) {
        SkuInfo skuInfo = skuInfoMapper.selectById(skuId);
        Goods goods = new Goods();
        goods.setId(skuId);
        goods.setDefaultImg(skuInfo.getSkuDefaultImg());
        goods.setTitle(skuInfo.getSkuDesc());
        goods.setPrice(skuInfo.getPrice().doubleValue());
        goods.setCreateTime(new Date());
        goods.setTmId(skuInfo.getTmId());

        BaseTrademark trademark = baseTrademarkService.getById(skuInfo.getTmId());

        goods.setTmName(trademark.getTmName());
        goods.setTmLogoUrl(trademark.getLogoUrl());
        CategoryViewTo categoryView = baseCategory3Mapper.getCategoryView(skuInfo.getCategory3Id());
        goods.setCategory1Id(categoryView.getCategory1Id());
        goods.setCategory1Name(categoryView.getCategory1Name());
        goods.setCategory2Id(categoryView.getCategory2Id());
        goods.setCategory2Name(categoryView.getCategory2Name());
        goods.setCategory3Id(categoryView.getCategory3Id());
        goods.setCategory3Name(categoryView.getCategory3Name());
        goods.setHotScore(0L);

        //获取到这个sku的对应的平台销售属性
        List<SearchAttr> attrList = skuAttrValueService.getSkuAttrNameAndValue(skuId);
        goods.setAttrs(attrList);
        return goods;
    }

    /**
     * 获取一个sku的详细信息
     * @param skuId
     * @return
     */
    @Override
    @Deprecated
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

        //获取这个spu所拥有的sku的skuId和当前sku所有用的属性值id
      String skuAttrAndValueIdJson= spuSaleAttrService.getSupAllSukIdAndValueId(skuInfo.getSpuId());
        skuDetailTo.setValuesSkuJson(skuAttrAndValueIdJson);

        return skuDetailTo;
    }

    @Override
    public SkuInfo getSkuInfo(Long skuId) {
        SkuInfo skuInfo = skuInfoMapper.selectById(skuId);
        return skuInfo;
    }

    @Override
    public BigDecimal getPrice(Long skuId) {
        BigDecimal nowPrice = skuInfoMapper.getNowPrice(skuId);
        return nowPrice;
    }

    private BigDecimal get1010Price(Long skuId) {
        return skuInfoMapper.getNowPrice(skuId);
    }

    @Override
    public List<Long> findAllSkuId() {
        return skuInfoMapper.findAllSkuId();
    }
}




