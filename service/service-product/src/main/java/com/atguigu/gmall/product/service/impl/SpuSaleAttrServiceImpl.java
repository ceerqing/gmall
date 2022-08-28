package com.atguigu.gmall.product.service.impl;

import com.atguigu.gmall.model.product.SpuSaleAttr;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.gmall.product.service.SpuSaleAttrService;
import com.atguigu.gmall.product.mapper.SpuSaleAttrMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

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
}




