package com.atguigu.gmall.order.biz;

/**
 * Author：张世平
 * Date：2022/9/17 11:52
 */

import com.atguigu.gmall.model.vo.order.OrderConfirmDataVo;
import com.atguigu.gmall.model.vo.order.OrderSubmitVo;

/**
 * 确认商品
 */
public interface OrderBizService {

    /**
     * 得到购物车中选中的商品
     * @return
     */
    OrderConfirmDataVo getOrderConfirmData();

    boolean checkTradeNo(String tradeNo);

    Long submitOrder(OrderSubmitVo submitVo, String tradeNo);
}
