package com.atguigu.gmall.product.service.impl;

import com.alibaba.fastjson.JSON;
import com.atguigu.gmall.common.util.Jsons;
import com.atguigu.gmall.model.product.SpuSaleAttr;
import com.atguigu.gmall.model.to.ValueSkuJsonTo;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.gmall.product.service.SpuSaleAttrService;
import com.atguigu.gmall.product.mapper.SpuSaleAttrMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* @author 张世平哒
* @description 针对表【spu_sale_attr(spu销售属性)】的数据库操作Service实现
* @createDate 2022-08-27 09:53:41
*/
@Service
public class SpuSaleAttrServiceImpl extends ServiceImpl<SpuSaleAttrMapper, SpuSaleAttr>
    implements SpuSaleAttrService{

    @Resource
    SpuSaleAttrMapper spuSaleAttrMapper;

    /**
     * 根据spuId获取所有的属性和属性值
     * @param supId
     * @return
     */
    @Override
    public List<SpuSaleAttr> getAttrAndValueBySpuId(Long supId) {
        return spuSaleAttrMapper.getAttrAndValueBySpuId(supId);
    }

    @Override
    public List<SpuSaleAttr> getSaleAttrAndValueMarkSku(Long spuId, Long skuId) {
        return spuSaleAttrMapper.getSaleAttrAndValueMarkSku(spuId,skuId);
    }

    @Override
    public String getSupAllSukIdAndValueId(Long spuId) {
      List<ValueSkuJsonTo>  skuJsonTo=spuSaleAttrMapper.getSupAllSukIdAndValueId(spuId);
        Map<String,Long> valuesId=new HashMap<>();
        skuJsonTo.forEach(e->{
            valuesId.put(e.getValueJson(),e.getId());
        });
        String attrJsonValues = Jsons.toStr(valuesId);
        return attrJsonValues;
    }
}




