package com.atguigu.gmall.cart.api;

import com.atguigu.gmall.common.constant.SysRedisConstant;
import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.product.SkuInfo;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Author：张世平
 * Date：2022/9/14 20:58
 */
@RestController
@RequestMapping("/api/inner/rpc/cart")
public class CartController {
    @GetMapping("/addToCart")
    public Result<SkuInfo> addToCart(@RequestParam("skuId") Long skuId,
                                     @RequestParam("num") Integer num,
                                     HttpServletRequest req) {

        String userId = req.getHeader(SysRedisConstant.USERID_HEADER);
        System.out.println("userId = " + userId);
        //将商品加入购物车，存入到redis中
        return Result.ok();
    }
}
