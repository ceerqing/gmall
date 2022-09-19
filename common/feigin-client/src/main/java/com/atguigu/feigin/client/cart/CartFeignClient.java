package com.atguigu.feigin.client.cart;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.cart.CartInfo;
import com.atguigu.gmall.model.vo.order.CartInfoVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Author：张世平
 * Date：2022/9/14 20:59
 */
@RequestMapping("/api/inner/rpc/cart")
@FeignClient("service-cart")
public interface CartFeignClient {

    /**
     * 将商品加入购物车
     * @param skuId
     * @param num
     * @return
     */
    @GetMapping("/addToCart")
    public Result<Object> addToCart(@RequestParam("skuId") Long skuId,
                                     @RequestParam("num") Integer num);
    @GetMapping("/deleteCheckGoods")
    Result deleteChecked();

    @GetMapping("/getallcheckgoods")
    Result<List<CartInfo>> getCheckCartGoods();

    @DeleteMapping("/deletecheckhasstockgoods")
    Result deleteCheckAndHasStockGoods(@RequestBody List<CartInfoVo> hasStockGoods);
}
