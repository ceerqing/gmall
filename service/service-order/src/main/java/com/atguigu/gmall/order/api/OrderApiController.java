package com.atguigu.gmall.order.api;

import com.atguigu.gmall.common.cart.GetUserCartUtils;
import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.order.OrderInfo;
import com.atguigu.gmall.model.vo.order.OrderConfirmDataVo;
import com.atguigu.gmall.model.vo.user.UserAuthInfo;
import com.atguigu.gmall.order.biz.OrderBizService;
import com.atguigu.gmall.order.service.OrderInfoService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.logging.log4j.message.ReusableMessage;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Author：张世平
 * Date：2022/9/17 11:47
 */
@RestController
@RequestMapping("/api/inner/rpc/order")
public class OrderApiController {
    @Autowired
    OrderBizService orderBizService;

    @Autowired
    OrderInfoService orderInfoService;
    //确定用户购物车中选中的数量

    @GetMapping("/getconfirmdata")
    Result<OrderConfirmDataVo> getOrderConfirmData() {
        OrderConfirmDataVo vo = orderBizService.getOrderConfirmData();
        return Result.ok(vo);
    }


    //返回订单数据,用于支付界面
    @GetMapping("/getorderinfo/{orderid}")
    Result<OrderInfo> getOrderInfo(@PathVariable("orderid") Long orderId){
        UserAuthInfo userId = GetUserCartUtils.getCartUser();

        LambdaQueryWrapper<OrderInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrderInfo::getId,orderId).eq(OrderInfo::getUserId,userId);
        OrderInfo orderInfo = orderInfoService.getOne(wrapper);
        return Result.ok(orderInfo);
    }
}
