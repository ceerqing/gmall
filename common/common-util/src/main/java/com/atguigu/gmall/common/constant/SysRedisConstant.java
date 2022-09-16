package com.atguigu.gmall.common.constant;

import io.swagger.models.auth.In;

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
    public static final String SKU_HOTSCORE = "sku:hotScore:";
    public static final String USER_TOKEN ="user:token:" ;
    public static final String USERID_HEADER ="userid" ;
    public static final String USERTEMPID_HEADER = "usertempid";
    public static final String USER_CART = "user:cart:";


    /**
     *  购物车中最多商品种类
     */
    public static final Integer CART_GOODS_CLASS_MAX = 200;

    //
    /**
     * 同一个商品最大数量
     */
    public static final Integer CART_GOODS_MAX_NUM = 200;
}
