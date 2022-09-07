package com.atguigu.mall.cache.cache.service;

import java.lang.reflect.Type;

/**
 * Author：张世平
 * Date：2022/9/4 14:54
 */
public interface CacheOpsService {

   <T> T  getCacheData(Long skuId, Class<T> cls);
   Object getCacheData(String cacheKey, Type type,Class cls);

    void setNullValue(Long skuId);

    void saveCacheData(Long skuId, Object skuDetailRpc);

    void saveCacheData(String cacheKey, Object skuDetailRpc,Long ttl);

    void unLock(Long skuId);

    void unLock(String lockName);

    boolean tryLock(Long skuId);

    boolean tryLock(String lockName);

    boolean boolmContain(Long skuId);
    boolean boolmContain(String bloomName,Object inBloomValue);
}
