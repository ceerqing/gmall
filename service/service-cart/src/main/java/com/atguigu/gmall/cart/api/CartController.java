package com.atguigu.gmall.cart.api;

import com.atguigu.gmall.cart.service.CartService;
import com.atguigu.gmall.common.constant.SysRedisConstant;
import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.product.SkuInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Author：张世平
 * Date：2022/9/14 20:58
 */
@RestController
@RequestMapping("/api/inner/rpc/cart")
public class CartController {

    @Autowired
    CartService cartService;
    @GetMapping("/addToCart")
    public Result<SkuInfo> addToCart(@RequestParam("skuId") Long skuId,
                                     @RequestParam("num") Integer num
                                     ) {
        //登录用户id和临时用户id都传进来了
        SkuInfo skuInfo=cartService.addCart(skuId,num);
        //将商品加入购物车，存入到redis中

        return Result.ok(skuInfo);
    }

    /**
     * 删除选中的商品
     * @return
     */
    @GetMapping("/deleteCheckGoods")
    Result deleteCartCheckGoods(){
        String comfrimKey = cartService.comfrimKey();
        cartService.deleteCartCheckGoods(comfrimKey);
        return Result.ok();
    }
}
