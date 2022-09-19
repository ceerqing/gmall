package com.atguigu.feigin.client.order;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.order.OrderInfo;
import com.atguigu.gmall.model.vo.order.OrderConfirmDataVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Author：张世平
 * Date：2022/9/17 11:44
 */
@FeignClient("service-order")
@RequestMapping("/api/inner/rpc/order")

public interface OrderFeignClient {
    @GetMapping("/getconfirmdata")
    Result<OrderConfirmDataVo> getOrderConfirmData();

    @GetMapping("/getorderinfo/{orderid}")
    Result<OrderInfo> getOrderInfo(@PathVariable("orderid") Long orderId);
}
