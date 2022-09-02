package com.atguigu.gmall.product.service;

import com.atguigu.gmall.model.product.SkuInfo;
import com.atguigu.gmall.model.to.SkuDetailTo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;

/**
* @author 张世平哒
* @description 针对表【sku_info(库存单元表)】的数据库操作Service
* @createDate 2022-08-27 09:53:41
*/
public interface SkuInfoService extends IService<SkuInfo> {

    /**
     * 保存spu对应的一个sku
     * @param skuInfo
     */
    void saveSkuInfo(SkuInfo skuInfo);

    /**
     * 上架或者下架商品
     * @param skuId
     * @param flag
     */
    void isSale(Long skuId, int flag);

    /**
     * 根据skuId获取其详细信息，图片
     * @param skuId
     * @return
     */
    SkuDetailTo getSkuDetail(Long skuId);

    SkuInfo getSkuInfo(Long skuId);

    BigDecimal getPrice(Long skuId);
}
