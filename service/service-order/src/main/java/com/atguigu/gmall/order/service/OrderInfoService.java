package com.atguigu.gmall.order.service;


import com.atguigu.gmall.model.enums.ProcessStatus;
import com.atguigu.gmall.model.order.OrderInfo;
import com.atguigu.gmall.model.vo.order.OrderSubmitVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author lfy
* @description 针对表【order_info(订单表 订单表)】的数据库操作Service
* @createDate 2022-09-09 14:41:24
*/
public interface OrderInfoService extends IService<OrderInfo> {

    /**
     * 保存订单到数据库
     * @param submitVo
     * @param tradeNo
     * @return
     */
    Long saveOrder(OrderSubmitVo submitVo, String tradeNo) ;


    /**
     * 改變訂單狀態
     * @param userId
     * @param orderId
     * @param closed
     * @param want
     */
    void changeOrderStatus(Long userId, Long orderId, ProcessStatus closed, List<ProcessStatus> want);
}
