package com.atguigu.gmall.order.listener;

import com.atguigu.gmall.common.util.Jsons;
import com.atguigu.gmall.model.to.mq.OrderMsg;
import com.atguigu.gmall.order.biz.OrderBizService;
import com.atguigu.gmall.rabbit.constant.RabbitConstant;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;


import java.io.IOException;

/**
 * Author：张世平
 * Date：2022/9/20 16:34
 */
@Component
@Slf4j
public class OrderCloseListener {

    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    OrderBizService orderBizService;

    @RabbitListener(queues = RabbitConstant.QUEUE_ORDER_DEAD)
    public void closeOrder(Message message, Channel channel) throws IOException {
       //处理死心队列中的数据
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        //拿到数据，手动确定，多次重试，重试失败在丢弃消息

        //拿到数据
        OrderMsg orderMsg= Jsons.mqToObj(message,OrderMsg.class);
        try {
            //处理数据  ，，这些订单有的可能是已经被支付了的
            log.info("监听到订单{}超时可能还未处理",orderMsg);
            orderBizService.changeOrderStatus(orderMsg.getUserId(),orderMsg.getOrderId());

            //处理完成之后手动确定，保证消息的可靠投递
            channel.basicAck(deliveryTag,false);
        }catch (Exception e){
            log.error("订单{}关闭失败，正在重试,失败原因",orderMsg,e);
            Long reTryCount = stringRedisTemplate.opsForValue()
                    .increment(RabbitConstant.ORDER_RETRY + orderMsg.getOrderId());
            //重新回复到队列中，
            if (reTryCount<=10){
                channel.basicNack(deliveryTag,false,true);
            }else{
                //消息丢失,可以调用方法存到数据库中，将失败投递的消息
                log.error("订单id:{}为的订单改变状态失败，请自行手动处理");
                channel.basicNack(deliveryTag,false,false);
            }
        }
    }
}
