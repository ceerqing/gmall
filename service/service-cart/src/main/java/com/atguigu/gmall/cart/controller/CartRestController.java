package com.atguigu.gmall.cart.controller;

import com.atguigu.gmall.cart.service.CartService;
import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.cart.CartInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Author：张世平
 * Date：2022/9/15 14:58
 */
@RequestMapping("/api/cart")
@RestController
public class CartRestController {


    @Autowired
    CartService cartService;

    /**
     * 购物车列表
     * @return
     */
    //获取用户所有商品的列表
    @GetMapping("/cartList")
    public Result cartList(){
        String comfrimKey = cartService.comfrimKey();
        //查看购物车的时候最适合合并购物车，合并购物车的时候不可以抛异常
        cartService.mergeCart();

        List<CartInfo> goods= cartService.getAllCartGoods(comfrimKey);
        return Result.ok(goods);
    }



    // 更新购物车商品的数量
    //http://api.gmall.com/api/cart/addToCart/56/1
    @PostMapping("/addToCart/{skuId}/{num}")
    public Result updateCartGoodsNum(@PathVariable("skuId")Long skuId,
                                     @PathVariable("num") Integer num){
        String comfrimKey = cartService.comfrimKey();
        cartService.updateCartGoodsNum(skuId,num,comfrimKey );
        return Result.ok();
    }

    //选中和不选中
    //http://api.gmall.com/api/cart/checkCart/56/1
    @GetMapping("/checkCart/{skuId}/{status}")
    public Result checkCart(@PathVariable("skuId")Long skuId,
                            @PathVariable("status")Integer status){
        String comfrimKey = cartService.comfrimKey();
        cartService.checkCart(skuId,status,comfrimKey);
        return Result.ok();
    }

    //http://api.gmall.com/api/cart/deleteCart/56
    //删除一个购物车中的商品
    @DeleteMapping("/deleteCart/{skuId}")
    public Result deleteCartGoods(@PathVariable("skuId")Long skuId){

        String comfrimKey = cartService.comfrimKey();
        cartService.deleteCartGoods(skuId,comfrimKey);
        return Result.ok();
    }

}
