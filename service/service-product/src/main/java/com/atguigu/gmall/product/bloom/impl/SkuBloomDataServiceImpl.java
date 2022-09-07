package com.atguigu.gmall.product.bloom.impl;

import com.atguigu.gmall.product.bloom.BloomDataService;
import com.atguigu.gmall.product.service.SkuInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Author：张世平
 * Date：2022/9/6 14:38
 */
@Service
public class SkuBloomDataServiceImpl implements BloomDataService {
    @Autowired
    SkuInfoService skuInfoService;
    @Override
    public List getBloomData() {
        return skuInfoService.findAllSkuId();
    }
}
