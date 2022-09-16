package com.atguigu.gmall.web.controller;

import com.atguigu.feigin.client.cart.CartFeignClient;
import com.atguigu.gmall.common.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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
                             Model model) {

        Result<Object> result = cartFeignClient.addToCart(skuId, skuNum);

        if (result.isOk()){

            model.addAttribute("skuInfo",result.getData());
            model.addAttribute("skuNum",skuNum);

            return "cart/addCart";
        }else {
            model.addAttribute("msg",result.getMessage());
            return "cart/error";
        }
    }


    /**
     * 购物车列表页
     * @return
     */
    @GetMapping("/cart.html")
    public String cartHtml(){
        //会发一个异步请求去查询购物车中所有的商品
        return "cart/index";
    }


    @GetMapping("/cart/deleteChecked")
    public String deleteChecked(){

        /**
         * redirect: 重定向
         * forward: 转发
         */
        cartFeignClient.deleteChecked();
        return "redirect:http://cart.gmall.com/cart.html";
    }
}
