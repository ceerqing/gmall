package com.atguigu.gmall.order.config;

import com.atguigu.gmall.common.constant.SysRedisConstant;
import com.atguigu.gmall.rabbit.constant.RabbitConstant;

import org.checkerframework.checker.units.qual.C;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * Author：张世平
 * Date：2022/9/20 11:22
 * 订单交换机
 */

public class OrderMqConfig {

    @Bean
    public Exchange getExchange(){
        /**
         * String name,
         * boolean durable,
         * boolean autoDelete,
         * Map<String, Object> arguments
         */
        TopicExchange exchange = new TopicExchange(
                RabbitConstant.EXCHANGE_ORDER,
                true,
                false
        );
        return exchange;
    }

    /**
     * 延时队列
     * @return
     */
    @Bean
    public Queue delayQueue(){
        /**
         * string name,
         * boolean durable,
         * boolean exclusive,
         * boolean autoDelete,
         * @Nullable Map<String, Object> arguments
         */
        Map arguments =new HashMap();
        arguments.put("x-message-ttl", SysRedisConstant.ORDER_CLOSE_TTL*1000);
        arguments.put("x-dead-letter-exchange",RabbitConstant.EXCHANGE_ORDER);
        arguments.put("x-dead-letter-routing-key",RabbitConstant.RK_ORDER_DEAD);

        Queue queue = new Queue(
                RabbitConstant.QUEUE_ORDER_DELAY,
                true,
                false,
                false,
                arguments
        );
        return queue;
    }


    /**
     * 死信队列
     * @return
     */
    @Bean
    public Queue deadQueue(){
        /**
         * string name,
         * boolean durable,
         * boolean exclusive,
         * boolean autoDelete,
         * @Nullable Map<String, Object> arguments
         */
        Queue queue = new Queue(
            RabbitConstant.QUEUE_ORDER_DEAD,
                true,
                false,
                false
        );
        return queue;
    }


    /**
     * 延迟队列和交换机绑定
     * @return
     */
    @Bean
    public Binding orderDelayQueueBinding(){
        /**
         * String destination, 目的地
         * DestinationType destinationType, 目的地类型
         * String exchange, 交换机
         * String routingKey, 路由键
         * @Nullable Map<String, Object> arguments 参数项
         *
         * 这个exchange交换机和这个destinationType类型的目的地（destination）
         * 使用routingKey进行绑定，  将交换机和队列进行绑定，或者将交换机与交换机进行绑定
         */
        return new Binding(
                RabbitConstant.QUEUE_ORDER_DELAY,
                Binding.DestinationType.QUEUE,
                RabbitConstant.EXCHANGE_ORDER,
                RabbitConstant.RK_ORDER_CREATED,
                null
        );
    }

    @Bean
    public Binding orderDeadQueueBinding(){

        /**
         * String destination,
         * Binding.DestinationType destinationType,
         * String exchange,
         * String routingKey,
         * @Nullable Map<String, Object> arguments
         */
        Binding binding = new Binding(
                RabbitConstant.QUEUE_ORDER_DEAD,
                Binding.DestinationType.QUEUE,
                RabbitConstant.EXCHANGE_ORDER,
                RabbitConstant.RK_ORDER_DEAD,
                null
        );
        return binding;
    }
}
