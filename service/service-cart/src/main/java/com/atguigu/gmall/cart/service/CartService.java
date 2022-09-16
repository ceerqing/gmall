package com.atguigu.gmall.cart.service;

import com.atguigu.gmall.model.cart.CartInfo;
import com.atguigu.gmall.model.product.SkuInfo;

import java.util.List;

/**
 * Author：张世平
 * Date：2022/9/15 12:49
 */
public interface CartService {

    /**
     * 先购物车中添加商品
     * @param skuId
     * @param num
     * @return
     */
    SkuInfo addCart(Long skuId, Integer num);

    /**
     * 确定在哪一个reids key中
     * @return
     */
    String comfrimKey();

    /**
     * 根据skuId和reids的key获取到一个商品
     * @param cartKey
     * @param skuId
     * @return
     */
    CartInfo getCartGoods(String cartKey,Long skuId);

    /**
     * 获取一个用户购物车的所有商品
     * @param comfrimKey
     * @return
     */

    /**
     * 跟新购物车中某一个商品的数量
     * @param skuId 商品的skuId
     * @param num 添加或减少的数量
     * @param comfrimKey 确定在哪一个redis key
     */
    void updateCartGoodsNum(Long skuId, Integer num, String comfrimKey);

    /**
     * 修改购物车商品的选中状态
     * @param skuId
     * @param status
     * @param comfrimKey
     */

    void checkCart(Long skuId, Integer status, String comfrimKey);

    /**
     * 删除购物车中的商品
     * @param skuId
     * @param comfrimKey
     */
    void deleteCartGoods(Long skuId, String comfrimKey) ;

    /**
     * 删除选中的商品
     * @param comfrimKey
     */
    void deleteCartCheckGoods(String comfrimKey);

    List<CartInfo> getAllCartGoods(String comfirmKey);

    List<CartInfo> getCheckCartGoods(String comfrimKey);

    /**
     * 合并购物车，临时用户可能会往购物车中添加商品
     */
    void mergeCart();
}
