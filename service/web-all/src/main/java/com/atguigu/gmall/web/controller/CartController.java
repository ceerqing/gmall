package com.atguigu.gmall.web.controller;

import com.atguigu.feigin.client.cart.CartFeignClient;
import com.atguigu.gmall.common.constant.SysRedisConstant;
import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.product.SkuInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Author：张世平
 * Date：2022/9/14 20:52
 */
@Controller
public class CartController {

    @Autowired
    CartFeignClient cartFeignClient;
    //添加商品到购物车
    @GetMapping("/addCart.html")
    public String toCartPage(@RequestParam("skuId") Long skuId,
                             @RequestParam("skuNum") Integer skuNum,
                             @RequestHeader(SysRedisConstant.USERID_HEADER) String userId,
                             Model model) {


        System.out.println("web-all 获取到的用户id："+userId);

        Result<SkuInfo> result = cartFeignClient.addToCart(skuId, skuNum);

        model.addAttribute("skuInfo",result.getData());
        model.addAttribute("skuNum",skuNum);

        return "cart/addCart";
    }
}
