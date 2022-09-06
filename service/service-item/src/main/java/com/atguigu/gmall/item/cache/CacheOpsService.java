package com.atguigu.gmall.item.cache;

import com.atguigu.gmall.model.to.SkuDetailTo;

/**
 * Author：张世平
 * Date：2022/9/4 14:54
 */
public interface CacheOpsService {

   <T> T getCacheData(Long skuId, Class<T> cls);

    void setNullValue(Long skuId);

    void saveCacheData(Long skuId, Object skuDetailRpc);

    void unLock(Long skuId);

    boolean tryLock(Long skuId);

    boolean boolmContain(Long skuId);
}
