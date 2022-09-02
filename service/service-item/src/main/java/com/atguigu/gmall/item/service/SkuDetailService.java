package com.atguigu.gmall.item.service;

import com.atguigu.gmall.model.to.SkuDetailTo;

/**
 * Author：张世平
 * Date：2022/9/1 11:48
 */
public interface SkuDetailService {
    SkuDetailTo getSkuDetail(Long skuId);
}
