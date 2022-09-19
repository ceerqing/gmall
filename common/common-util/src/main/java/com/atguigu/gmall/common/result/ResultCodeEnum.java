package com.atguigu.gmall.common.result;

import com.atguigu.gmall.common.constant.SysRedisConstant;
import lombok.Getter;

/**
 * 统一返回结果状态信息类
 *
 */
@Getter
public enum ResultCodeEnum {

    SUCCESS(200,"成功"),
    FAIL(201, "失败"),
    SERVICE_ERROR(2012, "服务异常"),

    PAY_RUN(205, "支付中"),

    LOGIN_AUTH(208, "未登陆"),
    LOGIN_FAIL(207, "密码或者用户名输入错误"),
    PERMISSION(209, "没有权限"),
    SECKILL_NO_START(210, "秒杀还没开始"),
    SECKILL_RUN(211, "正在排队中"),
    SECKILL_NO_PAY_ORDER(212, "您有未支付的订单"),
    SECKILL_FINISH(213, "已售罄"),
    SECKILL_END(214, "秒杀已结束"),
    SECKILL_SUCCESS(215, "抢单成功"),
    SECKILL_FAIL(216, "抢单失败"),
    SECKILL_ILLEGAL(217, "请求不合法"),
    SECKILL_ORDER_SUCCESS(218, "下单成功"),
    COUPON_GET(220, "优惠券已经领取"),
    COUPON_LIMIT_GET(221, "优惠券已发放完毕"),

    CARTGOODSCLASSOVERFLOW(222,"购物车中的商品种类数量不能超过"+ SysRedisConstant.CART_GOODS_CLASS_MAX),
    CAET_GOODS_NUM_ORVERFLOW(233, "购物车中一件商品的的数量不能超过"+SysRedisConstant.CART_GOODS_MAX_NUM),
    TEMP_CAET_NOT_CLEAR(224, "购物车已满，" +
            "临时购物车中的一部分商品还没有完全添加进入用户购物车，请删除或者结算购物车中的一部分商品显示其他商品"),

    ORDER_TOKEN_INVALID(225,"页面过期，请重新刷新页面" ),
    ALL_GOODS_NO_STOCK(226, "购物车中所有的商品都没有库存"),
    GOODS_PRICE_UNSAME(227,"商品价格发生改变，请重新刷新数据库" );

    private Integer code;

    private String message;

    private ResultCodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
