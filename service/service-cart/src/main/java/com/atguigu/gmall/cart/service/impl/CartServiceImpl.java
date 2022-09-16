package com.atguigu.gmall.cart.service.impl;

import com.atguigu.feigin.client.product.SkuFeignDetail;
import com.atguigu.gmall.cart.service.CartService;
import com.atguigu.gmall.common.cart.GetUserCartUtils;
import com.atguigu.gmall.common.constant.SysRedisConstant;
import com.atguigu.gmall.common.execption.GmallException;
import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.common.result.ResultCodeEnum;
import com.atguigu.gmall.common.util.Jsons;
import com.atguigu.gmall.model.cart.CartInfo;
import com.atguigu.gmall.model.product.SkuInfo;
import com.atguigu.gmall.model.vo.user.UserAuthInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Author：张世平
 * Date：2022/9/15 12:49
 */
@Service
@Slf4j
public class CartServiceImpl implements CartService {

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    SkuFeignDetail skuFeignDetail;
    @Override
    public SkuInfo addCart(Long skuId, Integer num) {
        //添加一个商品到购物车
        //哪一个用户
        String cartRedisKey=comfrimKey();//哪一个购物车
        //登录用户，临时用户，
        SkuInfo skuInfo=addGoodsToCart(cartRedisKey,skuId,num);

        //临时用户的购物车的过期时间
        UserAuthInfo cartUser = GetUserCartUtils.getCartUser();

        if (cartUser.getUserId() == null) {
            String tempComfirmKey = SysRedisConstant.USER_CART + cartUser.getUserTempId();
            stringRedisTemplate.boundHashOps(tempComfirmKey).expire(90, TimeUnit.DAYS);
        }
        //第一次添加，第二次添加
        return skuInfo;

    }

    private SkuInfo addGoodsToCart(String cartRedisKey, Long skuId, Integer num) {

        BoundHashOperations<String, String, String> hashOps = stringRedisTemplate.boundHashOps(cartRedisKey);

        Boolean exist = hashOps.hasKey(skuId+"");

        if (!exist){
            //第一次添加到购物车
            // 远程调用查询,
            if (hashOps.size()<SysRedisConstant.CART_GOODS_CLASS_MAX){
                Result<SkuInfo> skuInfo = skuFeignDetail.getSkuInfo(skuId);
                CartInfo cartInfo=skuInfoToCart(skuInfo.getData());
                //我觉得可以在这里进行购物车的合并操作
                //设置数量
                cartInfo.setSkuNum(num);

                String jsonCart = Jsons.toStr(cartInfo);
                hashOps.put(skuId+"",jsonCart);
                return skuInfo.getData();
            }
            throw new GmallException(ResultCodeEnum.CARTGOODSCLASSOVERFLOW);


        }else {
            //之前存在，去redis中查
            //多次添加到购物车，修改数量
            BigDecimal price = skuFeignDetail.getPrice(skuId).getData();

            String jsonCart = hashOps.get(skuId+"");
            CartInfo cartInfo = Jsons.toObject(jsonCart, CartInfo.class);
            //修改原来的购物车的数量
            cartInfo.setSkuNum(cartInfo.getSkuNum() + num);
            //重新在查一遍商品的价格
            cartInfo.setSkuPrice(price);
            //修改更新时间
            cartInfo.setUpdateTime(new Date());
            //写回redis中
            hashOps.put(skuId+"",Jsons.toStr(cartInfo));

            //返回数据给controller
            SkuInfo skuInfo = cartInfoToSkuInfo(cartInfo);
            return skuInfo;
        }
    }

    //在合并的时候给前端提示有一部分商品还在临时购物车中，将用户商品中的商品删除或者消费之后在给用展示
    private SkuInfo mergeAddGoodsToCart(String cartRedisKey, Long skuId, Integer num) {

        BoundHashOperations<String, String, String> hashOps = stringRedisTemplate.boundHashOps(cartRedisKey);

        Boolean exist = hashOps.hasKey(skuId+"");

        if (!exist){
            //第一次添加到购物车,合并购物车
            // 远程调用查询,
            if (hashOps.size()<SysRedisConstant.CART_GOODS_CLASS_MAX){
                Result<SkuInfo> skuInfo = skuFeignDetail.getSkuInfo(skuId);
                CartInfo cartInfo=skuInfoToCart(skuInfo.getData());
                //我觉得可以在这里进行购物车的合并操作
                //设置数量
                cartInfo.setSkuNum(num);

                String jsonCart = Jsons.toStr(cartInfo);
                hashOps.put(skuId+"",jsonCart);
                return skuInfo.getData();
            }
            throw new GmallException(ResultCodeEnum.TEMP_CAET_NOT_CLEAR);


        }else {
            //之前存在，去redis中查
            //多次添加到购物车，修改数量
            BigDecimal price = skuFeignDetail.getPrice(skuId).getData();

            String jsonCart = hashOps.get(skuId+"");
            CartInfo cartInfo = Jsons.toObject(jsonCart, CartInfo.class);
            //修改原来的购物车的数量
            cartInfo.setSkuNum(cartInfo.getSkuNum() + num);
            //重新在查一遍商品的价格
            cartInfo.setSkuPrice(price);
            //修改更新时间
            cartInfo.setUpdateTime(new Date());
            //写回redis中
            hashOps.put(skuId+"",Jsons.toStr(cartInfo));

            //返回数据给controller
            SkuInfo skuInfo = cartInfoToSkuInfo(cartInfo);
            return skuInfo;
        }
    }

    private SkuInfo cartInfoToSkuInfo(CartInfo cartInfo) {
        SkuInfo skuInfo = new SkuInfo();
        skuInfo.setSkuName(cartInfo.getSkuName());
        skuInfo.setSkuDefaultImg(cartInfo.getImgUrl());
        skuInfo.setId(cartInfo.getSkuId());
        return skuInfo;
    }

    private CartInfo skuInfoToCart(SkuInfo data) {
        CartInfo cartInfo = new CartInfo();
        //设置用户id
        cartInfo.setUserId(GetUserCartUtils.getCartUser().getUserId()+"");
        cartInfo.setSkuId(data.getId());
        cartInfo.setImgUrl(data.getSkuDefaultImg());
        cartInfo.setSkuName(data.getSkuName());
        cartInfo.setIsChecked(1);
        cartInfo.setCreateTime(new Date());
        cartInfo.setUpdateTime(new Date());
        cartInfo.setSkuPrice(data.getPrice());
        cartInfo.setCartPrice(data.getPrice());
        return cartInfo;
    }

    /**
     * 得到该用户应该放在哪一个键中
     * @return
     */
    @Override
    public String comfrimKey() {
        UserAuthInfo cartUser = GetUserCartUtils.getCartUser();
        //合并购物车
        if (cartUser.getUserId() !=null){
            log.warn("登录用户id："+cartUser.getUserId());
            return SysRedisConstant.USER_CART+cartUser.getUserId();
        }
        log.warn("临时用户id："+cartUser.getUserTempId());//用相同的前缀吧。。。
        return SysRedisConstant.USER_CART+cartUser.getUserTempId();
    }

    @Override
    public CartInfo getCartGoods(String cartKey, Long skuId) {
        BoundHashOperations<String, String, String> haspOps = stringRedisTemplate.boundHashOps(cartKey);
        CartInfo cartInfo = Jsons.toObject(haspOps.get(skuId + ""), CartInfo.class);
        return cartInfo;
    }

    //获取购物车所有的商品，待排序，这个方法的返回值不可能为空，，
    @Override
    public List<CartInfo> getAllCartGoods(String comfrimKey) {
        BoundHashOperations<String, String, String> hashOps = stringRedisTemplate.boundHashOps(comfrimKey);
        List<CartInfo> cartInfos = hashOps.values().stream()
                .map(str -> Jsons.toObject(str, CartInfo.class))
                .sorted(Comparator.comparing(CartInfo::getCreateTime).reversed())//默认规则是从小到大进行排序
                .collect(Collectors.toList());
        return cartInfos;
    }

    @Override
    public void updateCartGoodsNum(Long skuId, Integer num, String comfrimKey) {

        CartInfo cartGoods = getCartGoods(comfrimKey, skuId);
        //更新
        cartGoods.setSkuNum(cartGoods.getSkuNum()+num);
        cartGoods.setUpdateTime(new Date());
        //重新设置redis中的值
        BoundHashOperations<String, String, String> hashOps = stringRedisTemplate.boundHashOps(comfrimKey);
        hashOps.put(skuId+"",Jsons.toStr(cartGoods));
    }

    @Override
    public void checkCart(Long skuId, Integer status, String comfrimKey) {
        CartInfo cartGoods = getCartGoods(comfrimKey, skuId);
        cartGoods.setIsChecked(status);
        cartGoods.setUpdateTime(new Date());
        //写入redis
        BoundHashOperations<String, String, String> hashOps = stringRedisTemplate.boundHashOps(comfrimKey);
        hashOps.put(skuId+"",Jsons.toStr(cartGoods));
    }

    @Override
    public void deleteCartGoods(Long skuId, String comfrimKey) {
        BoundHashOperations<String, String, String> hashOps = stringRedisTemplate.boundHashOps(comfrimKey);
        hashOps.delete(skuId+"");
    }

    @Override
    public void deleteCartCheckGoods(String comfrimKey) {
        BoundHashOperations<String, String, String> hashOps = stringRedisTemplate.boundHashOps(comfrimKey);

        List<CartInfo> checkGoods=getCheckCartGoods(comfrimKey);
        //获取所有的商品
        List<String> checkSkuIds = checkGoods.stream()
                .map(cartInfo -> cartInfo.getSkuId() + "")
                .collect(Collectors.toList());
        //有可能没有选中，前端因该做一下
        if (checkSkuIds!=null&&checkSkuIds.size()>0){
            hashOps.delete(checkSkuIds.toArray());
        }

        //删除商品的isCheck=1的删除

    }

    //得到所有的被选中的商品
    @Override
    public List<CartInfo> getCheckCartGoods(String comfrimKey) {
        List<CartInfo> allCartGoods = getAllCartGoods(comfrimKey);
        List<CartInfo> checkCartGoods = allCartGoods.stream()
                .filter(cartInfo -> cartInfo.getIsChecked() == 1)
                .collect(Collectors.toList());
        return checkCartGoods;
    }

    @Override
    public void mergeCart() {
        //如果临时用户存在，查添加到用户
        UserAuthInfo cartUser = GetUserCartUtils.getCartUser();
        //
        if (cartUser.getUserId() != null && !StringUtils.isEmpty(cartUser.getUserTempId())) {
            //临时用户redisKey
            String tempConfirmKey = SysRedisConstant.USER_CART + cartUser.getUserTempId();

            BoundHashOperations<String, String, String> hashOps = stringRedisTemplate.boundHashOps(tempConfirmKey);
            //获取所有临时购物车中的商品
            List<CartInfo> allTempCart = getAllCartGoods(tempConfirmKey);

            Long userId = cartUser.getUserId();
            for (CartInfo cartInfo : allTempCart) {

                mergeAddGoodsToCart(SysRedisConstant.USER_CART+userId,
                        cartInfo.getSkuId(),cartInfo.getSkuNum());

                hashOps.delete(cartInfo.getSkuId()+"");

            }
          /*  if (allTempCart != null && allTempCart.size() > 0) {
                //存在，需要合并，合并过程中，购物车商品个数限制。和一个商品的个数限制，这样写有问题，因该调用不同的方法
                //给前端不同的提示
                allTempCart.forEach(tempCart -> {
                    addGoodsToCart(SysRedisConstant.USER_CART + cartUser.getUserId(),
                            tempCart.getSkuId(), tempCart.getSkuNum());
                });
                //删除原来的临时用户的购物车数据
                List<String> tempSkuIds = allTempCart.stream()
                        .map(tempCart -> tempCart.getSkuId() + "")
                        .collect(Collectors.toList());

                BoundHashOperations<String, String, String> hashOps = stringRedisTemplate.boundHashOps(tempConfirmKey);

                hashOps.delete(tempSkuIds.toArray());

            }*/
        }
    }
}
