package com.atguigu.gmall.product.service.impl;

import com.atguigu.gmall.model.product.SpuImage;
import com.atguigu.gmall.model.product.SpuInfo;
import com.atguigu.gmall.model.product.SpuSaleAttr;
import com.atguigu.gmall.model.product.SpuSaleAttrValue;
import com.atguigu.gmall.product.service.SpuImageService;
import com.atguigu.gmall.product.service.SpuSaleAttrService;
import com.atguigu.gmall.product.service.SpuSaleAttrValueService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.gmall.product.service.SpuInfoService;
import com.atguigu.gmall.product.mapper.SpuInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
* @author 张世平哒
* @description 针对表【spu_info(商品表)】的数据库操作Service实现
* @createDate 2022-08-27 09:53:41
*/
@Service
public class SpuInfoServiceImpl extends ServiceImpl<SpuInfoMapper, SpuInfo>
    implements SpuInfoService{

    @Resource
    SpuInfoMapper spuInfoMapper;

    @Autowired
    SpuImageService imageService;

    @Autowired
    SpuSaleAttrService spuSaleAttrService;

    @Autowired
    SpuSaleAttrValueService spuSaleAttrValueService;

    @Override
    @Transactional
    public void saveSpuInfo(SpuInfo spuInfo) {
        spuInfoMapper.insert(spuInfo);

        Long spuId = spuInfo.getId();

        List<SpuImage> spuImageList = spuInfo.getSpuImageList();

        for (SpuImage spuImage : spuImageList) {
            spuImage.setSpuId(spuId);
        }
        //批量插入到spuImage表中
        imageService.saveBatch(spuImageList);

        //一个spu对应的所有属性名列表
        List<SpuSaleAttr> attrList = spuInfo.getSpuSaleAttrList();

        for (SpuSaleAttr spuSaleAttr : attrList) {
            spuSaleAttr.setSpuId(spuId);
            String saleAttrName = spuSaleAttr.getSaleAttrName();
            //获取一个属性名下面所有的属性值
            List<SpuSaleAttrValue> attrValueList = spuSaleAttr.getSpuSaleAttrValueList();
            for (SpuSaleAttrValue spuSaleAttrValue : attrValueList) {
                //设置新插入一个spu对应的id
                spuSaleAttrValue.setSpuId(spuId);
                //将对应的属性名设置给attr_value表
                spuSaleAttrValue.setSaleAttrValueName(saleAttrName);
            }
            //批量保存属性名对应的所有属性值
            spuSaleAttrValueService.saveBatch(attrValueList);
        }
        spuSaleAttrService.saveBatch(attrList);
    }
}




