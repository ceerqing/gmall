package com.atguigu.gmall.order.biz.impl;
import java.math.BigDecimal;

import com.atguigu.feigin.client.cart.CartFeignClient;
import com.atguigu.feigin.client.product.SkuFeignDetail;
import com.atguigu.feigin.client.user.UserFeignClient;
import com.atguigu.feigin.client.ware.WareManageFeignClient;
import com.atguigu.gmall.common.cart.GetUserCartUtils;
import com.atguigu.gmall.common.constant.SysRedisConstant;
import com.atguigu.gmall.common.execption.GmallException;
import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.common.result.ResultCodeEnum;
import com.atguigu.gmall.model.cart.CartInfo;
import com.atguigu.gmall.model.enums.ProcessStatus;
import com.atguigu.gmall.model.user.UserAddress;
import com.atguigu.gmall.model.vo.order.CartInfoVo;
import com.atguigu.gmall.model.vo.order.OrderConfirmDataVo;
import com.atguigu.gmall.model.vo.order.OrderSubmitVo;
import com.atguigu.gmall.model.vo.user.UserAuthInfo;
import com.atguigu.gmall.order.biz.OrderBizService;
import com.atguigu.gmall.order.service.OrderInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Author：张世平
 * Date：2022/9/17 12:00
 */
@Service
public class OrderBizServiceImpl  implements OrderBizService {

    @Autowired
    CartFeignClient cartFeignClient;

    @Autowired
    UserFeignClient userFeignClient;

    @Autowired
    SkuFeignDetail skuFeignDetail;

    @Autowired
    WareManageFeignClient wareManageFeignClient;

    @Autowired
    StringRedisTemplate stringRedisTemplate;


    @Autowired
    OrderInfoService orderInfoService;

    @Override
    public OrderConfirmDataVo getOrderConfirmData(){
        OrderConfirmDataVo orderConfirmDataVo = new OrderConfirmDataVo();
        //查询购物车中选中的商品
        List<CartInfo> cartInfos=cartFeignClient.getCheckCartGoods().getData();
        //转换成为前端要展式的javaBean
        List<CartInfoVo> cartInfoVos = cartInfos.stream()
                .map(cartInfo -> {
            CartInfoVo cartInfoVo = new CartInfoVo();
            cartInfoVo.setSkuId(cartInfo.getSkuId());
            cartInfoVo.setImgUrl(cartInfo.getImgUrl());
            cartInfoVo.setSkuName(cartInfo.getSkuName());

            Result<BigDecimal> price = skuFeignDetail.getPrice(cartInfo.getSkuId());
                    //远程调用重新查询商品的价格
            cartInfoVo.setOrderPrice(price.getData());
            //查询库存，查询商品是否还有库存
            String hasStock = wareManageFeignClient.hasStock(cartInfo.getSkuId(), cartInfo.getSkuNum());
            cartInfoVo.setHasStock(hasStock);
            cartInfoVo.setSkuNum(cartInfo.getSkuNum());
            return cartInfoVo;
        })
                .collect(Collectors.toList());
        orderConfirmDataVo.setDetailArrayList(cartInfoVos);

        //查询购物车中有多少件商品
        Integer totalNum = cartInfoVos.stream()
                .collect(Collectors.summingInt(CartInfoVo::getSkuNum));
        orderConfirmDataVo.setTotalNum(totalNum);

        //查询总价格 bigDecimal.multiply
        BigDecimal goodsTotalAmount = cartInfos.stream()
                .map(cartInfo -> cartInfo.getSkuPrice().multiply(new BigDecimal(cartInfo.getSkuNum() + "")))
                .reduce(BigDecimal::add).get();
        orderConfirmDataVo.setTotalAmount(goodsTotalAmount);


        //查询用户的收货地址列表
        List<UserAddress> listAddress=userFeignClient.getUserAddByUserId().getData();
        orderConfirmDataVo.setUserAddressList(listAddress);

        //交易追踪号,创建订单token存入redis，防止重复下单，在下单成功之后，删除订单token

        String tradeNum =createTradeNum();
        orderConfirmDataVo.setTradeNo(tradeNum);
        return orderConfirmDataVo;
    }

    private String createTradeNum() {
        UserAuthInfo cartUser = GetUserCartUtils.getCartUser();

        String token=System.currentTimeMillis()+"_"+cartUser.getUserId();
        stringRedisTemplate.opsForValue().set(SysRedisConstant.ORDER_TOKEN+token,1+"",15, TimeUnit.MINUTES);
        //存到redis中
        return token;
    }

    @Override
    public boolean checkTradeNo(String tradeNo) {
        //1、先看有没有，如果有就是正确令牌, 1, 0 。脚本校验令牌
        String lua = "if redis.call(\"get\",KEYS[1]) == ARGV[1] then " +
                "    return redis.call(\"del\",KEYS[1]) " +
                "else " +
                "    return 0 " +
                "end";

        /**
         * RedisScript<T> script,
         * List<K> keys, Object... args
         */
        Long execute = stringRedisTemplate.execute(new DefaultRedisScript<Long>(lua, Long.class),
                Arrays.asList(SysRedisConstant.ORDER_TOKEN + tradeNo),
                new String[]{"1"});

        if(execute > 0){
            //令牌正确，并且已经删除
            return true;
        }
//        String val = redisTemplate.opsForValue().get(SysRedisConst.ORDER_TEMP_TOKEN + tradeNo);
//        if(!StringUtils.isEmpty(val)){
//            //redis有这个令牌。校验成功
//            redisTemplate.delete(SysRedisConst.ORDER_TEMP_TOKEN + tradeNo);
//            return true;
//        }

        return false;
    }

    @Override
    public Long submitOrder(OrderSubmitVo submitVo, String tradeNo) {
        //查询订单令牌
        boolean flag = checkTradeNo(tradeNo);
        if (!flag){
            throw new GmallException(ResultCodeEnum.ORDER_TOKEN_INVALID);
        }
        //查询是否还有库存
        List<CartInfoVo> cartInfoVos = submitVo.getOrderDetailList();

        List<CartInfoVo> hasStockGoods = cartInfoVos.stream()
                .filter(cartInfoVo -> "1".equals(cartInfoVo.getHasStock()))
                .collect(Collectors.toList());

        if (hasStockGoods.size()<=0){
            throw new GmallException(ResultCodeEnum.ALL_GOODS_NO_STOCK);
        }
        //没有库存的商品就过滤掉了
        //去查询数据库中存在库存商品的价格
        //删除不存在有库存的商品,再去数据库查询有库存商品的价格

        cartInfoVos
                .removeIf(cartInfoVo -> cartInfoVo.getHasStock().equals("0"));
        //多余了

        //查询价格是否改变
        List<String> changePriceGoods =new ArrayList<>();
        cartInfoVos.stream().forEach(cartInfoVo -> {
            BigDecimal orderPrice = cartInfoVo.getOrderPrice();
            BigDecimal nowPrice = skuFeignDetail.getPrice(cartInfoVo.getSkuId()).getData();
            if (!nowPrice.equals(orderPrice)){
                changePriceGoods.add(cartInfoVo.getSkuName());
            }
        });
        if (changePriceGoods.size()>0){
            //可加上改变价格的商品
            throw new GmallException(
                    ResultCodeEnum.GOODS_PRICE_UNSAME.getMessage(),
                    ResultCodeEnum.GOODS_PRICE_UNSAME.getCode());
        }

        //4、把订单信息保存到数据库
        Long orderId = orderInfoService.saveOrder(submitVo,tradeNo);

        //删除购物车中选中有货的商品

        cartFeignClient.deleteCheckAndHasStockGoods(hasStockGoods);

        //设置订单的过期时间，延时任务加定时补偿

     /*   //45min不支付就要关闭。
        ScheduledExecutorService pool = Executors.newScheduledThreadPool(10);
        pool.schedule(()->{
            closeOrder(orderId);
        },45,TimeUnit.MINUTES);*/

        return orderId;
    }

    @Override
    public void changeOrderStatus(Long userId, Long orderId) {
        //修改成这样
        ProcessStatus closed = ProcessStatus.CLOSED;
        //这个样子的都修改
        List<ProcessStatus> want=Arrays.
                asList(ProcessStatus.UNPAID,ProcessStatus.FINISHED);

        orderInfoService.changeOrderStatus(userId,orderId,closed,want);
    }

    @Scheduled(cron = "0 */5 * * * ?")
    private void closeOrder(Long orderId) {
    }


}
