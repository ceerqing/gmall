package com.atguigu.gmall.product.service;

import com.atguigu.gmall.model.product.SpuSaleAttr;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author 张世平哒
* @description 针对表【spu_sale_attr(spu销售属性)】的数据库操作Service
* @createDate 2022-08-27 09:53:41
*/
public interface SpuSaleAttrService extends IService<SpuSaleAttr> {

    /**
     * 根据spuId获取对应的所有属性和属性值
     * @param supId
     * @return
     */
    List<SpuSaleAttr> getAttrAndValueBySpuId(Long supId);

    List<SpuSaleAttr> getSaleAttrAndValueMarkSku(Long spuId, Long skuId);

    /**
     * 根据spuId获取这个spu所有的sku的id和销售属性的属性值id
     * @param spuId
     * @return
     */
    String getSupAllSukIdAndValueId(Long spuId);
}
