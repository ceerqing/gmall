package com.atguigu.gmall.rabbit.constant;

/**
 * Author：张世平
 * Date：2022/9/20 11:25
 */
public class RabbitConstant {
    public static final String EXCHANGE_ORDER = "order_exchange";
    public static final String RK_ORDER_DEAD = "route.dead";
    public static final String QUEUE_ORDER_DELAY = "order-delay-queue";
    public static final String QUEUE_ORDER_DEAD = "order-dead-queue";
    public static final String RK_ORDER_CREATED = "route.created";
    public static final String ORDER_RETRY ="order:retry" ;
}
