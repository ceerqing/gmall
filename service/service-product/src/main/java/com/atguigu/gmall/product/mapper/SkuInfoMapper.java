package com.atguigu.gmall.product.mapper;

import com.atguigu.gmall.model.product.SkuInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
* @author 张世平哒
* @description 针对表【sku_info(库存单元表)】的数据库操作Mapper
* @createDate 2022-08-27 09:53:41
* @Entity com.atguigu.gmall.product.domain.SkuInfo
*/
public interface SkuInfoMapper extends BaseMapper<SkuInfo> {

    /**
     * 上架或者下架商品
     * @param skuId
     * @param flag
     */
    void isSale(@Param("skuId") Long skuId, @Param("flag") int flag);

    BigDecimal getNowPrice(@Param("skuId") Long skuId);

    List<Long> findAllSkuId();
}




