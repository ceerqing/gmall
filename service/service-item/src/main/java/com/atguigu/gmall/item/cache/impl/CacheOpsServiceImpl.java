package com.atguigu.gmall.item.cache.impl;

import com.atguigu.gmall.common.constant.SysRedisConstant;
import com.atguigu.gmall.common.util.Jsons;
import com.atguigu.gmall.item.cache.CacheOpsService;
import com.atguigu.gmall.model.to.SkuDetailTo;
import org.apache.commons.lang.StringUtils;
import org.redisson.Redisson;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;


/**
 * Author：张世平
 * Date：2022/9/4 14:54
 */
@Service
public class CacheOpsServiceImpl implements CacheOpsService {
    @Autowired
    RedissonClient redissonClient;

    @Autowired
    StringRedisTemplate redisTemplate;

    @Override
    public <T> T getCacheData(Long skuId, Class<T> cls) {
        String cacheKey= SysRedisConstant.SKU_INFO_CACHE_KEY+skuId;
        String json = redisTemplate.opsForValue().get(cacheKey);

        if (SysRedisConstant.SKU_NULL_VALUE.equals(json)){
            //空值缓存，不用在查询数据库了，不能返回null值
            try {
                T t = cls.newInstance();
                return t;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (StringUtils.isEmpty(json)){
            return null;//缓存中不存在
        }

        //缓存命中
        return Jsons.toObject(json,cls);
    }

    @Override
    public void setNullValue(Long skuId) {

    }

    @Override
    public void saveCacheData(Long skuId, Object skuDetailRpc) {
        String cacheKey=SysRedisConstant.SKU_INFO_CACHE_KEY+skuId;
        if (skuDetailRpc==null){
            //回源操作数据库中没有这个数据，可以操作一下布隆过滤器
            redisTemplate.opsForValue().set(cacheKey,
                    SysRedisConstant.SKU_NULL_VALUE,
                    SysRedisConstant.SKU_NULL_VALUE_TTL,
                    TimeUnit.SECONDS);
            return;
        }

        redisTemplate.opsForValue().set(cacheKey,
                Jsons.toStr(skuDetailRpc),
                SysRedisConstant.SKU_VALUE_TTL,
                TimeUnit.SECONDS);
    }

    @Override
    public void unLock(Long skuId) {
        RLock lock = redissonClient.getLock(SysRedisConstant.SKU_LOCK + skuId);
        lock.unlock();
    }

    @Override
    public boolean tryLock(Long skuId) {
        RLock lock = redissonClient.getLock(SysRedisConstant.SKU_LOCK + skuId);
        boolean isGetLock = lock.tryLock();
        return isGetLock;
    }

    @Override
    public boolean boolmContain(Long skuId) {
        RBloomFilter<Object> bloomFilter = redissonClient.getBloomFilter(SysRedisConstant.SKU_BLOOM);

        return bloomFilter.contains(skuId);
    }
}
