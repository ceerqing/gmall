package com.atguigu.gmall.common.constant;

/**
 * Author：张世平
 * Date：2022/9/4 15:29
 */
public class SysRedisConstant {
    public static final String SKU_INFO_CACHE_KEY = "sku:cache:info:";
    public static final String SKU_NULL_VALUE="G";
    public static final String SKU_LOCK="sku:lock:info:";

    public static final Long SKU_NULL_VALUE_TTL=30*60L;
    public static final Long SKU_VALUE_TTL=60*60*24*2L;


    public static final String SKU_BLOOM ="skuId:bloom" ;
}
